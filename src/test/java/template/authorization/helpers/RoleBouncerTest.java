package template.authorization.helpers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import template.authorization.exceptions.NotAuthorizedException;
import template.authorization.models.InternalPermission;
import template.authorization.models.InternalRole;
import template.authorization.models.WhiteListProvider;
import template.authorization.services.InternalRoleManager;
import template.authorization.utilities.RoleDelegator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RoleBouncerTest {
    private final InternalRoleManager roleManager =
        Mockito.mock(InternalRoleManager.class);
    private final RoleDelegator roleDelegator =
        Mockito.mock(RoleDelegator.class);
    private final Environment environment = Mockito.mock(Environment.class);
    private RoleBouncer roleBouncer;

    @BeforeEach
    void initUseCase() {
        roleBouncer = new RoleBouncer(roleManager, roleDelegator, environment);
    }

    @Test
    public void givenIndexRouteCalled_whenAuthorizeCalled_shouldAllow() {
        MockHttpServletRequest httpServletRequest =
            new MockHttpServletRequest();
        httpServletRequest.setRequestURI("/");
        when(roleDelegator.buildScope(httpServletRequest)).thenReturn("");

        assertDoesNotThrow(() -> roleBouncer.authorize(httpServletRequest));
    }

    @Test
    public void givenWhiteListedRoute_whenAuthorizeCalled_shouldAllow() {
        MockHttpServletRequest httpServletRequest =
            new MockHttpServletRequest();
        httpServletRequest.setRequestURI("/test");
        when(roleDelegator.buildScope(httpServletRequest)).thenReturn(
            "test.read");
        MockedStatic<WhiteListProvider> whiteListProviderMockedStatic =
            Mockito.mockStatic(WhiteListProvider.class);
        whiteListProviderMockedStatic.when(WhiteListProvider::getWhiteList)
            .thenReturn(new String[]{"test"});

        assertEquals("test", WhiteListProvider.getWhiteList()[0]);
        assertDoesNotThrow(() -> roleBouncer.authorize(httpServletRequest));
    }

    @Test
    public void givenUserIdWithRole_whenAuthorizeCalled_shouldAllow() {
        MockHttpServletRequest httpServletRequest =
            new MockHttpServletRequest();
        httpServletRequest.setAttribute("user_id", 1);
        when(roleDelegator.buildScope(httpServletRequest)).thenReturn(
            "test.read");
        InternalPermission permissionModel = new InternalPermission();
        permissionModel.setName("test");
        permissionModel.setType("read");
        Set<InternalPermission> permissionModelSet = new HashSet<>();
        permissionModelSet.add(permissionModel);

        InternalRole roleModel = new InternalRole();
        roleModel.setPermissions(permissionModelSet);
        List<InternalRole> roleModelList = new ArrayList<>();
        roleModelList.add(roleModel);
        when(roleManager.getListByUserId(1)).thenReturn(roleModelList);

        assertDoesNotThrow(() -> roleBouncer.authorize(httpServletRequest));
    }

    @Test
    public void givenUserIdWithNoRole_whenAuthorizeCalledForUserData_shouldAllow() {
        MockHttpServletRequest httpServletRequest =
            new MockHttpServletRequest();
        httpServletRequest.setAttribute("user_id", 1);
        httpServletRequest.setRequestURI("/user/1");
        when(roleDelegator.buildScope(httpServletRequest)).thenReturn(
            "test.read");

        assertDoesNotThrow(() -> roleBouncer.authorize(httpServletRequest));
    }

    @Test
    public void givenNoAccess_whenAuthorizeCalled_shouldThrowException() {
        MockHttpServletRequest httpServletRequest =
            new MockHttpServletRequest();
        when(roleDelegator.buildScope(httpServletRequest)).thenReturn(
            "test.read");

        assertThrows(NotAuthorizedException.class,
            () -> roleBouncer.authorize(httpServletRequest));
    }
}
