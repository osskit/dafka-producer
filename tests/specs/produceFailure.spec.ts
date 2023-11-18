import type {Orchestrator} from '../testcontainers/orchestrator.js';
import {start} from '../testcontainers/orchestrator.js';

describe('tests', () => {
    let orchestrator: Orchestrator;

    beforeEach(async () => {
        orchestrator = await start(
            {
                KAFKA_BROKER: 'kafka:9092',
            },
            []
        );
    }, 5 * 60 * 1000);

    afterEach(async () => {
        if (!orchestrator) {
            return;
        }
        await orchestrator.stop();
    });

    it('produce failure', async () => {
        expect(() =>
            orchestrator.dafkaProducer.produce([
                {
                    topic: 'not exists',
                    value: {data: 'foo'},
                },
            ])
        ).rejects.toMatchSnapshot();
    });
});
