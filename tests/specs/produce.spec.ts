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

    it('produce', async () => {
        orchestrator.dafkaProducer.produce([
            {
                topic,
                key: 'thekey',
                value: {data: 'foo'},
                headers: {
                    'x-request-id': '123',
                    'x-b3-traceid': '456',
                    'x-b3-spanid': '789',
                    'x-b3-parentspanid': '101112',
                    'x-b3-sampled': '1',
                    'x-b3-flags': '1',
                    'x-ot-span-context': 'foo',
                },
            },
        ]);

        await delay(5000);

        await expect(consume(orchestrator.kafkaClient, topic)).resolves.toMatchSnapshot();
    });
});
