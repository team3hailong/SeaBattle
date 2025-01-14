package controller.gamemanager.shipplacement;

import controller.gamemanager.BoardController;
import model.game.Ship;
import java.util.List;

public class ShipPlacement {
    private PlacementStrategy placementStrategy;

    public void setPlacementStrategy(PlacementStrategy placementStrategy) {
        this.placementStrategy = placementStrategy;
    }
    public void placeShips(List<Ship> ships, BoardController boardController) {
        placementStrategy.placeShips(ships, boardController);
    }
}