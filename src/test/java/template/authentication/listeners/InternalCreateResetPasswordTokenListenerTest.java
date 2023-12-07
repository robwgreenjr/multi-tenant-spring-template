package template.authentication.listeners;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.events.publishers.InternalResetPasswordTokenEventPublisher;
import template.authentication.models.InternalResetPasswordToken;
import template.aws.services.SESSender;
import template.database.cli.Seeder;
import template.helpers.InternalIntegrationTest;
import template.internal.models.InternalUser;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InternalCreateResetPasswordTokenListenerTest
    extends InternalIntegrationTest {
    @Autowired
    private InternalResetPasswordTokenEventPublisher
        resetPasswordTokenEventPublisher;
    @MockBean
    private SESSender sesSender;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void whenUserCreateEvent_shouldSendEmail() {
        seeder.internalUserPassword(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");

        InternalUser user = new InternalUser();
        user.setId(Integer.parseInt(objectList.get(0).get("id").toString()));
        user.setEmail(objectList.get(0).get("email").toString());
        InternalResetPasswordToken resetPasswordToken =
            new InternalResetPasswordToken();
        resetPasswordToken.setUser(user);

        resetPasswordTokenEventPublisher.publishResetPasswordTokenCreatedEvent(
            resetPasswordToken);

        verify(sesSender, times(1)).sendEmail(Mockito.any(),
            Mockito.any(String.class), Mockito.any(String.class),
            Mockito.any(String.class));
    }
}
