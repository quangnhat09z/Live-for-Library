package com.example.library.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class AudioManagement {
    private MediaPlayer mediaPlayer;

    public AudioManagement(String audioFilePath) {
        // Tạo đối tượng Media từ tệp âm thanh
        Media media = new Media(new File(audioFilePath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    // Phát âm thanh
    public void playAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Dừng nếu đang phát
            mediaPlayer.play(); // Phát lại âm thanh
        }
    }

    // Tạm dừng âm thanh
    public void pauseAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // Dừng âm thanh
    public void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    // Tắt âm thanh
    public void muteAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.setMute(true);
        }
    }

    // Bật âm thanh
    public void unmuteAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.setMute(false);
        }
    }
}