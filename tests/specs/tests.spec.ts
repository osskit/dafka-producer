import type {Orchestrator} from '../testcontainers/orchestrator.js';
import {start as startKafka} from '../testcontainers/orchestrator.js';
import {Consumer, KafkaMessage} from 'kafkajs';

describe('tests', () => {
    let orchestrator: Orchestrator;

    beforeAll(async () => {
        orchestrator = await startKafka();
    }, 1800000);

    afterAll(async () => {
        await orchestrator.stop();
    }, 1800000);

    const createTopic = async (topics: string[]) => {
        const admin = orchestrator.kafkaClient.admin();

        await admin.createTopics({topics: topics.map((topic) => ({topic, replicationFactor: 1}))});
    };

    it.each([
        {
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
        {
            value: {data: 'foo'},
        },
    ])('produce and consume', async (message) => {
        const topic = `topic-${Date.now()}`;
        await createTopic([topic]);

        const consumer = orchestrator.kafkaClient.consumer({groupId: 'test'});
        await consumer.connect();

        await consumer.subscribe({topic, fromBeginning: true});

        await orchestrator.produce([
            {
                topic,
                ...message,
            },
        ]);

        const consumedMessage = await new Promise<KafkaMessage>((resolve) => {
            consumer.run({
                eachMessage: async ({message}) => resolve(message),
            });
        });

        expect(JSON.parse(consumedMessage.value?.toString() ?? '{}')).toMatchSnapshot();
        expect(
            Object.fromEntries(Object.entries(consumedMessage.headers!).map(([key, value]) => [key, value?.toString()]))
        ).toMatchSnapshot();

        await consumer.disconnect();
    });
});
