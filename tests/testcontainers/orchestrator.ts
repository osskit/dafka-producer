import {Network, StartedNetwork} from 'testcontainers';
import {dafkaProducer} from './dafkaProducer.js';
import {kafka} from './kafka.js';

import {Kafka} from 'kafkajs';

export interface KafkaOrchestrator {
    stop: () => Promise<void>;
    startOrchestrator: (dafkaEnv: Record<string, string>) => Promise<Orchestrator>;
    kafkaClient: Kafka;
}

export interface Orchestrator {
    stop: () => Promise<void>;
    produce: (payload: any) => Promise<Response>;
}

export const start = async () => {
    const network = await new Network().start();

    const {client: kafkaClient, stop: stopKafka} = await kafka(network);

    return {
        kafkaClient,
        stop: async () => {
            await stopKafka();
            await network.stop();
        },
        startOrchestrator: async (dafkaEnv: Record<string, string>) => {
            return startOrchestratorInner(network, dafkaEnv);
        },
    };
};

const startOrchestratorInner = async (
    network: StartedNetwork,
    dafkaEnv: Record<string, string>
): Promise<Orchestrator> => {
    const [{produce: produce, stop: stopService}] = await Promise.all([dafkaProducer(network, dafkaEnv)]);

    return {
        async stop() {
            await Promise.all([stopService()]);
        },
        produce,
    };
};
