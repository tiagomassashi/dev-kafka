package br.com.nagata.dev.service;

import br.com.nagata.dev.client.KafkaProducerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
  private final KafkaProducerClient kafkaProducerClient;

  @Autowired
  public KafkaService(KafkaProducerClient kafkaProducerClient) {
    this.kafkaProducerClient = kafkaProducerClient;
  }

  public void sendMessage(String id, String message) {
    kafkaProducerClient.doPublishDevStatusTopic(id, message);
  }
}
