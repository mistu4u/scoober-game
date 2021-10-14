package com.justeat.scoober.redis;

import com.justeat.scoober.entity.Input;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
/**
 * Redis publisher implementation
 */
public class MessagePublisherImpl implements MessagePublisher {
    @Autowired
    private RedisTemplate<String, Input> redisTemplate;
    @Autowired
    @Qualifier("clientTopic")
    private ChannelTopic topic;

    public MessagePublisherImpl() {
    }

    public MessagePublisherImpl(final RedisTemplate<String, Input> redisTemplate, final ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(final Input message) {
        if (message.getInput()!= 0) {
            log.info("message {} published to topic {}", message, topic.getTopic());
            redisTemplate.convertAndSend(topic.getTopic(), message);
        }
    }

}