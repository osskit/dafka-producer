FROM gradle:6-jdk11 AS build
WORKDIR /service
COPY . ./

RUN gradle clean --info --no-daemon
RUN gradle build --info --no-daemon

FROM openjdk:13-alpine AS release
WORKDIR /service

COPY --from=build /service/build/libs/*-all.jar /service/application.jar

ENTRYPOINT ["java", "-jar", "/service/application.jar"]