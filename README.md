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

    consumer:
        image: osskit/dafka-consumer:5.1
        ports:
            - 4001:4001
        environment:
            - KAFKA_BROKER=kafka:9092
            - GROUP_ID=consumer_1
            - TARGET_BASE_URL=http://target:8080
            - TOPICS_ROUTES=foo:/consume
            - MONITORING_SERVER_PORT=4001
```

## License
MIT License
