package template.authentication.listeners;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import template.authentication.events.publishers.InternalResetPasswordTokenEventPublisher;
import template.aws.services.SESSender;
import template.helpers.IntegrationTest;

public class InternalCreateResetPasswordTokenListenerTest
    extends IntegrationTest {
    @Autowired
    private InternalResetPasswordTokenEventPublisher
        resetPasswordTokenEventPublisher;

    @MockBean
    private SESSender sesSender;

    @Test
    public void whenUserCreateEvent_shouldSendEmail() {
//        UserModel userModel = new UserModel();
//        userModel.setEmail("test@gmail.com");
//        userModel.setFirstName("Tester");
//        InternalResetPasswordToken
//            resetPasswordTokenModel = new InternalResetPasswordToken();
//        resetPasswordTokenModel.setUser(userModel);

//        resetPasswordTokenEventPublisher.publishResetPasswordTokenCreatedEvent(
//            resetPasswordTokenModel);
//
//        verify(sesSender, times(1)).sendEmail(Mockito.any(),
//            Mockito.any(String.class), Mockito.any(String.class),
//            Mockito.any(String.class));
    }
}
