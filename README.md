**Welcome to the source code of Scoober game.**

Please follow the below instructions to run the game.

Pre requisites:
***User must have [Maven](https://maven.apache.org/) and [Docker](https://docs.docker.com/get-docker/) installed in his/her system.***

1) Download the source code from [Github](https://github.com/mistu4u/scoober-game).
2) Go to the root folder and run `mvn clean package` to build the jar file.
3) Next run the command `docker-compose build` to build the image
4) Run the command `docker-compose up -d` to bring up the containers
5) Since this is an interactive game, the user needs to login to the
container shell to play it.
6) Player 1 should log-in to the shell using `docker exec -it scoober-1 /bin/bash`
7) Player 2 should log-in to the shell using `docker exec -it scoober-2 /bin/bash`
8) Player 1 should execute the following command to start the game:
`java -Dserver.port=8080 -Dplayer.name="subir" -Dplayer.type=A -Dredis.topic.self=topica -Dredis.topic.client=topicb -Dredis.host=redis -jar scoober-0.0.1-SNAPSHOT.jar`
9) Similarly, player 2 needs to execute the following command to start the game.
`java -Dserver.port=8989 -Dplayer.name="mistu" -Dplayer.type=A -Dredis.topic.self=topicb -Dredis.topic.client=topica -Dredis.host=redis -jar scoober-0.0.1-SNAPSHOT.jar`
10) **Configurable parameters:** All the command line arguments are configurable, however only player name and playing mode should be configured, rest do not need any modification.
    To change the player name , set it `-Dplayer.name=<player-name>`
    To change the player type , set it `-Dplayer.type=A`
    A is automatic and M is manual.
11) While playing manually, system waits for 5 seconds by default for an input else it continues playing automatically