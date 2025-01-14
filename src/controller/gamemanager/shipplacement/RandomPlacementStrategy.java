package controller.gamemanager.shipplacement;

import controller.gamemanager.BoardController;
import controller.gamemanager.GameFunction;
import model.enums.Orientation;
import model.game.Cell;
import model.game.Ship;

import java.util.List;

public class RandomPlacementStrategy implements PlacementStrategy {
    @Override
    public void placeShips(List<Ship> ships, BoardController boardController) {
        for (Ship ship : ships) {
            boolean placed = false;
            while (!placed) {
                char x = (char) ('A' + GameFunction.random.nextInt(boardController.getBoard().getSize()));
                int y = GameFunction.random.nextInt(boardController.getBoard().getSize()) + 1;
                Orientation orientation = GameFunction.random.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;
                Cell startCell = boardController.getBoard().getCell(x, y);
                placed = boardController.placeShip(ship, startCell, orientation);
            }
        }
    }
}
