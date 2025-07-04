package io.github.sipankaj.provider.implementation;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import io.github.sipankaj.provider.interfaces.ISecretProvider;


public class AwsSecretsManagerProvider implements ISecretProvider {

    private final AWSSecretsManager secretsManager;

    public AwsSecretsManagerProvider(AWSSecretsManager secretsManager) {
        this.secretsManager = secretsManager;
    }

    @Override
    public String getSecret(String key) {
        return secretsManager.getSecretValue(
                new GetSecretValueRequest().withSecretId(key)).getSecretString();
    }
}