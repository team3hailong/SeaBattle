package model.game;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.enums.ShipType;
import model.enums.CellStatus;

import static view.EventInterface.reset;

public class Ship implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final ShipType type;
    private final int size;
    private final List<Cell> cells;
    private int hits;
    private boolean shielded = false;

    private static final String GREEN_BACKGROUND = "\u001B[42m";
    private static final String YELLOW_BACKGROUND = "\u001B[43m";
    private static final String BLUE_BACKGROUND = "\u001B[44m";
    private static final String PURPLE_BACKGROUND = "\u001B[45m";

    private enum ShipColor {
        PATROL_BOAT(BLUE_BACKGROUND),
        DESTROYER(PURPLE_BACKGROUND),
        SUBMARINE(YELLOW_BACKGROUND),
        BATTLESHIP(GREEN_BACKGROUND);

        private final String colorCode;

        ShipColor(String colorCode) {
            this.colorCode = colorCode;
        }
        public String getColorCode() {
            return colorCode;
        }

        @Override
        public String toString() {
            return colorCode + "🚢 " + this.name() + reset;
        }
    }

    public Ship(ShipType type) {
        this.type = type;
        this.size = getSizeByType();
        this.cells = new ArrayList<>();
        this.hits = 0;
    }

    public boolean isShielded() {
        return shielded;
    }

    public void setShielded(boolean shielded) {
        this.shielded = shielded;
    }

    private int getSizeByType() {
        return switch (type) {
            case PATROL_BOAT -> 2;
            case DESTROYER -> 4;
            case SUBMARINE -> 3;
            case BATTLESHIP -> 5;
        };
    }

    public String getColorCode() {
        return ShipColor.valueOf(type.name()).getColorCode();
    }

    public ShipType getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public int getHits() {
        return hits;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
        cell.setShip(this);
        cell.setStatus(CellStatus.SHIP);
    }

    public void takeHit() {
        hits++;
    }

    public boolean isSunk() {
        return hits == size;
    }

    public static void shipsList() {
        for (ShipColor sc : ShipColor.values()) {
            System.out.print(sc + "\t");
        }
    }
}
