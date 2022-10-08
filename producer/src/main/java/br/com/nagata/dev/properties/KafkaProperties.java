package br.com.nagata.dev.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.backing-services.kafka")
public class KafkaProperties {
  private static final String DEV_KAFKA_PRODUCER_ID = "devKafkaProducer";
  private String bootstrapServers;
  private String devOperationTopic;

  public Map<String, Object> getProducerConfigs() {
    var configs = new HashMap<String, Object>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ProducerConfig.CLIENT_ID_CONFIG, DEV_KAFKA_PRODUCER_ID);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configs.put(ProducerConfig.ACKS_CONFIG, "all");
    configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, Boolean.TRUE);
    configs.put(ProducerConfig.LINGER_MS_CONFIG, 0);
    configs.putAll(saslConfigs());

    return configs;
  }

  private Map<String, Object> saslConfigs() {
    var configs = new HashMap<String, Object>();
    configs.put(SaslConfigs.SASL_MECHANISM, SaslConfigs.GSSAPI_MECHANISM);
    configs.put(SaslConfigs.SASL_KERBEROS_TICKET_RENEW_WINDOW_FACTOR, SaslConfigs.DEFAULT_KERBEROS_TICKET_RENEW_WINDOW_FACTOR);
    configs.put(SaslConfigs.SASL_KERBEROS_TICKET_RENEW_JITTER, SaslConfigs.DEFAULT_KERBEROS_TICKET_RENEW_JITTER);
    configs.put(SaslConfigs.SASL_KERBEROS_MIN_TIME_BEFORE_RELOGIN, SaslConfigs.DEFAULT_KERBEROS_MIN_TIME_BEFORE_RELOGIN);
    configs.put(SaslConfigs.SASL_KERBEROS_KINIT_CMD, SaslConfigs.DEFAULT_KERBEROS_KINIT_CMD);
    configs.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, "kafka");

    return configs;
  }
}
