# cto-spring-webflux-training
CTO training for Spring WebFlux.

## Spring Cloud Stream - Kafka integration:

Main example to follow the theory.
It launches an application with a producer and a consumer, interchanging a SecretMessage object.
The SecretMessage holds a lowercase random string that it's injected in the apache Kafka and printed by the consumer in uppercase.

The application manages different configuration posibilities through the application.yml file:

```yaml
KafkaApplication:
  message:
    length: 10
  producer:
    enabled: false
    blockingTime: 10
  consumer:
    enabled: true
    withError: false
```

- message.length: it controls the size of the random lowercase string within SecretMessages objects.
- producer.enabled: it controls wether the producer it's or not created.
- producer.blockingTime: if producer is enabled but consumer not, it has to block the flux emitted, and the property controls in seconds this time.
- producer.consumer: it controls wether the consumer it's or not created.
- producer.withError: it controls wether the consumer generates exceptions when an "A" is included in the transformed message or not.
