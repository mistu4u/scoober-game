package com.justeat.scoober.redis;

public interface MessagePublisher {
    void publish(final String message);
}
