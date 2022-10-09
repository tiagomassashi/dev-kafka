package br.com.nagata.dev.service;

import br.com.nagata.dev.client.KafkaProducerClient;
import br.com.nagata.dev.exception.BusinessException;
import br.com.nagata.dev.model.dto.MessageRequestDTO;
import br.com.nagata.dev.model.enums.StatusEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class KafkaService {
  private final KafkaProducerClient kafkaProducerClient;
  private final ObjectMapper mapper;

  @Autowired
  public KafkaService(KafkaProducerClient kafkaProducerClient, ObjectMapper mapper) {
    this.kafkaProducerClient = kafkaProducerClient;
    this.mapper = mapper;
  }

  public void sendMessage(String id, String message) throws BusinessException {
    kafkaProducerClient.doPublishDevStatusTopic(id, processMessage(message));
  }

  private String processMessage(String message) throws BusinessException {
    try {
      MessageRequestDTO object =
          mapper.convertValue(mapper.readValue(message, JsonNode.class), MessageRequestDTO.class);
      object.setStatus(StatusEnum.SUCCESS);

      if (object.getOrderValue().compareTo(BigDecimal.ONE) < 0) {
        object.setStatus(StatusEnum.ERROR);
        object.setStatusDescription("Order value cannot be negative");
      }

      return mapper.convertValue(object, JsonNode.class).toString();
    } catch (JsonProcessingException e) {
      log.error("JsonProcessingException: {}", e.getMessage());
      throw new BusinessException(e.getMessage(), e.getCause());
    }
  }
}
