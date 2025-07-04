# Generic Secret Provider Spring Boot Starter

A Spring Boot starter for unified access to secrets from:
- 🟢 **AWS Secrets Manager**
- 🔵 **Azure Key Vault**
- 🟡 **Google Cloud Secret Manager**
- 🟤 **HashiCorp Vault**

---

## 🔥 Features

- 🔐 Unified abstraction via `SecretProvider` interface
- 🧱 Built using SOLID principles
- ✅ Supports runtime selection of secret backend
- ☁️ Cloud-native integrations (IAM/Managed Identity/GCP SA)
- ⚙️ Secrets can optionally be injected into Spring `Environment`
- 📦 Ready for Spring Boot 3.x with `@AutoConfiguration`

---

## 🚀 Getting Started

### 1. Add Dependency

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.sipankaj</groupId>
    <artifactId>generic-secret-provider-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
---

## ⚙️ Configure `application.yml`

```yaml
secret:
  backend: aws  # Options: vault, aws, azure, gcp

  # Vault
  vault-path: secret/data/myapp

  # AWS
  aws:
    region: us-east-1

  # Azure
  azure:
    vault-url: https://<your-vault-name>.vault.azure.net/

  # GCP
  gcp:
    project-id: your-gcp-project-id

  # Optional: preload keys into Spring Environment
  preload-enabled: true
  preload-keys:
    - db.password
    - jwt.secret
```

✅ Set `preload-enabled: true` to make secrets available via `@Value("${db.password}")`.

---

## 🔧 Required Environment Variables

| Backend   | Environment Variables                                                |
| --------- | -------------------------------------------------------------------- |
| **Vault** | `VAULT_ADDR`, `VAULT_TOKEN`                                          |
| **AWS**   | `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `[AWS_SESSION_TOKEN]`  |
| **Azure** | `AZURE_CLIENT_ID`, `AZURE_TENANT_ID`, `AZURE_CLIENT_SECRET`          |
| **GCP**   | `GOOGLE_APPLICATION_CREDENTIALS` (path to service account JSON file) |

ℹ️ Cloud-native options like IAM roles or Managed Identities are supported when running on cloud infrastructure (e.g., EC2, GKE, AKS, etc.).

---

## 🧩 Usage

### Option 1: Inject `SecretProvider`

```java
@Service
public class MyService {

    private final SecretProvider secretProvider;

    public MyService(SecretProvider secretProvider) {
        this.secretProvider = secretProvider;
    }

    public void useSecrets() {
        String password = secretProvider.getSecret("db.password");
    }
}
```

---

### Option 2: Use `@Value` (with preload enabled)

```java
@Value("${db.password}")
private String dbPassword;
```

> ⚠️ Only works if `secret.preload-enabled=true` and `db.password` is listed under `preload-keys`.

---

## 🧠 Auto-Configuration

This library uses Spring Boot 3’s modern auto-configuration system.

📄 Registered in:

```
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

```text
io.github.sipankaj.config.SecretProviderAutoConfiguration
```

✅ This enables seamless plug-and-play when the starter is added to the classpath.


---

## 🛡️ Security Best Practices

* ⚠️ Never hardcode secrets or credentials in code or properties files
* ✅ Prefer IAM roles / Managed Identity / Application Default Credentials
* 🔒 Use encrypted secrets in CI/CD and runtime environments

---

## 📜 License

This project is licensed under the **MIT License**.

---

## 🙌 Contributions

* Feature requests and bug reports are welcome
* PRs to support more providers (e.g., Kubernetes Secrets, Vault Agent Injector, etc.) are encouraged

---

> Built with care by [@sipankaj](https://github.com/sipankaj) and powered by Spring Boot 💛