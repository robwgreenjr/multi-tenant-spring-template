package template.authorization.utilities;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class RoleDelegatorImpl implements RoleDelegator {
    @Override
    public String buildScope(HttpServletRequest request) {
        String[] splitUri = request.getRequestURI().split("\\/");
        if (splitUri.length == 1) return "";

        String scope = splitUri[1];

        if (request.getMethod().equals(HttpMethod.GET.toString())) {
            scope += ".read";
        } else {
            scope += ".write";
        }

        return scope;
    }
}