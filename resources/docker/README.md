# Docker

## docker-compose를 실행 시키는 법

```shell
cd scripts

/bin/sh api-start
```

## docker-compose를 실행시키기 위해 필요한 환경 변수

```text
// 공통
API_SPRING_PROFILES_ACTIVE=
DB_HOSTNAME=
DB_USERNAME=
DB_PASSWORD=
REDIS_HOST=
REDIS_PORT=
REDIS_PASSWORD=
RABBITMQ_HOST=
RABBITMQ_PORT=
RABBITMQ_USERNAME=
RABBITMQ_PASSWORD=
TOKEN_SECRETKEY=

LOGSTASH_URL=

// docker-compose-api-pinpoint 사용시
PINPOINT_AGENT_PATH=
PINPOINT_PROFILES=
PINPOINT_JAR=
```
