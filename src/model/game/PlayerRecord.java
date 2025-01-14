package model.game;

import java.io.Serial;
import java.io.Serializable;

public class PlayerRecord implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public String playerName;
    public int shotsTaken;
    public int remainingShips;

    public PlayerRecord(String playerName, int shotsTaken, int remainingShips) {
        this.playerName = playerName;
        this.shotsTaken = shotsTaken;
        this.remainingShips = remainingShips;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getShotsTaken() {
        return shotsTaken;
    }

    public int getRemainingShips() {
        return remainingShips;
    }
}
