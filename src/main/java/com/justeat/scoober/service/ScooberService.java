package com.justeat.scoober.service;

import com.justeat.scoober.entity.Input;

import java.io.IOException;
import java.util.Optional;

/**
 * Interface for the service class
 */
public interface ScooberService {
    Input processOpponentInput(Input input) throws InterruptedException, IOException;

    Optional<String> challengeOpponent(Input input);

    void startGame();

    void stopGame(Input input);
}
