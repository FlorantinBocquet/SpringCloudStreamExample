package com.example.cloud.function.consumer;

import com.example.cloud.function.model.User;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * The cloud function 'consumeTopic' is here bound to a Bean method, inside a Configuration class.
 * The Bean method is a Spring Bean that return a {@link Consumer} object.
 * Do note the bean implementation is just an inlining of a Service implementation.
 */
@Configuration
public class ConsumerConfiguration {

  /**
   * This method create a {@link Consumer} object that is bind to the cloud function 'consumeTopic'.
   * <p>
   * The {@link Consumer} object is a lambda that take a {@link Message} object as input.
   *
   * @return a {@link Consumer} generated as implementation based on the lambda.
   * @throws Exception if something happens during the execution and an exception is thrown, the message will be sent
   *                   to the dlq associated to this topic.
   */
  @Bean
  public Consumer<Message<User>> consumeTopic() {
    // The lambda takes a Message object as input.
    return message -> {
      // The MessageHeaders is a map of headers.
      final MessageHeaders headers = message.getHeaders();

      // The payload is only cast at this point, so if the payload is not a User object, an exception will be thrown.
      final User value = message.getPayload();

      System.out.println("Received headers: " + headers);
      System.out.println("Received message: " + value);
    };
  }
}
