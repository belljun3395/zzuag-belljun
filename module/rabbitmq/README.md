# RabbitMQ

## 사용법

### 1. 필요한 설정을 추가한다.

### 2. 원하는 모듈에 rabbitMQ 모듈을 추가한다.

```groovy
dependencies {
    implementation project(':module:rabbitmq')
}
```

### 3. 원하는 모듈에 rabbitMQ 설정을 추가한다.

rabbitMQ 설정은 현제 2가지가 존재한다.

1. `ZRabbiMQConfig`: rabbitMQ 모듈에서 설정한 모든 설정을 불러온다.
2. `ZRabbitMQUserConfig`: rabbitMQ 모듈에서 설정한 사용자 설정을 불러온다.

일반적인 API와 서버에서 rabbitMQ를 사용할 때는 `ZRabbitMQUserConfig`를 사용하면 된다.

```java
@Import(value = {ZRabbiMQConfig.class})
or
@Import(value = {ZRabbitMQUserConfig.class})
```

위의 코드를 통해 rabbitMQ 모듈에서 설정한 설정을 불러온다.

### 4. 원하는 모듈에 rabbitMQ yml 설정을 추가한다.
