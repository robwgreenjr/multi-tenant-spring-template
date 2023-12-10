package template.tenants.helpers;

import org.springframework.stereotype.Service;
import template.authorization.models.TenantPermission;
import template.authorization.models.TenantRole;
import template.authorization.services.TenantPermissionManager;
import template.authorization.services.TenantRoleManager;
import template.tenants.models.TenantUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TenantActivationImpl implements TenantActivation {
    private final TenantRoleManager tenantRoleManager;
    private final TenantPermissionManager tenantPermissionManager;

    public TenantActivationImpl(TenantRoleManager tenantRoleManager,
                                TenantPermissionManager tenantPermissionManager) {
        this.tenantRoleManager = tenantRoleManager;
        this.tenantPermissionManager = tenantPermissionManager;
    }

    @Override
    public void setInitialAuthorization(TenantUser tenantUser) {
        List<TenantPermission> tenantPermissionList = new ArrayList<>();
        TenantPermission userPage = new TenantPermission();
        userPage.setName("users");
        userPage.setType("page");
        userPage.setDescription("Allow page access to edit user data.");
        tenantPermissionList.add(userPage);

        TenantPermission userRead = new TenantPermission();
        userRead.setName("user");
        userRead.setType("read");
        userRead.setDescription("Allow access to all user read endpoints.");
        tenantPermissionList.add(userRead);

        TenantPermission userWrite = new TenantPermission();
        userWrite.setName("user");
        userWrite.setType("write");
        userWrite.setDescription("Allow access to all user write endpoints.");
        tenantPermissionList.add(userWrite);

        TenantPermission usersRead = new TenantPermission();
        usersRead.setName("users");
        usersRead.setType("read");
        usersRead.setDescription("Allow access to all users read endpoints.");
        tenantPermissionList.add(usersRead);

        TenantPermission usersWrite = new TenantPermission();
        usersWrite.setName("users");
        usersWrite.setType("write");
        usersWrite.setDescription("Allow access to all users write endpoints.");
        tenantPermissionList.add(usersWrite);

        TenantPermission authorizationPage = new TenantPermission();
        authorizationPage.setName("authorization");
        authorizationPage.setType("page");
        authorizationPage.setDescription(
            "Allow page access to edit authorization data.");
        tenantPermissionList.add(authorizationPage);

        TenantPermission authorizationRead = new TenantPermission();
        authorizationRead.setName("authorization");
        authorizationRead.setType("read");
        authorizationRead.setDescription(
            "Allow access to all authorization read endpoints.");
        tenantPermissionList.add(authorizationRead);

        TenantPermission authorizationWrite = new TenantPermission();
        authorizationWrite.setName("authorization");
        authorizationWrite.setType("write");
        authorizationWrite.setDescription(
            "Allow access to all authorization write endpoints.");
        tenantPermissionList.add(authorizationWrite);

        TenantPermission authenticationRead = new TenantPermission();
        authenticationRead.setName("authentication");
        authenticationRead.setType("read");
        authenticationRead.setDescription(
            "Allow access to all authentication read endpoints.");
        tenantPermissionList.add(authenticationRead);

        TenantPermission authenticationWrite = new TenantPermission();
        authenticationWrite.setName("authentication");
        authenticationWrite.setType("write");
        authenticationWrite.setDescription(
            "Allow access to all authentication write endpoints.");
        tenantPermissionList.add(authenticationWrite);

        List<TenantPermission> savedPermissions =
            tenantPermissionManager.createAll(tenantPermissionList);

        Set<TenantUser> tenantUsers = new HashSet<>();
        Set<TenantPermission> tenantPermissions =
            new HashSet<>(savedPermissions);

        tenantUsers.add(tenantUser);
        TenantRole tenantRole = new TenantRole();
        tenantRole.setName("TOP_LEVEL");
        tenantRole.setDescription("Has full access.");
        tenantRole.setUsers(tenantUsers);
        tenantRole.setPermissions(tenantPermissions);

        tenantRoleManager.create(tenantRole);
    }
}
