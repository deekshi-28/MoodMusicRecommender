package com.example.moodmusic;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private Clip clip;
    private AudioInputStream audioInputStream;
    private Long currentFrame; // for pause/resume
    private String status;

    public void play(String filePath) {
        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.err.println("Audio file not found: " + filePath);
                return;
            }
            audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            status = "playing";
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            try { if (audioInputStream != null) audioInputStream.close(); } catch (IOException ignored) {}
            status = "stopped";
        }
    }

    public void pause() {
        if (clip != null && status.equals("playing")) {
            currentFrame = clip.getMicrosecondPosition();
            clip.stop();
            status = "paused";
        }
    }

    public void resume() {
        if (clip != null && status.equals("paused")) {
            clip.setMicrosecondPosition(currentFrame);
            clip.start();
            status = "playing";
        }
    }

    public boolean isPlaying() {
        return clip != null && clip.isActive();
    }
}
