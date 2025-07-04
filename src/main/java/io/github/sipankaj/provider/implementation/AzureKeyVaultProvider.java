package io.github.sipankaj.provider.implementation;

import com.azure.security.keyvault.secrets.SecretClient;
import io.github.sipankaj.provider.interfaces.ISecretProvider;

public class AzureKeyVaultProvider implements ISecretProvider {
    private final SecretClient secretClient;

    public AzureKeyVaultProvider(SecretClient secretClient) {
        this.secretClient = secretClient;
    }

    @Override
    public String getSecret(String key) {
        return secretClient.getSecret(key).getValue();
    }
}