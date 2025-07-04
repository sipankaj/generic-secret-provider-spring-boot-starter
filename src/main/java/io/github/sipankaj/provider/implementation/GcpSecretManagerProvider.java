package io.github.sipankaj.provider.implementation;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import io.github.sipankaj.provider.interfaces.ISecretProvider;

public class GcpSecretManagerProvider implements ISecretProvider {

    private final SecretManagerServiceClient secretManager;
    private final String projectId;

    public GcpSecretManagerProvider(SecretManagerServiceClient secretManager, String projectId) {
        this.secretManager = secretManager;
        this.projectId = projectId;
    }

    @Override
    public String getSecret(String key) {
        SecretVersionName secretVersionName = SecretVersionName.of(projectId, key, "latest");
        AccessSecretVersionResponse response = secretManager.accessSecretVersion(secretVersionName);
        return response.getPayload().getData().toStringUtf8();
    }
}
