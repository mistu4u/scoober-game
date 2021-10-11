package com.justeat.scoober.controller;

import com.justeat.scoober.entity.Input;
import com.justeat.scoober.redis.MessagePublisher;
import com.justeat.scoober.service.ScooberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rs")
public class ScooberController {
    @Autowired
    private ScooberService scooberService;
    @Autowired
    private MessagePublisher messagePublisher;

    @PostMapping
    public ResponseEntity<String> processInput(@RequestBody Input input) {
//        Output output = scooberService.processOpponentInput(input);
        messagePublisher.publish(input);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created");
    }

 /*   public ResponseEntity<Output> callOpponentApi(@RequestBody Input input) {
        final Optional<Output> clientResponse = scooberService.sendResponseToOpponent(input, );
        return ResponseEntity.ok(clientResponse.orElse(Output.builder().build()));
    }*/
}
