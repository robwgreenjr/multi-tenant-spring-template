package template.database.configs;

//@Configuration
public class TenantDataSourceConfiguration {
//    private static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setDataSource(dataSource);
//        liquibase.setChangeLog(properties.getChangeLog());
//        liquibase.setContexts(properties.getContexts());
//        liquibase.setDefaultSchema(properties.getDefaultSchema());
//        liquibase.setDropFirst(properties.isDropFirst());
//        liquibase.setShouldRun(properties.isEnabled());
//        liquibase.setChangeLogParameters(properties.getParameters());
//        liquibase.setRollbackFile(properties.getRollbackFile());
//        return liquibase;
//    }
//
//    @Bean
//    public DataSource tenantDataSource() {
//        return tenantDataSourceProperties()
//            .initializeDataSourceBuilder()
//            .build();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.tenant")
//    public DataSourceProperties tenantDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "datasource.tenant.liquibase")
//    public LiquibaseProperties tenantLiquibaseProperties() {
//        return new LiquibaseProperties();
//    }
//
//    @Bean("liquibaseTenant")
//    public SpringLiquibase tenantLiquibase() {
//        return springLiquibase(tenantDataSource(), tenantLiquibaseProperties());
//    }
}
