FROM gradle:jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle
WORKDIR /home/gradle
RUN gradle build

FROM balenalib/raspberry-pi-openjdk:8-stretch-run AS deployment

RUN apt-get update && apt-get install wiringpi

RUN mkdir /app
COPY --from=build /home/gradle/build/libs/*.jar /app/springboot-app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app/springboot-app.jar
