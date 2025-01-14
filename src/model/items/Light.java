package model.items;

import model.game.Cell;
import model.game.Player;
import controller.gamemanager.BoardController;

import java.io.Serial;
import java.io.Serializable;

public class Light extends Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Light() {
        super("Đèn");
    }

    @Override
    public void activate(Player player, BoardController opponentBoardController, char x, int y) {
        Cell cell = opponentBoardController.getBoard().getCell(x, y);
        if (cell != null) {
            System.out.println("Tại vị trí " + x  + y + ": " + ((cell.getShip()!=null) ? "Có tàu" : "Không có tàu"));
        }
    }
}
