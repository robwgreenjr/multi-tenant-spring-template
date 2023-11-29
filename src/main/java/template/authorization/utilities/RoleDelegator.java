package template.authorization.utilities;

import jakarta.servlet.http.HttpServletRequest;

public interface RoleDelegator {
    String buildScope(HttpServletRequest request);
}