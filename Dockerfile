FROM openjdk:11
MAINTAINER adhikarisubir@gmail.com
COPY target/scoober-0.0.1-SNAPSHOT.jar scoober-0.0.1-SNAPSHOT.jar
#COPY ./scripts/foo.sh /foo.sh
#COPY ./scripts/entrypoint.sh /
COPY ./scripts/foo.sh /
#USER root
#ENTRYPOINT ["bash","foo.sh"]
ENTRYPOINT ["/foo.sh"]