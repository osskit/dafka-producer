import {StartedNetwork, StoppedTestContainer} from 'testcontainers';
import {GenericContainer, Wait} from 'testcontainers';
import {withThrow} from '@osskit/fetch-enhancers';

const startupTimeout = parseInt(process.env.STARTUP_TIMEOUT ?? '60000');

const enhanchedFetch = withThrow(fetch);

export interface ServiceContainer {
    stop: () => Promise<StoppedTestContainer>;
    produce: (payload: any) => Promise<Response>;
}

export const dafkaProducer = async (
    network: StartedNetwork,
    env: Record<string, string>
): Promise<ServiceContainer> => {
    const container = await new GenericContainer('bazel/src:image')
        .withExposedPorts(8080)
        .withNetwork(network)
        .withEnvironment({
            ...env,
            KAFKA_BROKER: 'kafka:9092',
        })
        .withStartupTimeout(startupTimeout)
        .withWaitStrategy(Wait.forHttp('/metrics', 8080).forStatusCode(200))

        .start();

    if (process.env.VERBOSE) {
        const logs = await container.logs();
        logs.pipe(process.stdout);
    }

    const baseUrl = `http://localhost:${container.getMappedPort(8080)}`;

    return {
        stop: () => container.stop(),
        produce: (payload: any) =>
            enhanchedFetch(`${baseUrl}/produce`, {
                method: 'post',
                body: JSON.stringify(payload),
                headers: {'content-type': 'application/json'},
            }),
    };
};
