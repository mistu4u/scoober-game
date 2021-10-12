package com.justeat.scoober.redis;

import com.justeat.scoober.entity.Input;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {


    private MessageSubscriber redisMessageSubscriber;

    @Autowired
    public RedisConfig(MessageSubscriber redisMessageSubscriber) {
        this.redisMessageSubscriber = redisMessageSubscriber;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean("selfTopic")
    ChannelTopic selfTopic() {
        return new ChannelTopic(System.getProperty("redis.topic.self"));
    }

    @Bean("clientTopic")
    ChannelTopic clientTopic() {
        return new ChannelTopic(System.getProperty("redis.topic.client"));
    }

    @Bean
    public RedisTemplate<String, Input> redisTemplate() {
        final RedisTemplate<String, Input> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Input.class));
        return template;
    }

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(redisMessageSubscriber);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), selfTopic());
        log.info("Listener registered for topic {}", selfTopic().getTopic());
        return container;
    }

    @Bean("publisher")
    @Primary
    MessagePublisher redisPublisher() {
        return new MessagePublisherImpl(redisTemplate(), clientTopic());
    }
}
