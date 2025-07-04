package io.github.sipankaj.provider.interfaces;

public interface ISecretProvider {
    String getSecret(String key);
}
