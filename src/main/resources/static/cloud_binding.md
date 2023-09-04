# Code Binding and Producing

---

## Basic

The binding link a portion of code to a topic thanks to the application.yml.

The name of the definition must match the name of the function.

(c.f: [properties](configuration_properties.md))

```yaml
spring:
  cloud:
    function:
      definition: consumeTopic;consumeTopicAndProduceToTopic
```

---

## Binding

There is two ways to bind a function to a topic :

### Bean binding

The bean binding is available for **Java 8 +**.

You can find a documented example in [ConsumerConfiguration.java](../../java/com/example/cloud/function/consumer/ConsumerConfiguration.java)

```java
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class ConsumerConfiguration {
  @Bean
  public Function<Message<User>, Message<UpdatedUser>> consumeTopicAndProduceToTopic() {
    return message -> {
      // Your code here

      return Your_response;
    };
  }

  @Bean
  public Consumer<Message<User>> consumeTopic() {
    return message -> {
      // Your code here
    };
  }
}
```

### Service binding

If you bind a function with a service, the name of the binding will be that of the class, and the name in the yaml will start with a lowercase.

You can find a documented example in [ConsumeTopicAndProduceToTopic.java](../../java/com/example/cloud/function/function/ConsumeTopicAndProduceToTopic.java)

```java
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class ConsumeTopicAndProduceToTopic implements Function<Message<User>, Message<UpdatedUser>> {
  @Override
  public Message<UpdatedUser> apply(final Message<User> message) {
    // Your code here

    return Your_response;
  }
}

// Separated class

@Service
public class ConsumeTopic implements Consumer<Message<User>> {
  @Override
  public void accept(final Message<User> message) {
    // Your code here
  }
}
```

---

## Producing

Just producing a message work slightly differently.

You will need to inject a [StreamBridge](spring_cloud_stream_example.md#streambridge) in your class, and use it to send a message to the
topic.

example: [Producer.java](../../java/com/example/cloud/function/producer/Producer.java)

```java
import org.springframework.cloud.stream.function.StreamBridge;

public class Producer {
  private final StreamBridge streamBridge;

  public Producer(final StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }

  public void produce() {
    final Message<UpdatedUser> message = MessageBuilder.build();

    streamBridge.send("produceToTopic-out-0", message);
  }
}
```
