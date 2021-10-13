package com.justeat.scoober.redis;

import com.justeat.scoober.entity.Input;

public interface MessagePublisher {
    void publish(final Input message);
}
