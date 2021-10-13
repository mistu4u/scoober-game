#!/bin/sh
java -Dserver.port=8080 -Dplayer.name="subir" -Dplayer.type=A -Dredis.topic.self=topica -Dredis.topic.client=topicb -jar scoober-0.0.1-SNAPSHOT.jar