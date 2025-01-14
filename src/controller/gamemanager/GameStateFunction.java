package controller.gamemanager;

import model.game.GameState;
import model.game.Player;
import java.io.*;

public class GameStateFunction {
    private static final String SAVE_FILE_PATH = "tempGame.txt";

    private static class Holder {
        private static final GameStateFunction INSTANCE = new GameStateFunction();
    }

    public static GameStateFunction getInstance() {
        return Holder.INSTANCE;
    }

    public void saveGame(Player currentTurn, Player opponent, boolean itemEnabled) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
            GameState gameState = new GameState(currentTurn, opponent, itemEnabled);
            oos.writeObject(gameState);
            System.out.println("Trạng thái trò chơi đã được lưu thành công.");
        } catch (IOException e) {
            System.out.println("Có lỗi trong quá trình lưu file: " + e.getMessage());
        }
    }
    public void saveAndPauseGame(Player currentTurn, Player opponent, boolean itemEnabled) {
        saveGame(currentTurn, opponent, itemEnabled);
        System.out.println("Game đã được lưu. Kết thúc lượt.");
    }
    public GameState loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE_PATH))) {
            GameState gameState = (GameState) ois.readObject();
            System.out.println("Trạng thái trò chơi đã được phục hồi thành công.");
            return gameState;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Có lỗi trong quá trình đọc file: " + e.getMessage());
        }
        return null;
    }
    public void clearTempGame() {
        try (FileWriter fw = new FileWriter(SAVE_FILE_PATH, false)) {
            fw.write("");
            //System.out.println("Tệp lưu trữ trạng thái trò chơi đã được xóa.");
        } catch (IOException e) {
            System.out.println("Lỗi khi xóa tempGame.txt: " + e.getMessage());
        }
    }
}