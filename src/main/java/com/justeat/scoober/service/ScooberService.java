package com.justeat.scoober.service;

import com.justeat.scoober.entity.Input;
import com.justeat.scoober.entity.Output;

import java.util.Optional;

public interface ScooberService {
    Output processOpponentInput(Input input);

    Optional<String> challengeOpponent(Input input, String uri);

    void startGame();

}
