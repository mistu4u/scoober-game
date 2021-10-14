package com.justeat.scoober.entity;

/**
 * All the possible player types are maintained here
 */
public enum PlayerType {
    AUTOMATIC("A"),
    MANUAL("M");
    private String playerType;

    PlayerType(String playerType) {
        this.playerType = playerType;
    }

    public String getPlayerType() {
        return this.playerType;
    }

}
