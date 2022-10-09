package br.com.nagata.dev.service;

import br.com.nagata.dev.client.KafkaProducerClient;
import br.com.nagata.dev.model.dto.MessageRequestDTO;
import br.com.nagata.dev.model.enums.StatusEnum;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class KafkaService {
  private static final String PREFIX = "DEV";
  private final KafkaProducerClient kafkaProducerClient;

  @Autowired
  public KafkaService(KafkaProducerClient kafkaProducerClient) {
    this.kafkaProducerClient = kafkaProducerClient;
  }

  public void sendMessage(MessageRequestDTO request) {
    request.setStatus(StatusEnum.IN_PROGRESS);
    kafkaProducerClient.doPublishDevOperationTopic(
        generateId(), new ObjectMapper().convertValue(request, JsonNode.class).toString());
  }

  private String generateId() {
    return PREFIX + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS").format(LocalDateTime.now());
  }
}
