# API

## 목차

- [패키지 구조](#패키지-구조)
- [패키지 설명](#패키지-설명)
    - [config](#config)
    - [domain](#domain)
        - [domain/xxx](#domainxxx)
            - [domain/xxx/dao](#domainxxxdao)
            - [domain/xxx/service](#domainxxxservice)
            - [domain/xxx/usecase](#domainxxxusecase)
            - [domain/xxx/util](#domainxxxutil)
- [테스트](#테스트)
- [API 문서](#api-문서)

## 패키지 구조

```
api
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── zzuag
│   │   │           └── api
│   │   │               ├── config
│   │   │               ├── domain
│   │   │               ├── integration
│   │   │               ├── security
│   │   │               ├── web
│   │   │               ├── ApiApp.java
│   │   │               └── ...
```

## 패키지 설명

- `ApiApp.java`: Spring Boot Application 실행 클래스
- `config`: API 서버에서 사용하는 설정 클래스를 정의하기 위한 패키지
- `domain`: 도메인의 유즈케이스를 정의 및 구현하기 위한 패키지
- `integration`: 테스트 환경에서 필요한 설정 및 클래스를 위한 패키지
- `security`: 인증 및 권한 관련 클래스를 정의하기 위한 패키지
- `web`: API 서버의 컨트롤러 및 필터를 정의하기 위한 패키지

### config

config 패키지의 클래스를 통해 API 서버에서 어떠한 설정을 하고 있는지 한눈에 알 수 있도록 구성하는 것을 목표로합니다.

- `APIXXXConfig`/`APIXXXConfiguration`/`APIXXXConfigurer`: API 서버에서 사용하는 설정 클래스를 의미합니다.
- `APIXXXProperties`: API 서버에서 사용하는 설정 값을 정의하는 클래스를 의미합니다.
- 라이브러리 및 외부 모듈를 위한 설정이 필요한 경우 이를 위한 클래스를 생성합니다.
- 설정 클래스에서 정의한 빈의 이름의 경우 `ApiAppConfig#BEAN_NAME_PREFIX`를 활용한 설정 클래스별 prefix를 만들어 정의합니다.

### domain

도메인 패키지에서 도메인별 유즈케이스를 정의 및 구현하는 것을 목표로합니다.

- `common`: 도메인에서 공통으로 사용하는 클래스를 정의하기 위한 패키지
- `xxx`: 도메인별 유즈케이스를 정의 및 구현하기 위한 패키지

#### domain/xxx

특정 도메인의 유즈케이스를 정의 및 구현하기 위한 패키지입니다.

- `config`: 특정 도메인에서만 사용하는 설정 클래스를 정의하기 위한 패키지
- `dao`: 특정 도메인에서 사용하는 DAO 클래스를 정의하기 위한 패키지
- `data`: 특정 도메인에서 정의되는 데이터 클래스를 정의하기 위한 패키지
- `dto`: 외부에서 특정 도메인에 대한 요청 및 응답을 위한 DTO 클래스를 정의하기 위한 패키지
- `event`: 특정 도메인에서 발생하는 이벤트를 정의하기 위한 패키지
- `external`: 다른 도메인 또는 외부 모듈/시스템을 이용하기 위한 클래스를 정의하기 위한 패키지
- `model`: 특정 도메인에서 사용하는 모델 클래스를 정의하기 위한 패키지
- `service`: 특정 도메인에서 사용하는 서비스 클래스를 정의하기 위한 패키지
- `usecase`: 특정 도메인에서 사용하는 유즈케이스 클래스를 정의하기 위한 패키지
- `util`: 특정 도메인에서 사용하는 유틸리티 클래스를 정의하기 위한 패키지

##### domain/xxx/config

대부분의 빈은 `@Component` 어노테이션을 사용하여 정의합니다.

`domain/xxx/config`에서 정의해야하는 빈은 `@Profile`을 통해 `usecase-test`를 제외한 특정 프로필에서만 사용하는 빈을 정의해야하는 경우
사용합니다.

##### domain/xxx/dao

`domain/xxx/data`에서 정의한 엔티티를 사용하기 위한 DAO 클래스를 정의하기 위한 패키지입니다.

##### domain/xxx/service

서비스에는 3가지 타입의 클래스가 존재합니다.

- `XXXQuery`: 여러 유즈케이스에서 사용하는 단순 쿼리를 정의하기 위한 클래스
- `XXXCommand`: 여러 유즈케이스에서 사용하는 단순 커멘드를 정의하기 위한 클래스
- `XXXService`: 단순 커리, 커멘드가 아닌 복잡한 비즈니스 로직을 정의하기 위한 클래스

**주의 사항**
`XXXCommand`, `XXXService`는 트랜잭션을 사용하는 클래스입니다.

이때 새로운 트랜잭션이 필요한 경우 해당 클래스를 인터페이스로 분리한 이후 `NTX`가 접두로 붙는 구현 클래스를 만들어 사용합니다.

해당 클래스들의 네이밍 규칙은 다음과 같습니다.

`XXXOOOType`

- `XXX`: 클래스의 행동을 의미합니다. ex) Calculate, Get, Save
- `OOO`: 행동의 대상을 의미합니다. ex) TryCount, EmailAuthHistory, Member
- `Type`: 클래스의 타입을 의미합니다. ex) Query, Command, Service

##### domain/xxx/usecase

유즈케이스는 엔드포인트와 1:1로 매핑되는 클래스입니다.

데이터의 유효성을 제외한 비즈니스로직의 경우 모델을 만들어 처리합니다.

모델만을 통해 처리하지 못하는 비즈니스 로직의 경우 서비스를 만들어 처리합니다.

##### domain/xxx/util

유틸리티 클래스는 특정 도메인에서만 사용하는 유틸리티 클래스를 정의하기 위한 패키지입니다.

이때 유틸리티 클래스를 컴포넌트로 정의하려면 `@Component` 어노테이션을 사용하는 것이 아닌 `domain/xxx/config` 패키지에서 정의합니다.

## 테스트

**테스트 대상**

- `domain/xxx/usecase`: 유즈케이스 테스트
- `domain/xxx/service`: 서비스 테스트
- `domain/xxx/util`: 유틸리티 테스트
- `domain/xxx/data/persistence`: 엔티티 조회/저장 테스트
- `web`: 컨트롤러 테스트

이때 외부 서비스/모듈을 사용하는 경우 해당 클래스의 프로필을 `@Profile("!usecase-test")`로 설정하여 테스트에서는 외부 서비스/모듈을 사용하지 않도록 합니다.

대신 `UMockXXX` 클래스를 만들어 목 객체를 사용하여 테스트를 진행합니다.

## API 문서

API 문서의 경우 컨트롤러 테스트를 통해 자동으로 생성되는 문서를 사용합니다.

해당 문서를 확인하기 위해서는 아래의 테스크를 실행합니다.

```bash
./gradlew :api:openapi3
```

해당 테스크를 실행하면 `api/src/main/resources/static` 디렉토리에 `openapi3.json` 파일이 생성됩니다.
