package model.items;

import model.game.Cell;
import model.game.Player;
import controller.gamemanager.BoardController;

import java.io.Serial;
import java.io.Serializable;

public class Shield extends Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Shield() {
        super("Khiên");
    }

    @Override
    public void activate(Player player, BoardController boardController, char x, int y) {
        Cell cell = player.getBoard().getCell(x, y);
        if (cell != null && cell.getShip() != null) {
            cell.getShip().setShielded(true);
        }
    }
}
