package template.aws.helpers;

import com.amazonaws.auth.AWSCredentialsProvider;

public interface AWSCredentialBuilder {
    AWSCredentialsProvider getCredentialsProvider();
}
