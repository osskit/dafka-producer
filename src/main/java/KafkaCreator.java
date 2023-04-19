import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;

class KafkaCreator {

    public KafkaCreator() {}

    private Properties getAuthProperties() {
        var props = new Properties();
        props.put("bootstrap.servers", Config.KAFKA_BROKER);

        if (!Config.USE_SASL_AUTH) {
            return props;
        }

        props.put("security.protocol", "SASL_SSL");

        if (Config.TRUSTSTORE_PASSWORD != null) {
            props.put("ssl.truststore.location", Config.TRUSTSTORE_FILE_PATH);
            props.put("ssl.truststore.password", Config.TRUSTSTORE_PASSWORD);
        }

        props.put("sasl.mechanism", Config.SASL_MECHANISM.toUpperCase());
        props.put("ssl.endpoint.identification.algorithm", "");
        props.put(
            "sasl.jaas.config",
            String.format(
                "org.apache.kafka.common.security.%s required username=\"%s\" password=\"%s\";",
                getSaslMechanism(),
                Config.SASL_USERNAME,
                Config.SASL_PASSWORD
            )
        );

        return props;
    }

    private String getSaslMechanism() {
        switch (Config.SASL_MECHANISM.toUpperCase()) {
          case "PLAIN":
            return "plain.PlainLoginModule";
          case "SCRAM-SHA-512":
            return "scram.ScramLoginModule";
        }
        return "";
    }

    public KafkaProducer<String, String> createProducer() {
        Properties props = getAuthProperties();

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("linger.ms", Config.LINGER_TIME_MS);
        if (Config.BATCH_SIZE != null) {
            props.put("batch.size", Config.BATCH_SIZE);
        }
        props.put("compression.type", Config.COMPRESSION_TYPE);
        

        return new KafkaProducer<>(props);
    }
}
