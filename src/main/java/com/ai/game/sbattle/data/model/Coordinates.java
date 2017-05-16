package com.ai.game.sbattle.data.model;

import com.ai.game.sbattle.utils.ModelTransform;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by netikras on 17.5.16.
 */
@Entity
@Table(name = "coordinates")
public class Coordinates {

    public Coordinates() {}

    public Coordinates(int x, int y) {
        this();
        setX(x);
        setY(y);
        setId(UUID.randomUUID().toString());
    }

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @ModelTransform(dtoFieldName = "id", dtoUpdatable = false)
    private String id;

    @Column(name = "x", updatable = false)
    @ModelTransform(dtoFieldName = "x", dtoUpdatable = false)
    private int x;

    @Column(name = "y", updatable = false)
    @ModelTransform(dtoFieldName = "y", dtoUpdatable = false)
    private int y;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
