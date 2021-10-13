package com.justeat.scoober.service;

import com.justeat.scoober.entity.Input;
import com.justeat.scoober.entity.Output;
import com.justeat.scoober.entity.PlayerType;
import com.justeat.scoober.exception.ScooberException;
import com.justeat.scoober.redis.MessagePublisher;
import com.justeat.scoober.util.ScannerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

@Service
@Slf4j
public class ScooberServiceImpl implements ScooberService {
    Scanner userInput = ScannerUtil.getScanner();
    private static final int ROOT = 3;
    private static final int ONE = 1;
    private static final int ZERO = 0;
    private final String selfPlayerType = System.getProperty("player.type");
    private final String selfPlayerName = System.getProperty("player.name");

    @Autowired
    @Qualifier("publisher")
    private MessagePublisher messagePublisher;

    @Override
    public Input processOpponentInput(Input input) {
        Output output = Output.builder().build();
        if (input.isWinner()) {
            stopPlaying(input);
        }
        if (selfPlayerType.equals(PlayerType.AUTOMATIC.getPlayerType())) {
            output = playAutomatic(input, output);
        } else if (selfPlayerType.equals(PlayerType.MANUAL.getPlayerType())) {
            output = playManual(input, output);
        } else {
            throw new ScooberException("Player type must be either A or M");
        }

        return outputToInputConverter(output);

    }

    private void stopPlaying(Input input) {
        log.info("Opponent {} has won the game", input.getPlayerName());
    }

    private Input outputToInputConverter(Output output) {
        log.info("output {} being converted to input ", output);
        return Input.builder().input(output.getResult()).isWinner(output.isWinner())
                .playerName(System.getProperty("player.name")).add(output.getAdded()).build();
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
        log.info("\nChoose 1 of the following. \na) Initiate \nb) Wait for the opponent's turn ->");
        Scanner scanner = ScannerUtil.getScanner();
        String firstInput = scanner.nextLine();
        if (firstInput.equalsIgnoreCase("a")) {
            log.info("Please provide the starting number ->");
            int init = Integer.parseInt(scanner.nextLine());
            challengeOpponent(Input.builder()
                    .input(init).playerName(this.selfPlayerName)
                    .build());
        } else {
            log.info("Waiting for the opponent's turn");
            //empty block, wait for the opponent's response
        }


    }



    @Override
    public void stopGame(Input input) {
        log.info("Sending message to opponent about the win");
        messagePublisher.publish(input);
        log.info("Since the winner is decided, stopped the game");
    }


    private Output playManual(Input input, Output output) {
        if (isWinner(output, input.getInput())) {
            return Output.builder().result(-1).winner(true).build();
        }
        log.info("Enter the number to be added (+1,-1 or 0) ->");
        String userInputstr = userInput.nextLine();
        int added = Integer.parseInt(userInputstr);
        return Output.builder()
                .added(added).result((input.getInput() + added) / ROOT).build();
    }

    private Output playAutomatic(Input input, Output output) {
        int inputReceived = input.getInput();
        if (inputReceived % ROOT == ZERO && inputReceived > ROOT) {
            return Output.builder().result(inputReceived / ROOT)
                    .playerType(selfPlayerType).build();
        }
        int afterOps = inputReceived + ONE;
        if (afterOps % ROOT == ZERO && inputReceived > ROOT) {
            if (isWinner(output, afterOps)) {
                return Output.builder().result(-1).winner(true).build();
            }
            return Output.builder().added(ONE).result(afterOps / ROOT)
                    .playerType(selfPlayerType).build();
        }
        afterOps = inputReceived - ONE;
        if (afterOps % ROOT == ZERO && inputReceived > ROOT) {
            if (isWinner(output, afterOps)) {
                return Output.builder().result(-1).winner(true).build();
            }
            return Output.builder().added(-ONE).result(afterOps / ROOT)
                    .playerType(selfPlayerType).build();
        }
        if (isWinner(output, inputReceived)) {
            return Output.builder().result(-1).winner(true).build();
        }
        return output;
    }

    private boolean isWinner(Output output, int inputReceived) {
        if (inputReceived <= ROOT) {
            if ((inputReceived + ONE) / ROOT == ONE || inputReceived / ROOT == ONE) {
                log.info("Player {} won ", System.getProperty("player.name"));
                log.info(String.valueOf(output));
                return true;
            }
        }
        return false;
    }
}
