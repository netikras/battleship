package com.ai.game.sbattle.data.model;

import com.ai.game.sbattle.utils.ModelTransform;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created by netikras on 17.5.15.
 */
@Entity
@Table(name = "game_board")
public class GameBoard {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @ModelTransform(dtoFieldName = "boardId")
    private String id;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @ModelTransform(dtoFieldName = "createdOn")
    private Date createdOn;

    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedOn;

    @OneToMany(cascade = CascadeType.ALL,/* fetch = FetchType.EAGER, */mappedBy = "board")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ModelTransform(dtoFieldName = "ships")
    private List<Ship> ships;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "player_id", nullable = false, updatable = false)
    @ModelTransform(dtoFieldName = "playerId", dtoValueExtractField = "id")
    private Player boardOwner;

    @OneToMany(/*fetch = FetchType.EAGER, */cascade = CascadeType.ALL, mappedBy = "board")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ModelTransform(dtoFieldName = "squares")
    private List<Square> squares;

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

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
        if (ships != null) {
            for (Ship ship : ships) {
                ship.setBoard(this);
            }
        }
    }

    public Player getBoardOwner() {
        return boardOwner;
    }

    public void setBoardOwner(Player boardOwner) {
        this.boardOwner = boardOwner;
        if (boardOwner != null) {
            boardOwner.setBoard(this);
        }
    }

    public List<Square> getSquares() {
        return squares;
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
        if (squares != null) {
            for (Square square : squares) {
                square.setBoard(this);
            }
        }
    }


    @Override
    public String toString() {
        return "GameBoard{" +
                "id='" + id + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", ships=" + ships +
                ", boardOwner=" + boardOwner +
                ", squares=" + squares +
                '}';
    }
}
