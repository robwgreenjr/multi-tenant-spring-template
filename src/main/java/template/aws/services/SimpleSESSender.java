package template.aws.services;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import template.aws.exceptions.EmailSenderException;
import template.aws.helpers.AWSCredentialBuilder;
import template.global.constants.GlobalVariable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SimpleSESSender implements SESSender {
    static final String CHARSET = "UTF-8";
    private final AWSCredentialBuilder awsCredentialBuilder;
    private final Environment env;

    public SimpleSESSender(AWSCredentialBuilder awsCredentialBuilder, Environment env) {
        this.awsCredentialBuilder = awsCredentialBuilder;
        this.env = env;
    }

    @Override
    public void sendEmail(String[] to, String subject, String html, String text) {
        String environment = env.getProperty(GlobalVariable.ENVIRONMENT);
        if (environment == null || environment.equalsIgnoreCase("local")) return;

        try {
            AWSCredentialsProvider credentialsProvider =
                awsCredentialBuilder.getCredentialsProvider();

            AmazonSimpleEmailService client =
                AmazonSimpleEmailServiceClientBuilder.standard()
                    .withCredentials(credentialsProvider)
                    .withRegion(Regions.US_EAST_1)
                    .build();

            SendEmailRequest request =
                new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
                    .withMessage(new Message().withBody(new Body().withHtml(
                                new Content().withCharset(CHARSET).withData(html))
                            .withText(new Content().withCharset(CHARSET).withData(text)))
                        .withSubject(
                            new Content().withCharset(CHARSET).withData(subject)))
                    // TODO: setup dynamic source
                    .withSource("templatedevelopment@gmail.com");

            client.sendEmail(request);
        } catch (Exception exception) {
            throw new EmailSenderException();
        }
    }
}
