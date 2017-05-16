package com.ai.game.sbattle.data.dto;

import java.util.Date;

/**
 * Created by netikras on 17.5.15.
 */
public class MatchDto {

    private String id;
    private Date createdOn;
    private PlayerDto playerA;
    private PlayerDto playerB;
    private String winnerId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public PlayerDto getPlayerA() {
        return playerA;
    }

    public void setPlayerA(PlayerDto playerA) {
        this.playerA = playerA;
    }

    public PlayerDto getPlayerB() {
        return playerB;
    }

    public void setPlayerB(PlayerDto playerB) {
        this.playerB = playerB;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }


    @Override
    public String toString() {
        return "MatchDto{" +
                "id='" + id + '\'' +
                ", createdOn=" + createdOn +
                ", playerA=" + playerA +
                ", playerB=" + playerB +
                ", winnerId='" + winnerId + '\'' +
                '}';
    }
}
