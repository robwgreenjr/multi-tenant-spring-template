package template.authorization.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertEquals;

public class RoleDelegatorTest {
    private RoleDelegator roleDelegator;

    @BeforeEach
    void initUseCase() {
        roleDelegator = new RoleDelegatorImpl();
    }

    @Test
    public void givenGetResponse_whenCalled_shouldReturnReadScope() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI("/user");
        httpServletRequest.setMethod(HttpMethod.GET.toString());

        String actual = roleDelegator.buildScope(httpServletRequest);

        assertEquals("user.read", actual);
    }

    @Test
    public void givenPostResponse_whenCalled_shouldReturnWriteScope() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI("/user");
        httpServletRequest.setMethod(HttpMethod.POST.toString());

        String actual = roleDelegator.buildScope(httpServletRequest);

        assertEquals("user.write", actual);
    }

    @Test
    public void givenPutResponse_whenCalled_shouldReturnWriteScope() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI("/user");
        httpServletRequest.setMethod(HttpMethod.PUT.toString());

        String actual = roleDelegator.buildScope(httpServletRequest);

        assertEquals("user.write", actual);
    }

    @Test
    public void givenPatchResponse_whenCalled_shouldReturnWriteScope() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI("/user");
        httpServletRequest.setMethod(HttpMethod.PATCH.toString());

        String actual = roleDelegator.buildScope(httpServletRequest);

        assertEquals("user.write", actual);
    }

    @Test
    public void givenDeleteResponse_whenCalled_shouldReturnWriteScope() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI("/user");
        httpServletRequest.setMethod(HttpMethod.DELETE.toString());

        String actual = roleDelegator.buildScope(httpServletRequest);

        assertEquals("user.write", actual);
    }

    @Test
    public void givenNestedURL_whenCalled_shouldReturnScope() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI("/user/test/another/nested");
        httpServletRequest.setMethod(HttpMethod.GET.toString());

        String actual = roleDelegator.buildScope(httpServletRequest);

        assertEquals("user.read", actual);
    }
}
