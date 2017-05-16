package com.ai.game.sbattle.data.dto;

/**
 * Created by netikras on 17.5.15.
 */
public class SquareDto {

    private String id;
    private String boardId;
    private CoordsDto coordinates;
    private boolean isRevealed;
    private String shipId;

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

    public CoordsDto getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordsDto coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    @Override
    public String toString() {
        return "SquareDto{" +
                "id='" + id + '\'' +
                ", boardId='" + boardId + '\'' +
                ", coordinates=" + coordinates +
                ", isRevealed=" + isRevealed +
                ", shipId='" + shipId + '\'' +
                '}';
    }
}
