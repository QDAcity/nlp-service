# Base alpine linux
FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY target/nlp-service-1.0-SNAPSHOT.jar ./nlp.jar
COPY nlpservice-config.yml .

CMD usr/bin/java -jar nlp.jar server nlpservice-config.yml
