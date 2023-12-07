package template.authentication.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import template.authentication.exceptions.PasswordIncorrectException;
import template.authentication.exceptions.PasswordNotSetException;
import template.authentication.exceptions.UserPasswordNotFoundException;
import template.authentication.helpers.JwtSpecialist;
import template.authentication.models.Jwt;
import template.authentication.models.TenantUserPassword;
import template.authorization.models.TenantPermission;
import template.authorization.models.TenantRole;
import template.authorization.services.TenantRoleManager;
import template.global.exceptions.UnknownServerException;
import template.global.services.StringEncoder;
import template.tenants.exceptions.TenantUserNotFoundException;
import template.tenants.models.TenantUser;

import java.util.List;
import java.util.Optional;

@Service("TenantUserLogin")
public class TenantUserLogin
    implements UserLoginHandler<TenantUserPassword> {
    private final UserPasswordManager<TenantUserPassword> userPasswordManager;
    private final JwtSpecialist<TenantUser> simpleJwtSpecialist;
    private final StringEncoder bCryptEncoder;
    private final TenantRoleManager roleManager;

    public TenantUserLogin(
        UserPasswordManager<TenantUserPassword> userPasswordManager,
        JwtSpecialist<TenantUser> simpleJwtSpecialist,
        @Qualifier("BCryptEncoder")
        StringEncoder bCryptEncoder,
        TenantRoleManager roleManager) {
        this.userPasswordManager = userPasswordManager;
        this.simpleJwtSpecialist = simpleJwtSpecialist;
        this.bCryptEncoder = bCryptEncoder;
        this.roleManager = roleManager;
    }

    @Override
    public Jwt jwtProvider(String identifier, String password) {
        TenantUserPassword userPassword = login(identifier, password);

        String scopeList = buildScopeList(userPassword.getUser().getId());
        String token =
            simpleJwtSpecialist.generate(userPassword.getUser(), scopeList);

        Jwt jwtModel = new Jwt();
        jwtModel.setToken(token);

        return jwtModel;
    }

    @Override
    public TenantUserPassword login(String identifier, String password) {
        Optional<TenantUserPassword> userPassword;
        try {
            userPassword = userPasswordManager.findByUserEmail(identifier);
        } catch (UserPasswordNotFoundException exception) {
            throw new PasswordNotSetException();
        }

        if (userPassword.isEmpty()) {
            throw new TenantUserNotFoundException();
        }

        Boolean isVerified;
        try {
            isVerified =
                bCryptEncoder.verify(password,
                    userPassword.get().getPassword());
        } catch (NullPointerException nullPointerException) {
            throw new PasswordNotSetException();
        } catch (Exception exception) {
            throw new UnknownServerException(
                "Error occurred while verifying password.");
        }

        if (!isVerified) {
            throw new PasswordIncorrectException();
        }

        return userPassword.get();
    }

    private String buildScopeList(Integer userId) {
        List<TenantRole> roleModels = roleManager.getListByUserId(userId);

        StringBuilder scopeList = new StringBuilder();
        for (TenantRole roleModel : roleModels) {
            for (TenantPermission permissionModel : roleModel.getPermissions()) {
                if (scopeList.toString().isEmpty()) {
                    scopeList.append(permissionModel.getName()).append(".")
                        .append(permissionModel.getType());
                } else {
                    scopeList.append(",").append(permissionModel.getName())
                        .append(".")
                        .append(permissionModel.getType());
                }
            }
        }

        return scopeList.toString();
    }
}