package com.ai.game.sbattle.data.dto;

import java.util.List;

/**
 * Created by netikras on 17.5.15.
 */
public class ShipDto {

    private String id;
    private String boardId;
    private boolean killed;
    private List<String> squareIds;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public List<String> getSquareIds() {
        return squareIds;
    }

    public void setSquareIds(List<String> squareIds) {
        this.squareIds = squareIds;
    }

    public boolean isKilled() {
        return killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ShipDto{" +
                "id='" + id + '\'' +
                ", boardId='" + boardId + '\'' +
                ", killed=" + killed +
                ", squareIds=" + squareIds +
                ", type='" + type + '\'' +
                '}';
    }
}
