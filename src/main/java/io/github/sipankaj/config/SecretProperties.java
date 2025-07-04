package io.github.sipankaj.config;

import io.github.sipankaj.model.AwsProperties;
import io.github.sipankaj.model.AzureProperties;
import io.github.sipankaj.model.GcpProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "secret")
@Data
public class SecretProperties {
    private String backend;
    private String vaultPath;
    private List<String> preloadKeys = new ArrayList<>();
    private AzureProperties azure = new AzureProperties();
    private AwsProperties aws = new AwsProperties();
    private GcpProperties gcp = new GcpProperties();

}