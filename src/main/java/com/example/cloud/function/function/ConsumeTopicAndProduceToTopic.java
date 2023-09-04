package com.example.cloud.function.function;

import com.example.cloud.function.model.UpdatedUser;
import com.example.cloud.function.model.User;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * The cloud function 'consumeTopicAndProduceToTopic' is bind to this Service class.
 * The service class is a Spring Bean that implements the {@link Function} interface.
 * <p>
 * This implementation take two generic types set to {@link Message}.
 * <p>
 */
@Service
public class ConsumeTopicAndProduceToTopic implements Function<Message<User>, Message<UpdatedUser>> {

  /**
   * When a message is received, it's this method that will get called to process the message.
   *
   * @param message the message received from the consumer topic
   * @return a message to be sent to the producer topic
   * @throws Exception if something happens during the execution and an exception is thrown, the message will be sent
   *                   to the dlq associated to this topic.
   */
  @Override
  public Message<UpdatedUser> apply(final Message<User> message) {
    // The MessageHeaders is a map of headers.
    final MessageHeaders headers = message.getHeaders();

    // The payload is only cast at this point, so if the payload is not a User object, an exception will be thrown.
    final User value = message.getPayload();

    System.out.println("Received headers: " + headers);
    System.out.println("Received message: " + value);

    // The headers are a map of headers containing various information.
    // Some keys might be reserved by Spring Cloud Stream.
    final Map<String, Object> newHeaders = Map.of(
        "date", LocalDateTime.now().toString()
    );

    // The payload to send to the topic.
    final UpdatedUser updatedUser = new UpdatedUser(
        value.firstname() + value.lastname(),
        "Password"
    );

    System.out.println("Sending headers: " + newHeaders);
    System.out.println("Sending message: " + updatedUser);

    // Create a message with the payload and the headers.
    return MessageBuilder
        .withPayload(updatedUser)
        .copyHeaders(newHeaders)
        .build();
  }
}
