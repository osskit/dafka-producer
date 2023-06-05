import type {Orchestrator, KafkaOrchestrator} from '../testcontainers/orchestrator.js';
import {start as startKafka} from '../testcontainers/orchestrator.js';
import {Consumer, KafkaMessage} from 'kafkajs';

describe('tests', () => {
    let kafkaOrchestrator: KafkaOrchestrator;
    let orchestrator: Orchestrator;
    let consumer: Consumer;

    beforeAll(async () => {
        kafkaOrchestrator = await startKafka();
    }, 1800000);

    afterAll(async () => {
        await kafkaOrchestrator.stop();
    }, 1800000);

    afterEach(async () => {
        if (consumer) {
            await consumer.disconnect();
        }
        await orchestrator.stop();
    });

    const start = async (topics: string[], producerSettings?: Record<string, string>) => {
        const admin = kafkaOrchestrator.kafkaClient.admin();

        await admin.createTopics({topics: topics.map((topic) => ({topic, replicationFactor: 1}))});

        orchestrator = await kafkaOrchestrator.startOrchestrator({
            ...producerSettings,
        });

        consumer = kafkaOrchestrator.kafkaClient.consumer({groupId: 'test'});
        await consumer.connect();
    };

    it('produce and consume', async () => {
        const topic = `topic-${Date.now()}`;
        await start([topic]);

        await consumer.subscribe({topic, fromBeginning: true});

        await orchestrator.produce([
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

        const consumedMessage = await new Promise<KafkaMessage>((resolve) => {
            consumer.run({
                eachMessage: async ({message}) => resolve(message),
            });
        });

        expect(JSON.parse(consumedMessage.value?.toString() ?? '{}')).toMatchSnapshot();
        expect(
            Object.fromEntries(Object.entries(consumedMessage.headers!).map(([key, value]) => [key, value?.toString()]))
        ).toMatchSnapshot();
    }, 1800000);
    /*
    beforeAll(async () => {
        await expect(checkReadiness(['foo', 'bar', 'retry', 'dead-letter', 'unexpected'])).resolves.toBeTruthy();
    });

    afterEach(async () => {
        await wireMock.global.resetAll();
    });

    it('readiness', async () => {
        const producer = await fetch('http://localhost:6000/ready');
        const consumer = await fetch('http://localhost:4001/ready');

        expect(producer.ok).toBeTruthy();
        expect(consumer.ok).toBeTruthy();
    });

    it('produce and consume', async () => {
        const target = await mockHttpTarget();

        await produce('http://localhost:6000/produce', [
            {
                topic: 'foo',
                key: 'thekey',
                value: {data: 'foo'},
            },
        ]);
        await delay(1000);

        expect(await getCall(target)).toMatchSnapshot({
            headers: {'x-record-timestamp': expect.any(String), 'x-record-offset': expect.any(String)},
        });
    });

    it('produce with headers', async () => {
        const target = await mockHttpTarget();

        await produce('http://localhost:6000/produce', [
            {
                topic: 'foo',
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
        await delay(1000);

        expect(await getCall(target)).toMatchSnapshot({
            headers: {'x-record-timestamp': expect.any(String), 'x-record-offset': expect.any(String)},
        });
    });

    it('validate request', async () => {
        const method = 'post';
        const producerUrl = 'http://localhost:6000/produce';
        const headers = {'Content-Type': 'application/json'};
        let response;

        response = await fetch(producerUrl, {
            method,
            body: JSON.stringify([{key: 'key', value: {data: 1}}]),
            headers,
        });
        expect(response.status).toBe(400);
        expect(await response.text()).toBe('topic is missing');

        response = await fetch(producerUrl, {
            method,
            body: JSON.stringify([{topic: 'bar', value: {data: 1}}]),
            headers,
        });
        expect(response.status).toBe(400);
        expect(await response.text()).toBe('key is missing');

        response = await fetch(producerUrl, {
            method,
            body: JSON.stringify([{topic: 'bar', key: 'key'}]),
            headers,
        });
        expect(response.status).toBe(400);
        expect(await response.text()).toBe('value is missing');
    });*/
});
/*
const produce = (url: string, batch: any[], headers?: object) =>
    fetch(url, {
        method: 'post',
        body: JSON.stringify(batch),
        headers: {'Content-Type': 'application/json', ...headers},
    });

const mockHttpTarget = () =>
    wireMock.mappings.createMapping({
        request: {
            method: 'POST',
            urlPathPattern: `/consume`,
        },
        response: {
            status: 200,
        },
    });

const getCall = async (mapping: StubMapping, callIndex = 0) => {
    const requestsDetails = await wireMock.requests.findRequests(mapping.request!);
    const request = requestsDetails.requests[callIndex];
    return {
        method: request.method,
        url: request.url,
        body: JSON.parse(request.body),
        headers: request.headers,
    };
};
*/
