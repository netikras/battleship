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
    private List<SquareDto> squares;
    private List<ShipDto> ships;

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

    public List<SquareDto> getSquares() {
        return squares;
    }

    public void setSquares(List<SquareDto> squares) {
        this.squares = squares;
    }

    public List<ShipDto> getShips() {
        return ships;
    }

    public void setShips(List<ShipDto> ships) {
        this.ships = ships;
    }

    @Override
    public String toString() {
        return "GameBoardDto{" +
                "boardId='" + boardId + '\'' +
                ", createdOn=" + createdOn +
                ", playerId='" + playerId + '\'' +
                ", squares=" + squares +
                ", ships=" + ships +
                '}';
    }
}
