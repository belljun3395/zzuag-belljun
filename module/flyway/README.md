# Flyway

## 사용법

### 1. 필요한 설정을 추가한다.

`FlywayPropertiesConfigurations`에 필요한 설정을 추가한다.

```java

@Profile("xxx")
@Bean(name = BASE_BEAN_NAME_PREFIX + "xxxProperties")
@ConfigurationProperties(prefix = PROPERTY_PREFIX + ".xxx")
public FlywayProperties flywayApiProperties() {
    return new FlywayProperties();
}
```

`resources/db/migration` 아래 xxx 폴더를 생성하고, 마이그레이션 파일을 추가한다.

`resources` 아래 `application-flyway-xxx.yml` 파일을 추가하고, 필요한 설정을 추가한다.

기본적인 설정은 아래와 같다.

```yaml
zzaug:
  flyway:
    xxx:
      locations: classpath:db/migration/xxx
      sql-migration-suffixes: sql
      baseline-on-migrate: true
      baseline-version: 0
```

### 2. 원하는 모듈에 flyway 모듈을 추가한다.

```groovy
dependencies {
    implementation project(':module:flyway')
}
```

### 3. 원하는 모듈에 flyway 설정을 추가한다.

```java
@Import(value = {FlywayConfig.class})
```

위의 코드를 통해 flyway 모듈에서 설정한 설정을 불러온다.

### 4. 원하는 모듈에 flyway yml 설정을 추가한다.

### 5. 원하는 모듈을 실행시 해당 모듈을 구분할 수 있는 profile(xxx)을 추가한다.
