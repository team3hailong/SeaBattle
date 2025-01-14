package model.game;

import model.enums.CellStatus;

import java.io.Serial;
import java.io.Serializable;

public class Cell implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public char xAris;
    public int yAris;
    private CellStatus status;
    private Ship ship;
    public Cell(char x, int y){
        this.xAris = x;
        this.yAris = y;
        this.status = CellStatus.EMPTY;
        this.ship = null;
    }

    public char getXAris() {
        return xAris;
    }

    public int getYAris() {
        return yAris;
    }

    public CellStatus getStatus() {
        return status;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
}

