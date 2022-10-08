package br.com.nagata.dev.client;

import br.com.nagata.dev.properties.KafkaProperties;
import com.fasterxml.jackson.databind.JsonNode;
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
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final KafkaProperties kafkaProperties;

  @Autowired
  public KafkaProducerClient(
      KafkaTemplate<String, Object> kafkaTemplate, KafkaProperties kafkaProperties) {
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaProperties = kafkaProperties;
  }

  public void doPublishDevOperationTopic(String id, JsonNode message) {
    ListenableFuture<SendResult<String, Object>> future =
        kafkaTemplate.send(kafkaProperties.getDevOperationTopic(), id, message);
    try {
      SendResult<String, Object> result = future.get(1, TimeUnit.MINUTES);
      log.info(
          "Message sent... Topic: '{}' Id: '{}' Message: '{}' registered to partition: '{}' at offset: '{}'",
          kafkaProperties.getDevOperationTopic(),
          id,
          message,
          result.getRecordMetadata().partition(),
          result.getRecordMetadata().offset());
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      future.cancel(Boolean.TRUE);
      log.error(
          "Error delivering message to broker. Topic: '{}' Id: '{}' Message: '{}' Error: '{}'",
          kafkaProperties.getDevOperationTopic(),
          id,
          message,
          e.getMessage());
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
