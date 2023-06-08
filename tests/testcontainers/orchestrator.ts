import {Network} from 'testcontainers';
import {dafkaProducer} from './dafkaProducer.js';
import {kafka} from './kafka.js';

import {Kafka} from 'kafkajs';

export interface Orchestrator {
    stop: () => Promise<void>;
    produce: (payload: any) => Promise<Response>;
    kafkaClient: Kafka;
}

export const start = async () => {
    const network = await new Network().start();

    const [{client: kafkaClient, stop: stopKafka}, {produce: produce, stop: stopService}] = await Promise.all([
        kafka(network),
        dafkaProducer(network),
    ]);

    return {
        kafkaClient,
        stop: async () => {
            await stopService();
            await stopKafka();
            await network.stop();
        },
        produce,
    };
};
