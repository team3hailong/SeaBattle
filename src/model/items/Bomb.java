package model.items;

import model.game.*;
import controller.gamemanager.BoardController;
import view.EventInterface;

import java.io.Serial;
import java.io.Serializable;

public class Bomb extends Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public static EventInterface eventInterface = EventInterface.getInstance();
    public Bomb() {
        super("Bom 2x2");
    }

    @Override
    public void activate(Player player, BoardController opponentBoardController, char xTarget, int yTarget) {
        for (char xAris = xTarget; xAris < xTarget + 2; xAris++) {
            for (int yAris = yTarget; yAris < yTarget + 2; yAris++) {
                Board opponentBoard = opponentBoardController.getBoard();
                if (opponentBoard.isCoordinateValid(xAris, yAris)) {
                    Cell target = opponentBoard.getCell(xAris, yAris);
                    opponentBoardController.fireAt(target);
                }
            }
        }
        eventInterface.explosion();
    }
}
