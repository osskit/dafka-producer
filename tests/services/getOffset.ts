import {Kafka} from 'kafkajs';

export const getOffset = async (kafka: Kafka, groupId: string, topic: string) => {
    const admin = kafka.admin();
    await admin.connect();
    const metadata = await admin.fetchOffsets({groupId, topics: [topic]});
    admin.disconnect();
    return Number.parseInt(metadata[0]?.partitions[0]?.offset!);
};
