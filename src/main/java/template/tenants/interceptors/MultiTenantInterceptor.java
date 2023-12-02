package template.tenants.interceptors;

import com.zaxxer.hikari.HikariDataSource;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerInterceptor;
import template.authentication.helpers.HttpHeaderParser;
import template.authentication.helpers.JwtSpecialist;
import template.authorization.models.WhiteListProvider;
import template.database.models.ApplicationDataSource;
import template.database.models.DatabaseConnectionContext;
import template.global.constants.EnvironmentVariable;
import template.global.constants.GlobalVariable;
import template.global.services.StringEncoder;
import template.internal.models.InternalUser;
import template.tenants.models.Tenant;
import template.tenants.models.TenantDatabase;
import template.tenants.services.TenantDatabaseManager;
import template.tenants.services.TenantManager;

import java.util.UUID;

public class MultiTenantInterceptor implements HandlerInterceptor {
    private final TenantManager tenantManager;
    private final TenantDatabaseManager tenantDatabaseManager;
    private final JwtSpecialist<InternalUser> simpleJwtSpecialist;
    private final HttpHeaderParser simpleHttpHeaderParser;
    private final StringEncoder cryptoEncoder;
    private final Environment environment;

    public MultiTenantInterceptor(TenantManager tenantManager,
                                  TenantDatabaseManager tenantDatabaseManager,
                                  @Qualifier("InternalJwtSpecialist")
                                  JwtSpecialist<InternalUser> simpleJwtSpecialist,
                                  HttpHeaderParser simpleHttpHeaderParser,
                                  @Qualifier("CryptoEncoder")
                                  StringEncoder cryptoEncoder,
                                  Environment environment) {
        this.tenantManager = tenantManager;
        this.tenantDatabaseManager = tenantDatabaseManager;
        this.simpleJwtSpecialist = simpleJwtSpecialist;
        this.simpleHttpHeaderParser = simpleHttpHeaderParser;
        this.cryptoEncoder = cryptoEncoder;
        this.environment = environment;
    }


    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        DatabaseConnectionContext.setCurrentDatabaseConnection(
            DatabaseConnectionContext.getTargetDataSources()
                .get(environment.getProperty(GlobalVariable.DATABASE_URL)));
        UUID tenantId = null;

        /*
         *  Use tenant-id header when on a white list page without jwt
         */
        for (String white : WhiteListProvider.getWhiteList()) {
            if (!request.getRequestURI().contains(white)) continue;

            if (request.getHeader("tenant-id") == null) return true;

            try {
                tenantId = UUID.fromString(request.getHeader("tenant-id"));
            } catch (Exception exception) {
                return true;
            }

            break;
        }

        // For local development use tenant-id from header
        String env = environment.getProperty(GlobalVariable.ENVIRONMENT);
        if (env != null && env.equalsIgnoreCase(
            EnvironmentVariable.LOCAL)) {
            String tenantIdHeader = request.getHeader("tenant-id");

            if (tenantIdHeader != null) {
                tenantId = UUID.fromString(tenantIdHeader);
            }
        }

        if (tenantId == null) {
            String bearerToken =
                simpleHttpHeaderParser.getBearerToken(
                    request.getHeader("Authorization"));
            if (bearerToken == null || bearerToken.isEmpty()) return true;

            Claims claim = null;
            try {
                claim = simpleJwtSpecialist.validate(bearerToken);
            } catch (Exception exception) {
                // If this fails we do nothing because a user may
                // be using another method of authentication
            }

            if (claim != null && claim.get("tenantId") != null &&
                !claim.get("tenantId").toString().isEmpty() &&
                !claim.get("tenantId").toString().contains("main")) {
                tenantId = UUID.fromString(claim.get("tenantId").toString());
            }
        }

        Tenant tenant;
        try {
            tenant = tenantManager.getById(tenantId);
        } catch (Exception exception) {
            return true;
        }

        if (tenant == null) {
            return true;
        }

        TenantDatabase tenantDatabase =
            tenantDatabaseManager.getByTenant(tenant);

        if (tenantDatabase == null) {
            return true;
        }

        ApplicationDataSource tenantDataSource = new ApplicationDataSource();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(environment.getProperty(
            GlobalVariable.DATABASE_DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(tenantDatabase.getUrl());
        dataSource.setUsername(
            cryptoEncoder.decode(tenantDatabase.getUsername()));
        dataSource.setPassword(
            cryptoEncoder.decode(tenantDatabase.getPassword()));
        dataSource.setMinimumIdle(tenantDatabase.getMinimumIdle());
        dataSource.setMaximumPoolSize(tenantDatabase.getMaximumPoolSize());
        tenantDataSource.setDataSource(dataSource);

        DatabaseConnectionContext.setCurrentDatabaseConnection(
            tenantDataSource);

        return true;
    }
}
