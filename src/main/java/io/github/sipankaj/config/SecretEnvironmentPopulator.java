package io.github.sipankaj.config;

import io.github.sipankaj.provider.interfaces.ISecretProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnBean(ISecretProvider.class)
@ConditionalOnProperty(prefix = "secret", name = "preload-enabled", havingValue = "true", matchIfMissing = false)
public class SecretEnvironmentPopulator {

    private final ConfigurableEnvironment environment;
    private final ISecretProvider secretProvider;
    private final SecretProperties properties;

    public SecretEnvironmentPopulator(ConfigurableEnvironment environment,
                                      ISecretProvider secretProvider,
                                      SecretProperties properties) {
        this.environment = environment;
        this.secretProvider = secretProvider;
        this.properties = properties;
        populate();
    }

    private void populate() {
        Map<String, Object> secrets = new HashMap<>();
        for (String key : properties.getPreloadKeys()) {
            String value = secretProvider.getSecret(key);
            if (value != null) {
                secrets.put(key, value);
            }
        }
        MapPropertySource propertySource = new MapPropertySource("secrets", secrets);
        environment.getPropertySources().addFirst(propertySource);
    }
}
