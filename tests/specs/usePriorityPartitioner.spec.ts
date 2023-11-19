import delay from 'delay';

import type {Orchestrator} from '../testcontainers/orchestrator.js';
import {start} from '../testcontainers/orchestrator.js';
import {sortBy} from 'lodash-es';

describe('tests', () => {
    let orchestrator: Orchestrator;

    beforeEach(async () => {
        orchestrator = await start(
            {
                KAFKA_BROKER: 'kafka:9092',
                MAX_BLOCK_MS: '1000',
                USE_PRIORITY_PARTITIONE: 'true',
            },
            ['my-topic-1', 'my-topic-2', 'my-topic-3'],
            6
        );
    }, 5 * 60 * 1000);

    afterEach(async () => {
        if (!orchestrator) {
            return;
        }
        await orchestrator.stop();
    });

    it('use priority partitioner', async () => {
        const admin = orchestrator.kafkaClient.admin();

        await Promise.all(
            ['a', 'b'].map((i) =>
                orchestrator.dafkaProducer.produce([
                    {
                        topic: 'my-topic-1',
                        key: `f2692d56-307b-485c-be8e-30384b76107a_someKey${i}`,
                        value: {data: 'foo'},
                    },
                ])
            )
        );
        await delay(5000);
        await expect(
            admin
                .fetchTopicOffsets('my-topic-1')
                .then((metadata) => sortBy(metadata.filter((x) => parseInt(x.offset) > 0).map((x) => x.partition)))
        ).resolves.toEqual([0, 1]);

        await Promise.all(
            ['a', 'b'].map((i) =>
                orchestrator.dafkaProducer.produce([
                    {
                        topic: 'my-topic-2',
                        key: `6014e175-04de-45d8-a4d6-53659d3fdc72_someKey${i}`,
                        value: {data: 'foo'},
                    },
                ])
            )
        );
        await delay(5000);
        await expect(
            admin
                .fetchTopicOffsets('my-topic-2')
                .then((metadata) => sortBy(metadata.filter((x) => parseInt(x.offset) > 0).map((x) => x.partition)))
        ).resolves.toEqual([2, 3]);

        await Promise.all(
            ['a', 'b'].map((i) =>
                orchestrator.dafkaProducer.produce([
                    {
                        topic: 'my-topic-3',
                        key: `9f46546a-3e1d-4e5d-bdf1-4b0d5838d702_someKey${i}`,
                        value: {data: 'foo'},
                    },
                ])
            )
        );
        await delay(5000);
        await expect(
            admin
                .fetchTopicOffsets('my-topic-3')
                .then((metadata) => sortBy(metadata.filter((x) => parseInt(x.offset) > 0).map((x) => x.partition)))
        ).resolves.toEqual([4, 5]);
    });
});
