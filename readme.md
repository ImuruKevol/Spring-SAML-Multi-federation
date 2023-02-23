# Spring boot test project

## config

`src/main/resources/application.yml`

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://<URL>:<PORT>/<DB_NAME>?serverTimezone=UTC&characterEncoding=UTF-8
    username: <DB_USERNAME>
    password: <DB_PASSWORD>

mybatis:
  type-aliases-package: com.season.simpleweb.mybatis
  mapper-locations:
  - static/mapper/*.xml
```

## Change, Modify, Edit

### `simpleweb/src/main/resources/saml/update-certificate.sh`
- edit to your info

### `simpleweb/src/main/java/com/season/simpleweb/config/SecurityConfig.java`

- contextProvider
```java
cp.setServerName("<METADATA URL>"); // line 148
```

- keyManager
```java
String storePass = "password"; // line 210
String defaultKey = "testsp"; // line 212
```

- metadata
```java
providers.add(ssoExtendedMetadataProvider("https://<METADATA URL>")); // line 270
```

- metadataGenerator
```java
metadataGenerator.setEntityBaseURL("https://<METADATA URL>"); // line 279
metadataGenerator.setEntityId("https://<METADATA URL>/sp/spring"); // line 280
```

### `simpleweb/src/main/java/com/season/simpleweb/ctrl/SSOController.java`
```java
String url = "<METADATA-URL>"; // line 82
```

### `simpleweb/src/main/resources/templates/pages/discovery.html`
```html
<!-- line 23 -->
<form action="https://<METADATA URL>/saml/login?disco=true" method="get"> 
```
