FROM openjdk:8-jdk-alpine

LABEL maintainer="stoyangenchev41@gmail.com"

RUN mkdir -p /opt/service

EXPOSE 8080

ENV SERVICE_HOME=/opt/service

COPY start.sh $SERVICE_HOME/start.sh
COPY files/server-1.0-SNAPSHOT.jar $SERVICE_HOME/tictactoe.jar

RUN chmod +x $SERVICE_HOME/start.sh

WORKDIR $SERVICE_HOME
RUN ["chmod", "+x", "./start.sh"]
ENTRYPOINT ["./start.sh"]
