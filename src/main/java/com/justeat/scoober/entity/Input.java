package com.justeat.scoober.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class Input {
    @JsonProperty("playerName")
    public String playerName;
    @JsonProperty("input")
    public int input;
    @JsonProperty("add")
    public int add;
    @JsonProperty("winner")
    boolean isWinner;
}
