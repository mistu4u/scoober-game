package com.justeat.scoober.service;

import com.justeat.scoober.config.ScooberClient;
import com.justeat.scoober.entity.Input;
import com.justeat.scoober.entity.Output;
import com.justeat.scoober.entity.PlayerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;
import java.util.Scanner;

@Service
@Slf4j
public class ScooberServiceImpl implements ScooberService {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);
    private final int ROOT = 3;
    private final int ONE = 1;
    private final String selfPlayerType = System.getProperty("player.type");
    private final String selfPlayerName = System.getProperty("player.name");
    @Autowired
    private ScooberClient scooberClient;

    @Override
    public Output processOpponentInput(Input input) {
        Output output = Output.builder().build();
        if (input.getPlayerType().equals(PlayerType.AUTOMATIC.getPlayerType())) {
            output = playAutomatic(input, output);
        } else if (input.getPlayerType().equals(PlayerType.MANUAL.getPlayerType())) {
            output = playManual(input);
        } else {
            throw new RuntimeException("Player type must be either A or M");
        }

        return output;
    }

    @Override
    public Optional<String> challengeOpponent(Input input, String uri) {
        String opponentUrl = System.getProperty("server.url");
        //Call the other service
//        final Output output = playManual(input);
//        log.info(String.valueOf(output));
        Optional<String> output = scooberClient.localApiClient().post()
                .uri(opponentUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(input), Input.class).retrieve()
                .bodyToMono(String.class).blockOptional();
        log.info("response from post = {}", output);
        return output;
    }

    @Override
    public void startGame() {
        log.info("\nChoose 1 of the following. 1) Initiate \n 2) Wait for the opponent's turn ->");
        Scanner scanner = new Scanner(System.in);
        if (scanner.nextInt() == 1) {
            log.info("Please provide the starting number ->");
            int init = scanner.nextInt();
            challengeOpponent(Input.builder()
                    .input(init).playerType(this.selfPlayerType).playerName(this.selfPlayerName)
                    .build(), System.getProperty("server.url"));
        }

    }


    private Output playManual(Input input) {
        log.info("Enter the number to be added (+1,-1 or 0) ->");
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        return Output.builder().added(input.getAdd()).result(input.getInput() + input.getAdd()).build();
    }

    private Output playAutomatic(Input input, Output output) {
        int inputReceived = input.getInput();
        if (inputReceived % ROOT == 0 && inputReceived / ROOT == ONE) {
            log.info("Player {} won", input.getPlayerName());
        }
        if (inputReceived % ROOT == 0) {
            output = Output.builder().result(inputReceived / ROOT)
                    .playerType(selfPlayerType).build();
        }
        if ((inputReceived + ONE) % ROOT == 0) {
            output = Output.builder().added(ONE).result(inputReceived + ONE)
                    .playerType(selfPlayerType).build();
        }
        if ((inputReceived - ONE) % ROOT == 0) {
            output = Output.builder().added(-ONE).result(inputReceived - ONE)
                    .playerType(selfPlayerType).build();
        }
        log.info(String.valueOf(output));
        return output;
    }
}
