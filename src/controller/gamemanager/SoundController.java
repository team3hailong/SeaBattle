package controller.gamemanager;

import model.game.Sound;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundController {
    private final Sound sound;
    private static final ExecutorService SOUND_EXECUTOR = Executors.newCachedThreadPool();
    public SoundController() {
        this.sound = Sound.getInstance();
    }

    private static class Holder {
        private static final SoundController INSTANCE = new SoundController();
    }
    public static SoundController getInstance() {
        return Holder.INSTANCE;
    }

    public void playSoundWithDuration(String soundFile, boolean replay, int durationMillis, float volume) {
        if (!sound.isSoundEnabled()) {
            return;
        }
        SOUND_EXECUTOR.submit(() -> {
            try {
                List<Clip> activeClips = sound.getActiveClips();
                InputStream audioSrc = getClass().getResourceAsStream(soundFile);
                if (audioSrc == null) {
                    System.out.println("File âm thanh không tồn tại: " + soundFile);
                    return;
                }
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(bufferedIn);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                setVolume(clip, volume);
                clip.start();
                activeClips.add(clip);

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        if (!replay) {
                            clip.close();
                            activeClips.remove(clip);
                        } else {
                            clip.setFramePosition(0);
                            clip.start();
                        }
                    }
                });

                Thread.sleep(durationMillis);

                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.close();
                activeClips.remove(clip);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println("Lỗi khi chạy nhạc: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Âm thanh bị gián đoạn.");
            }
        });
    }

    public void playSoundWithDurationAsync(String soundFile, boolean replay, int durationMillis, float volume) {
        playSoundWithDuration(soundFile, replay, durationMillis, volume);
    }

    private void setVolume(Clip clip, float volume) {
        if (clip == null) return;

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (20.0 * Math.log10(volume));
        gainControl.setValue(dB);
    }
}
