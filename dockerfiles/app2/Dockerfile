FROM openjdk:11
RUN apt-get update && apt-get install -y iputils-ping
MAINTAINER adhikarisubir@gmail.com
COPY target/scoober-0.0.1-SNAPSHOT.jar scoober-0.0.1-SNAPSHOT.jar
COPY ./scripts/entrypoint.sh /
COPY ./scripts/foo.sh /
ENTRYPOINT ["/foo.sh"]