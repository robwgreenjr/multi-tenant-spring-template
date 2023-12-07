package template.authentication.listeners;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.events.publishers.TenantResetPasswordTokenEventPublisher;
import template.authentication.models.TenantResetPasswordToken;
import template.aws.services.SESSender;
import template.helpers.IntegrationTest;
import template.tenants.models.TenantUser;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TenantCreateResetPasswordTokenListenerTest
    extends IntegrationTest {
    @Autowired
    private TenantResetPasswordTokenEventPublisher
        resetPasswordTokenEventPublisher;

    @MockBean
    private SESSender sesSender;

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void whenUserCreateEvent_shouldSendEmail() {
        TenantUser user = new TenantUser();
        user.setEmail("test@gmail.com");
        user.setFirstName("Tester");
        TenantResetPasswordToken
            resetPasswordToken = new TenantResetPasswordToken();
        resetPasswordToken.setUser(user);

        resetPasswordTokenEventPublisher.publishResetPasswordTokenCreatedEvent(
            resetPasswordToken);

        verify(sesSender, times(1)).sendEmail(Mockito.any(),
            Mockito.any(String.class), Mockito.any(String.class),
            Mockito.any(String.class));
    }
}
