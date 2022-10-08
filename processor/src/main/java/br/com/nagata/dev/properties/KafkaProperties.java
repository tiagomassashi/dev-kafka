package br.com.nagata.dev.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.backing-services.kafka")
public class KafkaProperties {
  private static final String DEV_KAFKA_PRODUCER_ID = "devKafkaProducer";
  private static final String DEV_KAFKA_CONSUMER_ID = "devKafkaConsumer1";
  private String bootstrapServers;
  private String devOperationTopic;
  private String devStatusTopic;

  public Map<String, Object> getProducerConfigs() {
    var configs = new HashMap<String, Object>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ProducerConfig.CLIENT_ID_CONFIG, DEV_KAFKA_PRODUCER_ID);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.ACKS_CONFIG, "all");
    configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, Boolean.TRUE);
    configs.put(ProducerConfig.LINGER_MS_CONFIG, 0);
    configs.putAll(saslConfigs());

    return configs;
  }

  public Map<String, Object> getConsumerConfigs() {
    var configs = new HashMap<String, Object>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, DEV_KAFKA_CONSUMER_ID);
    configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
    configs.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 10000000);
    configs.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 250000000);
    configs.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 10000);
    configs.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 50000000);
    configs.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
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
