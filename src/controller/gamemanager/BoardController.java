package controller.gamemanager;

import model.game.*;
import model.enums.*;

public class BoardController {
    public Board board;

    public BoardController(Board board) {
        this.board = board;
    }

    public boolean placeShip(Ship ship, Cell start, Orientation orientation) {
        int dx = 0, dy = 0;
        if (orientation == Orientation.HORIZONTAL)
            dy = 1;
        else
            dx = 1;

        int row = start.getXAris() - 'A';
        int col = start.getYAris() - 1;

        int endRow = row + dx * (ship.getSize() - 1);
        int endCol = col + dy * (ship.getSize() - 1);
        if (endRow >= board.getSize() || endCol >= board.getSize()) {
            return false;
        }

        Cell[][] cells = board.getCells();
        for (int distance = 0; distance < ship.getSize(); distance++) {
            int currentRow = row + dx * distance;
            int currentCol = col + dy * distance;
            if (cells[currentRow][currentCol].getStatus() != CellStatus.EMPTY) {
                return false;
            }
        }

        for (int distance = 0; distance < ship.getSize(); distance++) {
            int currentRow = row + dx * distance;
            int currentCol = col + dy * distance;
            Cell cell = cells[currentRow][currentCol];
            ship.addCell(cell);
        }

        return true;
    }

    public FireResult fireAt(Cell target) {
        Ship targetShip = target.getShip();

        if (target.getStatus() == CellStatus.SHIP) {
            if(targetShip.isShielded()){
                System.out.println("Tàu được bảo vệ bởi lá chắn!");
                targetShip.setShielded(false);
                return FireResult.MISS;
            }
            target.setStatus(CellStatus.HIT);
            targetShip.takeHit();
            if (targetShip.isSunk()) {
                return FireResult.SUNK;
            }
            return FireResult.HIT;
        } else {
            target.setStatus(CellStatus.MISS);
            return FireResult.MISS;
        }
    }

    public Board getBoard() {
        return board;
    }
}

