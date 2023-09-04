package com.example.cloud.function.producer;

import com.example.cloud.function.model.UpdatedUser;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Producer {
  // The StreamBridge is a Spring Cloud Stream component that allow to send a message to a topic.
  private final StreamBridge streamBridge;

  public Producer(final StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }

  /**
   * This endpoint is use as an example to send a message to a producer topic.
   */
  @PostMapping("/producer")
  public void callProducer() {
    // The headers are a map of headers containing various information.
    // Some keys might be reserved by Spring Cloud Stream.
    final Map<String, Object> newHeaders = Map.of(
        "date", LocalDateTime.now().toString()
    );

    // The payload to send to the topic.
    final UpdatedUser updatedUser = new UpdatedUser(
        "John Doe",
        "Password"
    );

    System.out.println("Sending headers: " + newHeaders);
    System.out.println("Sending message: " + updatedUser);

    // Create a message with the payload and the headers.
    final Message<UpdatedUser> message = MessageBuilder
        .withPayload(updatedUser)
        .copyHeaders(newHeaders)
        .build();

    // Send the message to the topic.
    // The binding name is the field in the configuration yml file under the 'bindings' field matching your producer
    // topic.
    streamBridge.send("produceToTopic-out-0", message);
  }
}
