# Configuration file editing

in the [application.yml](../application.yml) file you can find the configuration for the kafka binder.

We will check each part of the configuration separately.

## Kafka connection

This part of the properties is used to configure the connection to the kafka broker.

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:29092
    properties:
      schema-registry:
        url: http://localhost:7999
      security-protocol: PLAINTEXT
```

## Cloud Function binding

This part of the properties is used to configure the binding between the function and the kafka topic.

```yaml
spring:
  cloud:
    function:
      definition: consumeTopic;consumeTopicAndProduceToTopic
```

The function bound is an element of code (a @Bean or a @Service) that will be executed when a message is received on the
topic.

## Consumer binding

This part of the properties is used to configure the binding between the function and the kafka topic, and the
associated dlq.
The message will be sent to the dlq when the associated function throw an exception.

Do note that the first part of the binding name must be the same as the function name.

```yaml
spring:
  cloud:
    function:
      definition: consumeTopic # function name
    # This part is used to configure the consumer
    stream:
      bindings:
        consumeTopic-in-0: # binding name
          destination: topic-consumer-test # topic name
          group: group-test # consumer group
      # This part is used to configure the dlq
      kafka:
        bindings:
          consumeTopic-in-0: # binding name
            consumer:
              enable-dlq: true
              dlq-name: topic-consumer-test-dlq # dlq topic name
```

## Producer binding

This part of the properties is used to configure the binding with the producer.

```yaml
spring:
  cloud:
    stream:
      bindings:
        produceToTopic-out-0: # binding name
          destination: topic-producer-test # topic name
```

## Function binding

A function allows to bind a Function to both a consumer (input) and a producer (output).

The same as above, but with both input and output bindings first part of the name must be the same as the function name.

```yaml
spring:
  cloud:
    function:
      definition: consumeTopicAndProduceToTopic # function name
    stream:
      bindings:
        consumeTopicAndProduceTopic-in-0: # consumer binding name
          destination: topic-function-consumer-test # consumer topic name
          group: group-test # consumer group
        consumeTopicAndProduceToTopic-out-0: # producer binding name
          destination: topic-function-producer-test # producer topic name
      kafka:
        bindings:
          consumeTopicAndProduceTopic-in-0: # consumer binding name
            consumer:
              enable-dlq: true
              dlq-name: topic-function-consumer-test-dlq # dlq consumer topic name
```

---

## Notes

This configuration above explain how to realise bindings with json (de)serialisation.

To realise bindings with avro (de)serialisation, you will need to add a little more properties.

The main change will be the need to define the key/value (de)serializers manually, and set the topic name strategy. 

You can find an commented example in [application-avro.yml](../templates/application-avro.yml)

you will also need to add the following dependencies :

```xml

<dependency>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro</artifactId>
    <version>1.11.1</version>
</dependency>

```

```xml

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.14.1</version>
</dependency>

 ```

---