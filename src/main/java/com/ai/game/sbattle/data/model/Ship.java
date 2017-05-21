package com.ai.game.sbattle.data.model;

import com.ai.game.sbattle.utils.ModelTransform;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

/**
 * Created by netikras on 17.5.15.
 */

@Entity
@Table(name = "ships")
public class Ship {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @ModelTransform(dtoFieldName = "id", dtoUpdatable = false)
    private String id;

    @OneToMany(orphanRemoval = true/*, fetch = FetchType.EAGER*/, cascade = CascadeType.ALL, mappedBy = "hostedShip")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ModelTransform(dtoFieldName = "squareIds", dtoValueExtractField = "id", dtoUpdatable = false)
    private List<Square> squares;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id")
    @ModelTransform(dtoFieldName = "boardId", dtoUpdatable = false, dtoValueExtractField = "id")
    private GameBoard board;

    @Column(name = "is_killed")
    @ModelTransform(dtoFieldName = "killed", dtoUpdatable = false)
    private boolean killed = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    @ModelTransform(dtoFieldName = "type", dtoUpdatable = false)
    private ShipType type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Square> getSquares() {
        return squares;
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public boolean isKilled() {
        return killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public ShipType getType() {
        return type;
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id='" + id + '\'' +
                ", squares=" + squares +
                ", board=" + board +
                ", killed=" + killed +
                ", type=" + type +
                '}';
    }

    public enum ShipType {
        CARRIER(5, 1),
        BATTLESHIP(4, 1),
        CRUISER(3, 1),
        DESTROYER(2, 2),
        SUBMARINE(1, 2);


        private final int size;
        private final int count;

        ShipType(int size, int countPerBoard) {
            this.size = size;
            this.count = countPerBoard;
        }

        public int getShipSize() {
            return size;
        }

        public int getCount() {
            return count;
        }
    }


}
