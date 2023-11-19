import delay from 'delay';

import type {Orchestrator} from '../testcontainers/orchestrator.js';
import {start} from '../testcontainers/orchestrator.js';
import {sortBy} from 'lodash-es';

const topic = 'my-topic';

describe('tests', () => {
    let orchestrator: Orchestrator;

    beforeEach(async () => {
        orchestrator = await start(
            {
                KAFKA_BROKER: 'kafka:9092',
                MAX_BLOCK_MS: '1000',
                USE_PRIORITY_PARTITIONER: 'false',
            },
            [topic],
            6
        );
    }, 5 * 60 * 1000);

    afterEach(async () => {
        if (!orchestrator) {
            return;
        }
        await orchestrator.stop();
    });

    it('use default partitioner', async () => {
        await Promise.all(
            [1, 2, 3, 4, 5, 6].map((i) =>
                orchestrator.dafkaProducer.produce([
                    {
                        topic,
                        key: `dd387004-202e-487d-9c2d-1f5d6fea914f_someKey${i}`,
                        value: {data: 'foo'},
                    },
                ])
            )
        );

        await delay(5000);

        const admin = orchestrator.kafkaClient.admin();
        const metadata = await admin.fetchTopicOffsets(topic);
        const partitions = metadata.filter((x) => parseInt(x.offset) > 0).map((x) => x.partition);
        expect(sortBy(partitions)).toEqual([0, 1, 2, 3, 4, 5]);
    });
});
