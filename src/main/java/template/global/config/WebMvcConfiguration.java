package template.global.config;

import template.authentication.helpers.HttpHeaderParser;
import template.authentication.helpers.JwtSpecialist;
import template.global.services.StringEncoder;
import template.tenants.interceptors.MultiTenantInterceptor;
import template.tenants.services.TenantDatabaseManager;
import template.tenants.services.TenantManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final TenantManager tenantManager;
    private final TenantDatabaseManager tenantDatabaseManager;
    private final JwtSpecialist simpleJwtSpecialist;
    private final HttpHeaderParser simpleHttpHeaderParser;
    private final StringEncoder cryptoEncoder;
    private final Environment environment;

    public WebMvcConfiguration(TenantManager tenantManager,
                               TenantDatabaseManager tenantDatabaseManager,
                               JwtSpecialist simpleJwtSpecialist,
                               HttpHeaderParser simpleHttpHeaderParser,
                               @Qualifier("CryptoEncoder")
                               StringEncoder cryptoEncoder, Environment environment) {
        this.tenantManager = tenantManager;
        this.tenantDatabaseManager = tenantDatabaseManager;
        this.simpleJwtSpecialist = simpleJwtSpecialist;
        this.simpleHttpHeaderParser = simpleHttpHeaderParser;
        this.cryptoEncoder = cryptoEncoder;
        this.environment = environment;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MultiTenantInterceptor(tenantManager,
            tenantDatabaseManager, simpleJwtSpecialist,
            simpleHttpHeaderParser, cryptoEncoder, environment));
    }
}
