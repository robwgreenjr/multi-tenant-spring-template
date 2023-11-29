package template.aws.helpers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import template.aws.constants.AWSVariable;
import template.global.exceptions.KnownServerException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class AWSCredentialBuilderImpl implements AWSCredentialBuilder {
    private final Environment env;

    public AWSCredentialBuilderImpl(Environment env) {
        this.env = env;
    }

    @Override
    public AWSCredentialsProvider getCredentialsProvider() {
        String accessKey = env.getProperty(AWSVariable.AWS_ACCESS_KEY);
        String accessSecret = env.getProperty(AWSVariable.AWS_SECRET_KEY);

        if (accessKey == null) {
            throw new KnownServerException("AWS access key isn't sets.");
        }

        if (accessSecret == null) {
            throw new KnownServerException("AWS secret key isn't sets.");
        }

        AWSCredentials awsCredentials =
                new BasicAWSCredentials(accessKey, accessSecret);

        return new AWSStaticCredentialsProvider(awsCredentials);
    }
}
