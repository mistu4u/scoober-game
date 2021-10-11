package com.justeat.scoober.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Input {
    @JsonProperty("player")
    public int playerNumber;
    @JsonProperty("input")
    public int input;
    @JsonProperty("add")
    public int add;
    @JsonProperty("playerType")
    public String playerType;
    @JsonProperty("isFirst")
    public boolean isFirst;
}
