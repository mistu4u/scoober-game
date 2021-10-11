package com.justeat.scoober.controller;

import com.justeat.scoober.entity.Input;
import com.justeat.scoober.entity.Output;
import com.justeat.scoober.service.ScooberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/rs")
public class ScooberController {
    @Autowired
    private ScooberService scooberService;

    @PostMapping
    public ResponseEntity<Void> processInput(@RequestBody Input input) {
//        Output output = scooberService.processOpponentInput(input);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

 /*   public ResponseEntity<Output> callOpponentApi(@RequestBody Input input) {
        final Optional<Output> clientResponse = scooberService.sendResponseToOpponent(input, );
        return ResponseEntity.ok(clientResponse.orElse(Output.builder().build()));
    }*/
}
