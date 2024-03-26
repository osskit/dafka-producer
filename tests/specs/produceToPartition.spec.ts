import delay from 'delay';

import type {Orchestrator} from '../testcontainers/orchestrator.js';
import {start} from '../testcontainers/orchestrator.js';
import {consume} from '../services/consume.js';

const topic = 'my-topic';

describe('tests', () => {
    let orchestrator: Orchestrator;

    beforeEach(async () => {
        orchestrator = await start(
            {
                KAFKA_BROKER: 'kafka:9092',
                MAX_BLOCK_MS: '1000',
            },
            [topic]
        );
    }, 5 * 60 * 1000);

    afterEach(async () => {
        if (!orchestrator) {
            return;
        }
        await orchestrator.stop();
    });

    it('produce to partition', async () => {
        orchestrator.dafkaProducer.produce([
            {
                topic,
                partition: 2,
                key: 'thekey',
                value: {data: 'foo'},
            },
        ]);

        await delay(5000);

        await expect(consume(orchestrator.kafkaClient, topic)).resolves.toMatchSnapshot();
    });
});
