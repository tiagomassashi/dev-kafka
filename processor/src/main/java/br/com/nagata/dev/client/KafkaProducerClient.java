package br.com.nagata.dev.client;

import br.com.nagata.dev.properties.KafkaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class KafkaProducerClient {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final KafkaProperties kafkaProperties;

  @Autowired
  public KafkaProducerClient(
      KafkaTemplate<String, String> kafkaTemplate, KafkaProperties kafkaProperties) {
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaProperties = kafkaProperties;
  }

  public void doPublishDevStatusTopic(String id, String message) {
    ListenableFuture<SendResult<String, String>> future =
        kafkaTemplate.send(kafkaProperties.getDevStatusTopic(), id, message);
    try {
      SendResult<String, String> result = future.get(1, TimeUnit.MINUTES);
      log.info(
          "Message sent... Topic: '{}' Id: '{}' Message: '{}' registered to partition: '{}' at offset: '{}'",
          kafkaProperties.getDevStatusTopic(),
          id,
          message,
          result.getRecordMetadata().partition(),
          result.getRecordMetadata().offset());
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      future.cancel(Boolean.TRUE);
      log.error(
          "Error delivering message to broker. Topic: '{}' Id: '{}' Message: '{}' Error: '{}'",
          kafkaProperties.getDevStatusTopic(),
          id,
          message,
          e.getMessage());
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
