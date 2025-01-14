package model.game;

import javax.sound.sampled.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sound {
    private boolean isSoundEnabled = true;
    private static final String SOUND_FILE = "soundSettings.txt";
    private final List<Clip> activeClips = new CopyOnWriteArrayList<>();
    private Sound() {
        isSoundEnabledChecker();
    }

    private static class Holder {
        private static final Sound INSTANCE = new Sound();
    }

    public static Sound getInstance() {
        return Holder.INSTANCE;
    }

    public boolean isSoundEnabled() {
        return isSoundEnabled;
    }

    public List<Clip> getActiveClips() {
        return activeClips;
    }

    public void isSoundEnabledChecker() {
        try (FileReader fr = new FileReader(SOUND_FILE)) {
            int savedSound;
            while ((savedSound = fr.read()) != -1) {
                this.isSoundEnabled = savedSound == 49;
            }
        } catch (IOException e) {
            System.out.println("Lỗi chỉnh âm thanh: " + e.getMessage());
        }
    }

    public void toggleSound() {
        try (FileWriter fw = new FileWriter(SOUND_FILE, false)) {
            isSoundEnabled = !isSoundEnabled;
            fw.write(isSoundEnabled ? "1" : "0");
        } catch (IOException e) {
            System.out.println("Lỗi lưu cài đặt âm thanh: " + e.getMessage());
        }
        System.out.println("Âm thanh đã được " + (isSoundEnabled ? "bật." : "tắt."));
        stopAllClips();
    }

    public void stopAllClips() {
        for (Clip clip : activeClips) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
            activeClips.remove(clip);
        }
    }
}