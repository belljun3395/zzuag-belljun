# ZZUAG

해당 프로젝트는 "2023 스마일게이트 서버개발캠프"에서 진행한 프로젝트입니다.

캠프 기간에 작성한 코드는 그 당시의 최선이었지만 끝나고 나서 보이는 부족한 점들을 보안하기 위해 다시 구현합니다.

## 프로젝트를 실행하는 방법

swagger 배포 주소: https://zzuag.shop/docs/swagger-ui/index.html

### 프로젝트 개발 환경을 구성합니다.

docker가 설치되어 있어야 합니다.

```shell
cd scripts/

/bin/sh recycle-develop-env
```

### 프로젝트를 실행합니다.

```shell
./gradlew :api:build -x test

cd api/build/libs

java -jar -Duser.timezone=Asia/Seoul api-0.0.1-SNAPSHOT.jar --spring.profiles.active=local,api
```

_편의를 위해 테스트를 제외한 빌드 작업을 수행하였습니다._

## 아키텍처(기본)

```
    user 요청
       |
       |
+-----------------+             +-----------------+
|                 |             |                 |
|    api 서버      |  ---------  |       db        |
|                 |             |                 |
+-----------------+             +-----------------+
       |
       |
       |                                                      로그 수집
+-----------------+                                     +-----------------+
|                 |                                     |                 |
|     queue       |                                     |      elk 서버    |
|     메시지 큐     |                                     |                 |
+-----------------+                                     +-----------------+
       |
       |
       |
+-----------------+
|                 |
|  notification   |
|      서버        |
+-----------------+
```

위의 아키텍처는 해당 프로젝트의 가장 기본적인 기능을 수행하기 위해 필요한 구성입니다.

추후 아키텍처 변경을 하며 변경에 따른 장단 및 성능을 알아볼 계획입니다.
