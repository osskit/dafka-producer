<p align="center">
  <img width="300" height="300" src="https://user-images.githubusercontent.com/15312980/175078334-f284f44e-0366-4e24-8f09-5301b098ea64.svg"/>
 </p>
 
# dafka-producer
Dockerized kafka producer

## Introduction

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

## License
MIT License
