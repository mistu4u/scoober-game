package com.justeat.scoober.service;

import com.justeat.scoober.entity.Input;

import java.util.Optional;

public interface ScooberService {
    Input processOpponentInput(Input input);

    Optional<String> challengeOpponent(Input input);

    void startGame();

    void playAgain();

    void stopGame(Input input);
}
