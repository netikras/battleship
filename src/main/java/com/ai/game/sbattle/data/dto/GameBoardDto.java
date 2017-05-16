package com.ai.game.sbattle.data.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by netikras on 17.5.15.
 */
public class GameBoardDto {


    private String boardId;
    private Date createdOn;
    private String playerId;
    private List<String> squareIds;


    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public List<String> getSquareIds() {
        return squareIds;
    }

    public void setSquareIds(List<String> squareIds) {
        this.squareIds = squareIds;
    }

    @Override
    public String toString() {
        return "GameBoardDto{" +
                "boardId='" + boardId + '\'' +
                ", createdOn=" + createdOn +
                ", playerId='" + playerId + '\'' +
                ", squareIds=" + squareIds +
                '}';
    }
}
