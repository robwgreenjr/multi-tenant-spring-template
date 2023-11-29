package template.authentication.helpers;

import org.springframework.stereotype.Service;

@Service
public class SimpleHttpHeaderParser implements HttpHeaderParser {
    @Override
    public String getBearerToken(String authorizationHeader) {
        String bearerToken = authorizationHeader;
        if (bearerToken == null) return "";

        String[] verifyFormat = bearerToken.split("Bearer ");
        if (verifyFormat.length != 2) return "";
        bearerToken = verifyFormat[1];

        return bearerToken;
    }
}
