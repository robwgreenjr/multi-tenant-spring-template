package template.authentication.listeners;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import template.authentication.events.InternalUserPasswordCreatedEvent;
import template.authentication.models.InternalUserPassword;
import template.authentication.services.UserPasswordManager;
import template.global.services.StringEncoder;

@Component
public class CreateInternalUserPasswordListener
    implements ApplicationListener<InternalUserPasswordCreatedEvent> {
    private final StringEncoder bCryptEncoder;
    private final UserPasswordManager<InternalUserPassword> userPasswordManager;

    public CreateInternalUserPasswordListener(
        @Qualifier("BCryptEncoder")
        StringEncoder bCryptEncoder,
        @Qualifier("InternalUserPasswordManager")
        UserPasswordManager<InternalUserPassword> userPasswordManager) {
        this.bCryptEncoder = bCryptEncoder;
        this.userPasswordManager = userPasswordManager;
    }

    @Override
    public void onApplicationEvent(InternalUserPasswordCreatedEvent event) {
        InternalUserPassword userPasswordModel = event.getUserPasswordModel();
        userPasswordModel.setPassword(
            bCryptEncoder.encode(userPasswordModel.getPassword()));

        userPasswordManager.updatePartial(userPasswordModel.getId(),
            userPasswordModel);
    }
}