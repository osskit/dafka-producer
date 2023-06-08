import {StartedNetwork, Wait} from 'testcontainers';
import {KafkaContainer} from 'testcontainers';
import {Kafka, logLevel} from 'kafkajs';

export const kafka = async (network: StartedNetwork) => {
    const container = await new KafkaContainer('confluentinc/cp-kafka:7.2.2')
        .withNetwork(network)
        .withNetworkAliases('kafka')
        .withEnvironment({
            KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: 'BROKER:PLAINTEXT,PLAINTEXT:PLAINTEXT',
            KAFKA_INTER_BROKER_LISTENER_NAME: 'BROKER',
            KAFKA_BROKER_ID: '1',
            KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: '1',
            KAFKA_STATE_LOG_REPLICATION_FACTOR: '1',
            KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: '1',
            KAFKA_OFFSETS_TOPIC_NUM_PARTITIONS: '1',
            KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: '1',
            KAFKA_LOG_FLUSH_INTERVAL_MESSAGES: '9223372036854775807',
            KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: '0',
            KAFKA_CONFLUENT_SUPPORT_METRICS_ENABLE: 'false',
        })
        .withWaitStrategy(
            Wait.forLogMessage('Registered broker 1 at path /brokers/ids/1 with addresses: BROKER://kafka:9092')
        )
        .start();
    const client = new Kafka({
        logLevel: logLevel.NOTHING,
        brokers: [`${container.getHost()}:${container.getMappedPort(9093)}`],
    });

    return {
        stop: () => container.stop(),
        client,
    };
};
