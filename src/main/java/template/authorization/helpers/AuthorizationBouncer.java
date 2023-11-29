package template.authorization.helpers;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthorizationBouncer {
    void authorize(HttpServletRequest request);
}