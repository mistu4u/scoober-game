package com.client;

import com.justeat.scoober.entity.Input;
import com.justeat.scoober.entity.PlayerType;
import com.justeat.scoober.service.ScooberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;
import java.util.Scanner;

@SpringBootApplication
@Slf4j
@ComponentScan({"com.justeat"})
public class ClientApplication implements CommandLineRunner {

    private ScooberService scooberService;
    private static String playerType;

    @Autowired
    public ClientApplication(ScooberService scooberService) {
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
        log.info("\nEnter the ip of the opponent or localhost if running locally: ");
        String ip = scanner.next();
        log.info("\nEnter the port of the server: ");
        String port = scanner.next();
        System.setProperty("player.name", username);
        System.setProperty("player.type", playerType);
        System.setProperty("server.url", "http://" + ip + ":" + port + "/rs");
        log.info("\nProperties set, bringing up the application");
        SpringApplication app = new SpringApplication(ClientApplication.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", selfPort));
        app.run(args);
    }

    @Override
    public void run(String... args) {
        start();
    }

    private void start() {
        log.info("\nChoose 1 of the following. 1) Initiate \n 2) Wait for the opponent's turn ->");
        Scanner scanner = new Scanner(System.in);
        if (scanner.nextInt() == 1) {
            log.info("Please provide the starting number ->");
            int init = scanner.nextInt();
            scooberService
                    .sendResponseToOpponent(Input.builder()
                            .input(init).playerType(playerType)
                            .build(), System.getProperty("server.url"));
        }
    }
}
