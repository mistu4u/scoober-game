package com.justeat.scoober;

import com.justeat.scoober.entity.PlayerType;
import com.justeat.scoober.service.ScooberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.util.Collections;
import java.util.Scanner;

@SpringBootApplication
@Slf4j
@Profile("!test")
public class ScooBerApplication implements CommandLineRunner {
    private ScooberService scooberService;
    private static String playerType;

    @Autowired
    public ScooBerApplication(ScooberService scooberService) {
        this.scooberService = scooberService;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //Prompt to get player name
        log.info("\nEnter your name: ");
        String username = scanner.next();

        //Prompt to get player type
        log.info("\nEnter the player type A or M: ");
        playerType = scanner.next();
        playerType = playerType.equals("A") ? PlayerType.AUTOMATIC.getPlayerType() : PlayerType.MANUAL.getPlayerType();

        log.info("\nEnter the port number of this client application: ");
        int selfPort = scanner.nextInt();

        log.info("\nEnter the topic name for this client: -> ");
        String selfTopic = scanner.next();
        log.info("\nEnter the topic name for the other client: -> ");
        String clientTopic = scanner.next();

        System.setProperty("player.name", username);
        System.setProperty("player.type", playerType);
        System.setProperty("redis.topic.self", selfTopic);
        System.setProperty("redis.topic.client", clientTopic);
        log.info("\nProperties set, bringing up the application");
        SpringApplication app = new SpringApplication(ScooBerApplication.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", selfPort));
        app.run(args);
    }

    @Override
    public void run(String... args) {
        scooberService.startGame();
    }

}
