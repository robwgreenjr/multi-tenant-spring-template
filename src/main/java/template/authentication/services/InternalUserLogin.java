package template.authentication.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import template.authentication.exceptions.PasswordIncorrectException;
import template.authentication.exceptions.PasswordNotSetException;
import template.authentication.exceptions.UserPasswordNotFoundException;
import template.authentication.helpers.JwtSpecialist;
import template.authentication.models.InternalUserPassword;
import template.authentication.models.Jwt;
import template.authorization.models.InternalPermission;
import template.authorization.models.InternalRole;
import template.authorization.services.InternalRoleManager;
import template.global.exceptions.UnknownServerException;
import template.global.services.StringEncoder;
import template.internal.models.InternalUser;

import java.util.List;
import java.util.Optional;

@Service("InternalUserLogin")
public class InternalUserLogin
    implements UserLoginHandler<InternalUserPassword> {
    private final UserPasswordManager<InternalUserPassword> userPasswordManager;
    private final JwtSpecialist<InternalUser> simpleJwtSpecialist;
    private final StringEncoder bCryptEncoder;
    private final InternalRoleManager roleManager;

    public InternalUserLogin(
        @Qualifier("InternalUserPasswordManager")
        UserPasswordManager<InternalUserPassword> userPasswordManager,
        @Qualifier("InternalJwtSpecialist")
        JwtSpecialist<InternalUser> simpleJwtSpecialist,
        @Qualifier("BCryptEncoder")
        StringEncoder bCryptEncoder,
        InternalRoleManager roleManager) {
        this.userPasswordManager = userPasswordManager;
        this.simpleJwtSpecialist = simpleJwtSpecialist;
        this.bCryptEncoder = bCryptEncoder;
        this.roleManager = roleManager;
    }

    @Override
    public Jwt jwtProvider(String identifier, String password) {
        InternalUserPassword userPassword = login(identifier, password);

        String scopeList = buildScopeList(userPassword.getUser().getId());
        String token =
            simpleJwtSpecialist.generate(userPassword.getUser(), scopeList);

        return new Jwt(token);
    }

    @Override
    public InternalUserPassword login(String identifier, String password) {
        Optional<InternalUserPassword> userPassword;
        try {
            userPassword = userPasswordManager.findByUserEmail(identifier);
        } catch (UserPasswordNotFoundException exception) {
            throw new PasswordNotSetException();
        }

        if (userPassword.isEmpty()) {
            throw new PasswordNotSetException();
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
        List<InternalRole> roleModels = roleManager.getListByUserId(userId);

        StringBuilder scopeList = new StringBuilder();
        for (InternalRole roleModel : roleModels) {
            for (InternalPermission permissionModel : roleModel.getPermissions()) {
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