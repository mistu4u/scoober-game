package com.justeat.scoober.redis;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justeat.scoober.entity.Input;
import com.justeat.scoober.service.ScooberService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MessageSubscriber implements MessageListener {
    @Autowired
    private ScooberService scooberService;
    private List<String> messageList = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public void onMessage(final Message message, final byte[] pattern) {
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        messageList.add(message.toString());
        log.info("Message received: " + message);
        Input input = scooberService
                .processOpponentInput(objectMapper.readValue(message.toString(), Input.class));
        if (!input.isWinner()) {
            scooberService.challengeOpponent(input);
        }
        else{
            log.info("Player {} won the game ", input.getPlayerName());
//            scooberService.playAgain();
        }
    }

}
