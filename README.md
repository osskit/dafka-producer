<p align="center">
  <img width="300" height="300" src="https://user-images.githubusercontent.com/15312980/175078334-f284f44e-0366-4e24-8f09-5301b098ea64.svg"/>
 </p>
 
# dafka-producer
Dockerized kafka producer

## Overview
Dafka-producer is a dockerized Kafka producer used to abstract producing messages to a kafka topic.

### Motivation
Why use this over just a Kafka client?
* Abstracts away the messaging layer, could be replaced with RabbitMQ or any other producer.
* Separates configuration, everything that's related to Kafka is encapsulated in Dafka and not the service itself.
* When testing your service you only test your service's logic and not the messaging layer implementation details.

<img width="790" alt="image" src="https://user-images.githubusercontent.com/15312980/175814041-9991f7d5-830c-4e3f-9b2b-ad3e33228946.png">


## Usage & Examples

### docker-compose
```
version: '3.9'

services:
    producer:
        image: osskit/dafka-producer
        ports:
            - 6000:6000
        environment:
            - PORT=6000
            - KAFKA_BROKER=kafka:9092
        depends_on:
            - kafka
    # Generic Kafka Setup Containers
    zookeeper:
        image: wurstmeister/zookeeper
    kafka:
        image: wurstmeister/kafka:2.12-2.2.0
        ports:
            - '9092:9092'
        environment:
            - KAFKA_ADVERTISED_HOST_NAME=kafka
            - KAFKA_CREATE_TOPICS=foo:1:1
            - KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
        depends_on:
            - zookeeper
```
in joint with `dafka-consumer`:
```
version: '3.9'

services:
    producer:
        image: osskit/dafka-producer
        ports:
            - 6000:6000
        environment:
            - PORT=6000
            - KAFKA_BROKER=kafka:9092
        depends_on:
            - kafka
    consumer:
        image: osskit/dafka-consumer
        ports:
            - 4001:4001
        environment:
            - KAFKA_BROKER=kafka:9092
            - GROUP_ID=consumer_1
            - TARGET_BASE_URL=http://target:8080
            - TOPICS_ROUTES=foo:/consume
            - MONITORING_SERVER_PORT=4001
        depends_on:
             - kafka
    # Generic Kafka Setup Containers
    zookeeper:
        image: wurstmeister/zookeeper
    kafka:
        image: wurstmeister/kafka:2.12-2.2.0
        ports:
            - '9092:9092'
        environment:
            - KAFKA_ADVERTISED_HOST_NAME=kafka
            - KAFKA_CREATE_TOPICS=foo:1:1
            - KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
        depends_on:
            - zookeeper
```

## Parameters

Container images are configured using parameters passed at runtime.

| Parameter | Default Values | Description
| :----: | --- | ---- |
| `PORT=3000` | HTTP port to listen to incoming requests | | 
| `KAFKA_BROKER=https://kafka-broker.com` | URL for the Kafka Broker | | 
| `READINESS_TOPIC=readiness` | Producing to this topic will provide an healthcheck of the producer container | |
| `LINGER_TIME_MS=0` |  #optional | |
| `COMPRESSION_TYPE="none"` | #optional | |
| `USE_SASL_AUTH=false` | #optional | |
| `SASL_PASSWORD` | #optional | |
| `SASL_USERNAME` | #optional | |
| `TRUSTSTORE_FILE_PATH=null` | #optional | |
| `TRUSTSTORE_PASSWORD=true` | #optional | |
| `USE_PROMETHEUS=false` | #optional | |
| `PROMETHEUS_BUCKETS=0.003,0.03,0.1,0.3,1.5,10` | #optional | |

## License
MIT License
