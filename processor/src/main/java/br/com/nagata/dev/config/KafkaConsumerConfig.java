package br.com.nagata.dev.config;

import br.com.nagata.dev.properties.KafkaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
  private final KafkaProperties kafkaProperties;

  @Autowired
  public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }

  @Bean
  public ConsumerFactory<String, Object> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(kafkaProperties.getConsumerConfigs());
  }

  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>>
      kafkaListenerContainerFactory(KafkaTemplate<?, ?> template) {
    ConcurrentKafkaListenerContainerFactory<String, Object> listener =
        new ConcurrentKafkaListenerContainerFactory<>();
    listener.setConsumerFactory(consumerFactory());
    listener.getContainerProperties().setIdleBetweenPolls(10000);
    listener.getContainerProperties().setMissingTopicsFatal(Boolean.FALSE);
    listener.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    listener.getContainerProperties().setSyncCommits(Boolean.TRUE);
    return listener;
  }
}
