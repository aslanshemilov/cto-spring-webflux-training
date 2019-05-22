# Spring WebFlux integrations

Spring Cloud Stream - Kafka integration

## Main example to follow theory

It launches an application with a producer and a consumer, interchanging a SecretMessage object.
The SecretMessage holds a lowercase random string that it's injected in the apache Kafka and printed by the consumer in uppercase.

The application manages different configuration posibilities through the application.yml file:

```yaml
kafka-application:
  message:
    length: 10
  producer:
    enabled: false
    blocking-time: 10
  consumer:
    enabled: true
    with-error: true
```

- message.length: it controls the size of the random lowercase string within SecretMessages objects.
- producer.enabled: it controls wether the producer is or not created.
- producer.blocking-time: if producer is enabled but consumer not, it has to block the flux emitted, and the property controls in seconds this time.
- consumer.enabled: it controls wether the consumer is or not created.
- consumer.with-error: if enable consumer throws an exception when the message contains an "a". To test retry strategy.
