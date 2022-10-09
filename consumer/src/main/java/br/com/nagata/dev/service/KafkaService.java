package br.com.nagata.dev.service;

import br.com.nagata.dev.model.ApplicationLog;
import br.com.nagata.dev.model.dto.MessageRequestDTO;
import br.com.nagata.dev.repository.ApplicationLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaService {
  private final ApplicationLogRepository repository;
  private final ObjectMapper mapper;

  @Autowired
  public KafkaService(ApplicationLogRepository repository, ObjectMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public void saveLog(String id, String message) {
    repository.save(new ApplicationLog(id, jsonToObject(message)));
  }

  private MessageRequestDTO jsonToObject(String message) {
    try {
      return mapper.convertValue(
          mapper.readValue(message, JsonNode.class), MessageRequestDTO.class);
    } catch (JsonProcessingException e) {
      log.error("JsonProcessingException: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
