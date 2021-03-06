# Base alpine linux
FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY target/nlp-service-1.0-SNAPSHOT.jar ./nlp.jar
COPY nlpservice-config.yml .
RUN mkdir -p ./resources/frequencyLists
COPY resources/frequencyLists/bnc.frequencies ./resources/frequencyLists/
COPY english.properties .

CMD usr/bin/java -Xmx8g -jar nlp.jar server nlpservice-config.yml
