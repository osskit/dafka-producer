import {Network} from 'testcontainers';
import {ServiceClient, dafka} from './dafka.js';
import {kafka} from './kafka.js';
import { Kafka } from 'kafkajs';

export interface Orchestrator {
    kafkaClient: Kafka;
    dafkaProducer: ServiceClient
    stop: () => Promise<void>;
}
export const start = async (env: Record<string, string>, topics: string[]) => {
    const network = await new Network().start();

    const {client: kafkaClient, stop: stopKafka} = await kafka(network, topics);
    const {stop: stopDafka, client: dafkaProducer} = await dafka(network, env);

    return {
        kafkaClient,
        dafkaProducer,
        stop: async () => {
            await stopDafka();
            await stopKafka();
            await network.stop();
        },
    };
};
