package com.justeat.scoober.service;

import com.justeat.scoober.config.ScooberClient;
import com.justeat.scoober.entity.Input;
import com.justeat.scoober.entity.Output;
import com.justeat.scoober.entity.PlayerType;
import com.justeat.scoober.redis.MessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Autowired
    @Qualifier("publisher")
    private MessagePublisher messagePublisher;

    @Override
    public Input processOpponentInput(Input input) {
        Output output = Output.builder().build();
        if (selfPlayerType.equals(PlayerType.AUTOMATIC.getPlayerType())) {
            output = playAutomatic(input, output);
        } else if (selfPlayerType.equals(PlayerType.MANUAL.getPlayerType())) {
            output = playManual(input);
        } else {
            throw new RuntimeException("Player type must be either A or M");
        }

        return outputToInputConverter(output);

    }

    private Input outputToInputConverter(Output output) {
        return Input.builder().input(output.getResult()).isWinner(output.isWinner())
                .playerName(System.getProperty("player.name")).build();
    }

    @Override
    public Optional<String> challengeOpponent(Input input) {
        messagePublisher.publish(input);
        return Optional.of("message sent");
    }

    /**
     * Function to start the game
     */
    @Override
    public void startGame() {
        log.info("\nChoose 1 of the following. 1) Initiate \n 2) Wait for the opponent's turn ->");
        Scanner scanner = new Scanner(System.in);
        if (scanner.nextInt() == 1) {
            log.info("Please provide the starting number ->");
            int init = scanner.nextInt();
            challengeOpponent(Input.builder()
                    .input(init).playerName(this.selfPlayerName)
                    .build());
        }

    }

    @Override
    public void playAgain() {
        log.info("Do you want to play again? [y/n] ->");
        Scanner sc = new Scanner(System.in);
        if (sc.next().equalsIgnoreCase("Y")) {
            startGame();
        }
    }

    @Override
    public void stopGame(Input input) {
        log.info("Since the winner is decided, stopped the game");
    }


    private Output playManual(Input input) {
        log.info("Enter the number to be added (+1,-1 or 0) ->");
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        return Output.builder()
                .added(input.getAdd()).result(input.getInput() + input.getAdd()).build();
    }

    private Output playAutomatic(Input input, Output output) {
        int inputReceived = input.getInput();
        if (inputReceived % ROOT == 0 && inputReceived / ROOT == ONE) {
            log.info("Player {} won ", System.getProperty("player.name"));
            log.info(String.valueOf(output));
            return Output.builder().result(-1).winner(true).build();
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
