package model.items;

import model.game.Player;
import controller.gamemanager.BoardController;

import java.io.Serial;
import java.io.Serializable;

public abstract class Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void activate(Player player, BoardController opponentBoardController, char x, int y);
}
