import type {Orchestrator} from '../testcontainers/orchestrator.js';
import {start as startKafka} from '../testcontainers/orchestrator.js';

describe('tests', () => {
    let orchestrator: Orchestrator;

    beforeAll(async () => {
        orchestrator = await startKafka({
            KAFKA_AUTO_CREATE_TOPICS_ENABLE: 'false',
        });
    }, 1800000);

    afterAll(async () => {
        await orchestrator.stop();
    }, 1800000);

    it('should throw if failed to produce', async () => {
        const topic = `topic-${Date.now()}`;

        expect(() =>
            orchestrator.produce([
                {
                    topic,
                    value: {data: 'foo'},
                },
            ])
        ).rejects.toMatchSnapshot();
    });
});
