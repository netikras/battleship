package com.ai.game.sbattle.data.dto;

/**
 * Created by netikras on 17.5.15.
 */
public class PlayerDto {

    private String id;
    private GameBoardDto board;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameBoardDto getBoard() {
        return board;
    }

    public void setBoard(GameBoardDto board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "PlayerDto{" +
                "id='" + id + '\'' +
                ", board=" + board +
                '}';
    }
}
