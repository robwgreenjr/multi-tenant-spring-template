package template.authorization.helpers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import template.authorization.exceptions.NotAuthorizedException;
import template.authorization.models.InternalPermission;
import template.authorization.models.InternalRole;
import template.authorization.models.WhiteListProvider;
import template.authorization.services.InternalRoleManager;
import template.authorization.utilities.RoleDelegator;
import template.global.constants.EnvironmentVariable;
import template.global.constants.GlobalVariable;

import java.util.List;

@Service
public class RoleBouncer implements AuthorizationBouncer {
    private final InternalRoleManager roleManager;
    private final RoleDelegator roleDelegator;
    private final Environment env;

    public RoleBouncer(InternalRoleManager roleManager,
                       RoleDelegator roleDelegator,
                       Environment env) {
        this.roleManager = roleManager;
        this.roleDelegator = roleDelegator;
        this.env = env;
    }

    @Override
    public void authorize(HttpServletRequest request) {
        String scope = roleDelegator.buildScope(request);
        if (scope.isEmpty()) return;

        String environment = env.getProperty(GlobalVariable.ENVIRONMENT);
        if (environment != null &&
            (environment.equalsIgnoreCase(EnvironmentVariable.LOCAL) ||
                environment.equalsIgnoreCase(EnvironmentVariable.TEST))) return;

        /*
         * Any path added to whitelist will grant user access
         */
        for (String white : WhiteListProvider.getWhiteList()) {
            if (request.getRequestURI().contains(white)) return;
        }

        // Run through any potential authorization methods
        List<InternalRole> roleModels;

        // TODO: Handle different types of users internal and admin
        // Use provided user id to check if user has any roles
        Integer userId = (Integer) request.getAttribute("user_id");
        if (userId != null) {
            roleModels = roleManager.getListByUserId(userId);

            if (verifyPermission(scope, roleModels)) return;
        }

        if (request.getRequestURI().equals("/user/" + userId)) return;

        throw new NotAuthorizedException(scope);
    }

    private boolean verifyPermission(String scope,
                                     List<InternalRole> roleModelList) {
        boolean verified = false;

        for (InternalRole roleModel : roleModelList) {
            for (InternalPermission permissionModel : roleModel.getPermissions()) {
                if (permissionModel.validateScope(scope)) {
                    verified = true;
                }
            }
        }

        return verified;
    }
}