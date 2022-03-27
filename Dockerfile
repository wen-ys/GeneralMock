FROM adoptopenjdk/openjdk11

MAINTAINER Wenys <tjln0608@gmail.com>

ARG JAR
ENV JAR_FILE_PATH=${JAR:-test}

CMD mkdir /service
CMD touch /service/routing_rule.yml
COPY ${JAR_FILE_PATH} /service/generalmock.jar


ENTRYPOINT java -jar -DinitRoutingSource='/service/routing_rule.yml' /service/generalmock.jar
