package model.game;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import model.enums.ShipType;
import model.items.Item;

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String name;
    private final Board board;
    private int turn;
    private final List<Ship> ships;
    private final List<Item> items;
    public Player(String name, int boardSize) {
        this.name = name;
        this.board = new Board(boardSize);
        this.ships = new ArrayList<>();
        this.items = new ArrayList<>();
        this.turn = 0;
        initializeShips();
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }

    private void initializeShips() {
        ships.add(new Ship(ShipType.PATROL_BOAT));
        ships.add(new Ship(ShipType.PATROL_BOAT));
        ships.add(new Ship(ShipType.DESTROYER));
        ships.add(new Ship(ShipType.SUBMARINE));
        ships.add(new Ship(ShipType.BATTLESHIP));
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public int getHits() {
        int hits = 0;
        for (Ship ship : ships) {
            hits += ship.getHits();
        }
        return hits;
    }

    public int getSunkShips() {
        return (int) ships.stream()
                .filter(Ship::isSunk)
                .count();
    }

    public int getRemainingShips() {
        return ships.size()-getSunkShips();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

}
