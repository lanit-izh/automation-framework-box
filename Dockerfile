FROM maven:3.5.4-jdk-8 as build

COPY src src
COPY pom.xml .
RUN MAVEN_OPTS="-Xmx2048m" mvn clean compile package spring-boot:repackage -Dmaven.test.skip=true


FROM openjdk:8-jdk as production

WORKDIR /application

COPY src src
COPY --from=build /target/* /application/
COPY src/main/resources/* /application/

RUN chmod -R 777 /application

EXPOSE 8091

ENTRYPOINT ["java","-classpath","\"at-sample-1.0-SNAPSHOT-jar-with-dependencies.jar./:./*:./\"","application.Application"]

