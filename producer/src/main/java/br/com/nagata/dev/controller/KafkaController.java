package br.com.nagata.dev.controller;

import br.com.nagata.dev.model.MessageRequest;
import br.com.nagata.dev.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/operations")
public class KafkaController {
  private final KafkaService kafkaService;

  @Autowired
  public KafkaController(KafkaService kafkaService) {
    this.kafkaService = kafkaService;
  }

  @PostMapping
  public ResponseEntity<Void> asynchronousProcess(@RequestBody MessageRequest request) {
    kafkaService.sendMessage(request);
    return ResponseEntity.accepted().build();
  }
}
