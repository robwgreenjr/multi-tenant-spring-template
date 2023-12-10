package template.authentication.services;

import com.zaxxer.hikari.HikariDataSource;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import template.authentication.helpers.HttpHeaderParser;
import template.authentication.helpers.JwtSpecialist;
import template.database.models.ApplicationDataSource;
import template.database.models.DatabaseConnectionContext;
import template.global.constants.EnvironmentVariable;
import template.global.constants.GlobalVariable;
import template.global.services.StringEncoder;
import template.tenants.models.Tenant;
import template.tenants.models.TenantDatabase;
import template.tenants.models.TenantUser;
import template.tenants.resolvers.TenantIdentifierResolver;
import template.tenants.services.TenantDatabaseManager;
import template.tenants.services.TenantManager;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

@Service("TenantJwtDecipher")
public class TenantJwtDecipher implements AuthenticationProcessor {
    private final JwtSpecialist<TenantUser> simpleJwtSpecialist;
    private final HttpHeaderParser simpleHttpHeaderParser;
    private final TenantManager tenantManager;
    private final TenantDatabaseManager tenantDatabaseManager;
    private final Environment env;
    private final StringEncoder cryptoEncoder;
    private final TenantIdentifierResolver currentTenant;

    public TenantJwtDecipher(
        @Qualifier("TenantJwtSpecialist")
        JwtSpecialist<TenantUser> simpleJwtSpecialist,
        HttpHeaderParser simpleHttpHeaderParser,
        TenantManager tenantManager,
        TenantDatabaseManager tenantDatabaseManager,
        Environment env,
        @Qualifier("CryptoEncoder")
        StringEncoder cryptoEncoder,
        TenantIdentifierResolver currentTenant) {
        this.simpleJwtSpecialist = simpleJwtSpecialist;
        this.simpleHttpHeaderParser = simpleHttpHeaderParser;
        this.tenantManager = tenantManager;
        this.tenantDatabaseManager = tenantDatabaseManager;
        this.env = env;
        this.cryptoEncoder = cryptoEncoder;
        this.currentTenant = currentTenant;
    }

    @Override
    public void validate(HttpServletRequest request) {
        currentTenant.resetTenant();

        String tenantIdHeader = request.getHeader("tenant-id");
        if (tenantIdHeader != null && !tenantIdHeader.isEmpty() &&
            !currentTenant.isTenantSet()) {
            currentTenant.setCurrentTenant(UUID.fromString(tenantIdHeader));
        }

        String bearerToken =
            simpleHttpHeaderParser.getBearerToken(
                request.getHeader("Authorization"));
        if (bearerToken == null || bearerToken.isEmpty()) return;

        Claims claim = null;
        try {
            claim = simpleJwtSpecialist.validate(bearerToken);
            Object userDetails = claim.get("userDetails");

            request.setAttribute("user_id",
                ((LinkedHashMap<?, ?>) userDetails).get("id"));
        } catch (Exception exception) {
            // If this fails we do nothing because a user may
            // be using another method of authentication
        }

        // Only for local dev, if tenant id has been set return early
        // We still need to check Authorization before returning
        if (currentTenant.isTenantSet()) {
            return;
        }

        /*
         *    Assign tenant database context
         */
        UUID tenantId = null;

        // For local development use tenant-id from header
        String environmentValue = env.getProperty(GlobalVariable.ENVIRONMENT);
        if (environmentValue != null && environmentValue.equalsIgnoreCase(
            EnvironmentVariable.LOCAL)) {
            if (tenantIdHeader != null) {
                tenantId = UUID.fromString(tenantIdHeader);
            }
        }

        // Use tenant id from jwt
        if (claim != null && claim.get("tenantId") != null &&
            !claim.get("tenantId").toString().isEmpty() &&
            !claim.get("tenantId").toString().contains("main")) {
            tenantId = UUID.fromString(claim.get("tenantId").toString());
        }

        Optional<Tenant> tenant;
        try {
            tenant = tenantManager.getById(tenantId);
        } catch (Exception exception) {
            return;
        }

        if (tenant.isEmpty()) {
            return;
        }

        currentTenant.setCurrentTenant(tenant.get().getId());

        Optional<TenantDatabase> tenantDatabase;
        try {
            tenantDatabase = tenantDatabaseManager.getByTenant(tenant.get());
        } catch (Exception exception) {
            return;
        }

        if (tenantDatabase.isEmpty()) {
            return;
        }

        ApplicationDataSource tenantDataSource = new ApplicationDataSource();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(
            env.getProperty(GlobalVariable.DATABASE_DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(tenantDatabase.get().getUrl());
        dataSource.setUsername(
            cryptoEncoder.decode(tenantDatabase.get().getUsername()));
        dataSource.setPassword(
            cryptoEncoder.decode(tenantDatabase.get().getPassword()));
        dataSource.setMinimumIdle(tenantDatabase.get().getMinimumIdle());
        dataSource.setMaximumPoolSize(
            tenantDatabase.get().getMaximumPoolSize());
        tenantDataSource.setDataSource(dataSource);

        DatabaseConnectionContext.setCurrentDatabaseConnection(
            tenantDataSource);
    }
}