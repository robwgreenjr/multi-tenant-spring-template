package template.authorization.models;

public class WhiteListProvider {
    private static final String[] whiteList =
            {"authentication", "tenant/registration"};

    public static String[] getWhiteList() {
        return whiteList;
    }
}
