package template.database.models;

import java.util.HashMap;
import java.util.Map;

public class DatabaseConnectionContext {
    private static final ThreadLocal<ApplicationDataSource>
        currentDatabaseConnection = new InheritableThreadLocal<>();

    private static final Map<String, ApplicationDataSource>
        targetDataSources =
        new HashMap<>();

    public static ApplicationDataSource getCurrentDatabaseConnection() {
        return currentDatabaseConnection.get();
    }

    public static void setCurrentDatabaseConnection(
        ApplicationDataSource applicationDataSource) {
        currentDatabaseConnection.set(applicationDataSource);
    }

    public static void setCurrentDatabaseConnection(String databaseUrl) {
        currentDatabaseConnection.set(getTargetDataSources().get(databaseUrl));
    }

    public static void setToDefault() {
        currentDatabaseConnection.set(null);
    }

    public static Map<String, ApplicationDataSource> getTargetDataSources() {
        return targetDataSources;
    }
}