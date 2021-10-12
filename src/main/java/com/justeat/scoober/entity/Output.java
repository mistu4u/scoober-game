package com.justeat.scoober.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Output {
    int result;
    int added;
    String playerType;
    boolean winner;
}
