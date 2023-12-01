package template.authentication.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import template.authentication.helpers.JwtSpecialist;
import template.authentication.models.InternalUserPassword;
import template.authentication.models.Jwt;
import template.authorization.models.InternalRole;
import template.authorization.services.InternalRoleManager;
import template.global.services.StringEncoder;

import java.util.List;

@Service
public class SimpleUserLogin implements UserLoginHandler {
    private final UserPasswordManager userPasswordManager;
    private final JwtSpecialist simpleJwtSpecialist;
    private final StringEncoder bCryptEncoder;
    private final InternalRoleManager roleManager;

    public SimpleUserLogin(UserPasswordManager userPasswordManager,
                           JwtSpecialist simpleJwtSpecialist,
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

//        String scopeList = buildScopeList(userPassword.getUser().getId());
//        String token =
//            simpleJwtSpecialist.generate(userPassword.getUser(), scopeList);

        Jwt jwtModel = new Jwt();
//        jwtModel.setToken(token);

        return jwtModel;
    }

    @Override
    public InternalUserPassword login(String identifier, String password) {
        InternalUserPassword userPassword = new InternalUserPassword();
//        try {
//            userPassword = userPasswordManager.findByUserEmail(identifier);
//        } catch (UserPasswordNotFoundException exception) {
//            throw new PasswordNotSetException();
//        }

//        Boolean isVerified;
//        try {
//            isVerified =
//                bCryptEncoder.verify(password, userPassword.getPassword());
//        } catch (NullPointerException nullPointerException) {
//            throw new PasswordNotSetException();
//        } catch (Exception exception) {
//            throw new UnknownServerException(
//                "Error occurred while verifying password.");
//        }

//        if (!isVerified) {
//            throw new PasswordIncorrectException();
//        }

        return userPassword;
    }

    private String buildScopeList(Integer userId) {
        List<InternalRole> roleModels = roleManager.getListByUserId(userId);

//        StringBuilder scopeList = new StringBuilder();
        for (InternalRole roleModel : roleModels) {
//            for (InternalPermission permissionModel : roleModel.getPermissions()) {
//                if (scopeList.toString().isEmpty()) {
//                    scopeList.append(permissionModel.getName()).append(".")
//                        .append(permissionModel.getType());
//                } else {
//                    scopeList.append(",").append(permissionModel.getName())
//                        .append(".")
//                        .append(permissionModel.getType());
//                }
//            }
        }

        return "";
    }
}