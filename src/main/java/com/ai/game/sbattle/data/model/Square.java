package com.ai.game.sbattle.data.model;

import com.ai.game.sbattle.utils.ModelTransform;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by netikras on 17.5.15.
 */
@Entity
@Table(name = "board_square")
public class Square {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @ModelTransform(dtoFieldName = "id", dtoUpdatable = false)
    private String id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id")
    @ModelTransform(dtoFieldName = "boardId", dtoUpdatable = false, dtoValueExtractField = "id")
    private GameBoard board;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "coord_id", nullable = false, updatable = false)
    @ModelTransform(dtoFieldName = "coordinates", dtoUpdatable = false)
    private Coordinates coordinates;

    @Column(name = "revealed", nullable = false)
    @ModelTransform(dtoFieldName = "revealed", dtoUpdatable = false)
    private boolean revealed;

    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "hosted_ship_id")
    @ModelTransform(dtoFieldName = "shipId", dtoUpdatable = false, dtoValueExtractField = "id")
    private Ship hostedShip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public Ship getHostedShip() {
        return hostedShip;
    }

    public void setHostedShip(Ship hostedShip) {
        this.hostedShip = hostedShip;
    }

    @Override
    public String toString() {
        return "Square{" +
                "id='" + id + '\'' +
                ", board=" + board +
                ", coordinates=" + coordinates +
                ", revealed=" + revealed +
                ", hostedShip=" + hostedShip +
                '}';
    }
}
