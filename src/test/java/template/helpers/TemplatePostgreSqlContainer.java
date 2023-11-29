package template.helpers;


import org.testcontainers.containers.PostgreSQLContainer;

public class TemplatePostgreSqlContainer
        extends PostgreSQLContainer<TemplatePostgreSqlContainer> {
    private static final String IMAGE_VERSION = "postgres:15.0";
    private static TemplatePostgreSqlContainer container;

    private TemplatePostgreSqlContainer() {
        super(IMAGE_VERSION);
    }

    public static TemplatePostgreSqlContainer getInstance() {
        if (container == null) {
            container = new TemplatePostgreSqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();

        System.setProperty("DRIVER_CLASS_NAME", container.getDriverClassName());
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}