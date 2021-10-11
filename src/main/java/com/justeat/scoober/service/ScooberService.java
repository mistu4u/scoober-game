package com.justeat.scoober.service;

import com.justeat.scoober.entity.Input;
import com.justeat.scoober.entity.Output;

import java.util.Optional;

public interface ScooberService {
    Output processOpponentInput(Input input);

    Optional<Output> sendResponseToOpponent(Input input, String uri);

}
