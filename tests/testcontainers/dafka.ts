import {StartedNetwork, Wait} from 'testcontainers';
import {GenericContainer} from 'testcontainers';
import fs from 'node:fs';
import {withThrow} from '@osskit/fetch-enhancers';

const enhanchedFetch = withThrow(fetch);

export interface ServiceClient {
    produce: (payload: any) => Promise<Response>;
}

export const dafka = async (
    network: StartedNetwork,
    env: Record<string, string>
) => {
    const container = await new GenericContainer('bazel/src:image')
        .withExposedPorts(3000)
        .withNetwork(network)
        .withEnvironment(env)
        .withWaitStrategy(Wait.forHttp('/ready', 8080).forStatusCode(200))
        .withStartupTimeout(parseInt(process.env.STARTUP_TIMEOUT ?? '60000'))
        .start();

    if (process.env.DEBUG) {
        try {
            fs.truncateSync('service.log', 0);
        } catch (err) {
            fs.writeFileSync('service.log', "", { flag: "wx" });
        }
        await container.logs().then((logs) => logs.pipe(fs.createWriteStream('service.log')));
    }

    const baseUrl = `http://localhost:${container.getMappedPort(8080)}`;

    return {
        stop: () => container.stop(),
        client: {
            produce: (payload: any) =>
            enhanchedFetch(`${baseUrl}/produce`, {
                method: 'post',
                body: JSON.stringify(payload),
                headers: {'content-type': 'application/json'},
            }),
        }
    };
};
