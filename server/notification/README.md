# Notification

## 아키텍쳐

```plaintext

+-----------------+
|                 |
|      api        |     api 서버
|                 |
+-----------------+
       |
       |
       |
+-----------------+
|                 |
|     queue       |     메시지 큐
|                 |
+-----------------+
       |
       |
       |
+-----------------+
|                 |
|  notification   |     notification 서버(워커)
|                 |
+-----------------+

```

### 아키텍처 설명

- `api`: 사용자로부터 요청을 받아 메시지 큐에 메시지를 전송하는 서버
- `queue`: 메시지 큐
- `Notification`: 메시지 큐에서 메시지를 받아 사용자에게 알림을 전송하는 서버

현재 아키텍처 구성상 사용자의 요청을 받는 api 서버와 사용자에게 알림을 전송하는 notification 서버가 분리되어 있습니다.

이는 api 서버가 notification 서버에 자신이 원하는 요청을 전달해야함을 의미합니다.

이때 방법은 여러가지가 존재합니다.

- 통신을 이용하는 방법 ex) http 통신, gRPC
- 메시지 브로커를 이용하는 방법 ex) RabbitMQ
- 이벤트 브로커를 이용하는 방법 ex) Kafka

이 중에서 메시지 브로커를 이용하는 방법을 선택한 이유는 다음과 같습니다.

- 메시지 브로커를 이용하여 api 서버와 notification 서버의 결합도를 낮출 수 있습니다.
- 메시지 브로커를 이용하여 api 서버와 notification 서버의 확장성을 높일 수 있습니다.

추가로 이벤트가 아닌 메시지 브로커를 선택한 이유는 아래와 같습니다.

#### 이벤트와 메시지의 차이

제가 파악한 이벤트와 메시지의 가장 큰 차이는 수신자 여부입니다.

이벤트는 발행된 이벤트를 수신할 수 있는 수신자를 특정하지 않습니다.

그렇기에 이벤트의 내용은 단지 무언가 일어났다는 것이고 발행자는 이벤트를 수신자가 누구인지, 자신의 이벤트를 받을 준비가 되어있는지 알지 못한 상태로 발행합니다.

이때 이벤트는 일반적으로 `topics`, `topic/fanout exchanges`, `stream`, `notification service`를 활용한 발행 구독(
publish and subscribe) 채널을 사용합니다.

그리고 이러한 이벤트의 발행자 수신자 간의 계약은 발행자에 의해 소유되고 유지됩니다.

하지만 메시지는 이벤트와 달리 해당 메시지의 수신자가 명확합니다.

그렇기에 메시지의 내용 역시 수신자에게 전달한 내용이고 이벤트와 다르게 명확한 수신자가 지정되어 있습니다.

메시지에서 사용하는 채널은 일반적으로 `queues`, `direct exchanges`, `messaging services`를 활용한 점대점(point to point) 채널을
사용합니다.

그리고 이러한 메시지의 계약은 수신자에 의해 소유되고 유지됩니다.

#### RabbitMQ

RabbitMQ는 `AMQP 0-9-1`을 구현합니다.

AMQP에서 메시징 브로커는 발행자로부터 메시지를 받고 소비자에게 라우팅합니다.

라우팅에 대해 조금 더 자세히 살펴보면 메시지를 Exchange에 발행합니다.

이 Exchange는 Binding이라는 메시지를 Queue에 분배하는 규칙을 가지고 있고 규칙에 따라 메시지 사본을 분배합니다.

이 중 우리가 주목할 것은 메시지와 Queue를 연결하는 규칙인 Exchange입니다.

Exchange는 아래 4가지 교환 유형을 제공합니다.

- Direct exchange
- Fanout exchange
- Topic exchange
- Headers exchange

위 유형 중 `Fanout`과 `Topic`를 활용하면 이벤트 발행을 위한 채널을 만들 수 있다는 것을 "이벤트와 메시지의 차이"를 통해 알 수 있습니다.

