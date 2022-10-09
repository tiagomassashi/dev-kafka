package br.com.nagata.dev.client;

import br.com.nagata.dev.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumerClient {
  private final KafkaService kafkaService;

  @Autowired
  public KafkaConsumerClient(KafkaService kafkaService) {
    this.kafkaService = kafkaService;
  }

  @KafkaListener(topics = "${application.backing-services.kafka.dev-operation-topic}")
  public void operationListener(
      @Payload String payload,
      Acknowledgment acknowledgment,
      ConsumerRecord<String, Object> consumerRecord) {
    log.info(
        "Message received... Topic: 'dev-operation-topic' Id: '{}' Message: '{}'",
        consumerRecord.key(),
        payload);
    kafkaService.saveLog(consumerRecord.key(), payload);
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "${application.backing-services.kafka.dev-status-topic}")
  public void statusListener(
      @Payload String payload,
      Acknowledgment acknowledgment,
      ConsumerRecord<String, Object> consumerRecord) {
    log.info(
        "Message received... Topic: 'dev-status-topic' Id: '{}' Message: '{}'",
        consumerRecord.key(),
        payload);
    kafkaService.saveLog(consumerRecord.key(), payload);
    acknowledgment.acknowledge();
  }
}
