package model.game;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Leaderboard {
    private List<PlayerRecord> records;
    private static final String RECORD_FILE = "record.txt";
    private Leaderboard() {
        loadFromFile();
    }
    private static class Holder {
        private static final Leaderboard INSTANCE = new Leaderboard();
    }

    public static Leaderboard getInstance() {
        return Holder.INSTANCE;
    }
    public void addRecord(PlayerRecord record) {
        records.add(record);
        saveToFile();
    }
    public void display() {
        if (records.isEmpty()) {
            System.out.println("Hiện chưa có thành tích nào được lưu lại.");
        }
        else {
            records.sort(Comparator.comparingInt(PlayerRecord::getShotsTaken));
            System.out.println("╔══════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                             Bảng Xếp Hạng                                ║");
            System.out.println("╠══════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("║ %-32s | %-17s | %-17s ║\n", "Tên Người Chơi", "Số Lượt Bắn", "Số Tàu Còn Lại");
            for (PlayerRecord record : records) {
                System.out.printf("║ %-32s | %-17d | %-17d ║\n", record.getPlayerName(), record.getShotsTaken(), record.getRemainingShips());
            }
            System.out.println("╚══════════════════════════════════════════════════════════════════════════╝");
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RECORD_FILE))) {
            oos.writeObject(records);
        } catch (IOException e) {
            System.out.println("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RECORD_FILE))) {
            records = (List<PlayerRecord>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            records = new ArrayList<>();
        }
    }
}
