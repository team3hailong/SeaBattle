package controller.gamemanager.shipplacement;

import controller.gamemanager.BoardController;
import model.game.Ship;

import java.util.List;

public interface PlacementStrategy {
    void placeShips(List<Ship> ships, BoardController boardController);
}
