import type {Orchestrator} from '../testcontainers/orchestrator.js';
import {start} from '../testcontainers/orchestrator.js';

const topic = 'my-topic';

describe('tests', () => {
    let orchestrator: Orchestrator;

    beforeEach(async () => {
        orchestrator = await start(
            {
                KAFKA_BROKER: 'kafka:9092',
                MAX_BLOCK_MS: '1000',
                KAFKA_PROPERTY_MAX_REQUEST_SIZE: '1024',
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

    it('extra kafka properties', async () => {
        await expect(
            orchestrator.dafkaProducer.produce([
                {
                    topic,
                    value: {data: 'x'.repeat(2048)},
                },
            ])
        ).rejects.toThrow(/max\.request\.size/);
    });
});
