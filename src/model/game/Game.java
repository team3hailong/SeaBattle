package model.game;

import model.enums.GameStatus;

public class Game {
    private final Player player1;
    private final Player player2;
    private Player currentTurn;
    private GameStatus status;

    public Game(String player1Name, String player2Name, int boardSize) {
        this.player1 = new Player(player1Name, boardSize);
        this.player2 = new Player(player2Name, boardSize);
        this.currentTurn = player1;
        this.status = GameStatus.READY;
    }

    public Game(GameState gameState) {
        this.player1 = gameState.player1();
        this.player2 = gameState.player2();
        this.currentTurn = player1;
        this.status = GameStatus.IN_PROGRESS;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Player currentTurn) {
        this.currentTurn = currentTurn;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }
}
