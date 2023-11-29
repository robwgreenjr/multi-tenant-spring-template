package template.authentication.helpers;

public interface HttpHeaderParser {
    String getBearerToken(String authorizationHeader);
}
