package template.database.configs;

//@Configuration
public class MainDataSourceConfiguration {
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
//    @Primary
//    public DataSource mainDataSource() {
//        return mainDataSourceProperties()
//            .initializeDataSourceBuilder()
//            .build();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.main")
//    public DataSourceProperties mainDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "datasource.main.liquibase")
//    public LiquibaseProperties mainLiquibaseProperties() {
//        return new LiquibaseProperties();
//    }
//
//    @Bean("liquibase")
//    public SpringLiquibase primaryLiquibase() {
//        return springLiquibase(mainDataSource(), mainLiquibaseProperties());
//    }
}
