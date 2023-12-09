package template.authorization.helpers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import template.authorization.exceptions.NotAuthorizedException;
import template.authorization.models.*;
import template.authorization.services.InternalRoleManager;
import template.authorization.services.TenantRoleManager;
import template.authorization.utilities.RoleDelegator;
import template.global.constants.EnvironmentVariable;
import template.global.constants.GlobalVariable;

import java.util.List;

@Service
public class RoleBouncer implements AuthorizationBouncer {
    private final InternalRoleManager internalRoleManager;
    private final TenantRoleManager tenantRoleManager;
    private final RoleDelegator roleDelegator;
    private final Environment env;

    public RoleBouncer(InternalRoleManager internalRoleManager,
                       TenantRoleManager tenantRoleManager,
                       RoleDelegator roleDelegator,
                       Environment env) {
        this.internalRoleManager = internalRoleManager;
        this.tenantRoleManager = tenantRoleManager;
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

        if (request.getRequestURI().contains("/internal")) {
            List<InternalRole> roles;
            // Use provided user id to check if user has any roles
            Integer userId = (Integer) request.getAttribute("user_id");
            if (userId != null) {
                roles = internalRoleManager.getListByUserId(userId);

                if (verifyInternalPermission(scope, roles)) return;
            }

            if (request.getRequestURI().equals("/internal/user/" + userId))
                return;
        } else {
            List<TenantRole> roles;
            Integer userId = (Integer) request.getAttribute("user_id");
            if (userId != null) {
                roles = tenantRoleManager.getListByUserId(userId);

                if (verifyPermission(scope, roles)) return;
            }

            if (request.getRequestURI().equals("/user/" + userId))
                return;
        }

        throw new NotAuthorizedException(scope);
    }

    private boolean verifyInternalPermission(String scope,
                                             List<InternalRole> internalRoleList) {
        boolean verified = false;

        for (InternalRole role : internalRoleList) {
            for (InternalPermission permission : role.getPermissions()) {
                if (permission.validateScope(scope)) {
                    verified = true;
                }
            }
        }

        return verified;
    }

    private boolean verifyPermission(String scope,
                                     List<TenantRole> roleList) {
        boolean verified = false;

        for (TenantRole role : roleList) {
            for (TenantPermission permission : role.getPermissions()) {
                if (permission.validateScope(scope)) {
                    verified = true;
                }
            }
        }

        return verified;
    }
}