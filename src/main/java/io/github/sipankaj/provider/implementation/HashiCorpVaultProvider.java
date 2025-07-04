package io.github.sipankaj.provider.implementation;

import io.github.sipankaj.provider.interfaces.ISecretProvider;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

public class HashiCorpVaultProvider implements ISecretProvider {
    private final VaultTemplate vaultTemplate;

    private final String basePath;

    public HashiCorpVaultProvider(VaultTemplate vaultTemplate, String basePath) {
        this.vaultTemplate = vaultTemplate;
        this.basePath = basePath;
    }

    @Override
    public String getSecret(String key) {
        VaultResponse response = vaultTemplate.read(basePath + "/" + key);
        if (response != null && response.getData() != null) {
            return String.valueOf(response.getData().get("value"));
        }
        return null;
    }
}