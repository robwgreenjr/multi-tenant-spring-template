package template.authentication.services;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationProcessor {
    void validate(HttpServletRequest request);
}