Topic exchange는 특정 라우팅키로 보낸 메시지는 일치하는 바인딩키로 바인딩 된 모든 대기열에 전달된다는 점에서 Topic exchange는 직접 교환과 유사합니다.

하지만 바인딩 키에는 두 가지 중요한 특수한 경우가 있습니다.

- `*` : 정확히 한 단어를 대체할 수 있습니다.
- `#` : 0개 이상의 단어를 대체할 수 있습니다.

![python-five](https://www.rabbitmq.com/img/tutorials/python-five.png)

위의 예시 Topic exchange에서는 Q1은 `*.orange.*` 와 C1 그리고 Q2는 `*.*.rabbit`, `lazy.#`와 C2로 바인딩 되어 있다.

이러면 아래와 같은 결과가 나오게 됩니다.

| 라우팅 키                   | 전달되는 큐 | 전달되는 컨슈며 |
|-------------------------|--------|----------|
| quick.orange.rabbit     | Q1, Q2 | C1, C2   |
| lazy.orange.elephant    | Q1, Q2 | C1, C2   |
| quick.orange.fox        | Q1     | C1       |
| lazy.brown.fox          | Q2     | C2       |
| quick.brown.fox         | X      | X        |
| quick.orange.new.rabbit | X      | X        |
| lazy.orange.new.rabbit  | Q2     | C2       |

만약 위의 상황에서 Q1과 동일한 Q3가 추가된다면 어떻게 될까요?

Q1과 동일한 정보가 Q3에도 전달되고 따라서 C1과 동일한 정보를 C3로 받게 됩니다.

그렇다면 Q3에 C4를 추가하면 어떻게 될까요?

이 경우에는 Q3에 전달되는 정보가 C3, C4 모두에 전달되는 것이 아닌 하나에게만 전달된다.

#### 정리

위의 내용들을 통해 알 수 있는 것은 우리가 발행할 이벤트는 수신자가 아닌 **정말 그 이벤트 자체에 초점**이 맞추어져 있어야 한다는 것입니다.

추가로 RabbitMQ를 메시지 브로커로서 활용하기 위해서는 **Topic exchange 혹은 Fanout exchange 교환 유형을 이용**하여야 합니다.

동일한 이벤트를 서로 다른 컨슈머에서 받기 위해서는 **각각의 컨슈머에게 자신만의 큐가 필요하며 이를 이벤트를 발행하는 Topic과 Binding 하여야 합니다.**

**참고 링크**

- [Event vs Message](https://youtu.be/ziuzxxqjgtA?si=7O7aVD0ifQQTUOOQ)
- [Event-Driven Architecture](https://www.eventstore.com/event-driven-architecture)
- [AMQP 0-9-1 Model Explained](https://www.rabbitmq.com/tutorials/amqp-concepts.html)
- [RabbitMQ tutorial - Topics](https://www.rabbitmq.com/tutorials/tutorial-five-spring-amqp.html)
- [[NHN FORWARD 2020] RabbitMQ and Cloud Messaging Platform](https://www.youtube.com/watch?v=SmE_k8lqfRQ&t=755s)

## 패키지 구조

메시지 큐를 사용한다는 것을 고려하여 패키지 구조를 설계하였습니다.

```
notification
├── config
├── xxx
│   ├── config
│   ├── dto
│   ├── service
│   └── xxxListener.java
└── ...
```

### 패키지 설명

- `config`: notification 서버에서 사용하는 설정 클래스를 정의하기 위한 패키지
- `xxx`: notification 서버에서 처리할 수 있는 요청에 대한 최상위 패키지
- `xxx/config`: notification 서버에서 `xxx`에 대한 설정 클래스를 정의하기 위한 패키지
- `xxx/dto`: notification 서버에서 `xxx`에 대한 요청 및 응답을 위한 DTO 클래스를 정의하기 위한 패키지
- `xxx/service`: notification 서버에서 `xxx`에 대한 서비스 클래스를 정의하기 위한 패키지
- `xxx/xxxListener.java`: notification 서버에서 `xxx`에 대한 메시지를 수신하기 위한 리스너 클래스
