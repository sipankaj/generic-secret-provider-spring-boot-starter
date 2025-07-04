package io.github.sipankaj.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretManagerServiceSettings;
import io.github.sipankaj.provider.implementation.AwsSecretsManagerProvider;
import io.github.sipankaj.provider.implementation.AzureKeyVaultProvider;
import io.github.sipankaj.provider.implementation.GcpSecretManagerProvider;
import io.github.sipankaj.provider.implementation.HashiCorpVaultProvider;
import io.github.sipankaj.provider.interfaces.ISecretProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.VaultTemplate;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties(SecretProperties.class)
public class SecretProviderAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "secret.backend", havingValue = "vault")
    public ISecretProvider vaultSecretProvider(VaultTemplate vaultTemplate, SecretProperties props) {
        return new HashiCorpVaultProvider(vaultTemplate, props.getVaultPath());
    }

    @Bean
    @ConditionalOnProperty(name = "secret.backend", havingValue = "aws")
    public AWSSecretsManager awsSecretsManager(SecretProperties props) {
        return AWSSecretsManagerClientBuilder.standard()
                .withRegion(props.getAws().getRegion())
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "secret.backend", havingValue = "aws")
    public ISecretProvider awsSecretProvider(AWSSecretsManager awsSecretsManager) {
        return new AwsSecretsManagerProvider(awsSecretsManager);
    }

    @Bean
    @ConditionalOnProperty(name = "secret.backend", havingValue = "azure")
    public SecretClient azureSecretClient(SecretProperties props) {
        return new SecretClientBuilder()
                .vaultUrl(props.getAzure().getVaultUrl())
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }

    @Bean
    @ConditionalOnProperty(name = "secret.backend", havingValue = "azure")
    public ISecretProvider azureSecretProvider(SecretClient azureSecretClient) {
        return new AzureKeyVaultProvider(azureSecretClient);
    }

    @Bean
    @ConditionalOnProperty(name = "secret.backend", havingValue = "gcp")
    public SecretManagerServiceClient gcpSecretManagerClient() throws IOException {
        return SecretManagerServiceClient.create(SecretManagerServiceSettings.newBuilder().build());
    }

    @Bean
    @ConditionalOnProperty(name = "secret.backend", havingValue = "gcp")
    public ISecretProvider gcpSecretProvider(SecretManagerServiceClient gcpClient, SecretProperties props) {
        return new GcpSecretManagerProvider(gcpClient, props.getGcp().getProjectId());
    }
}
