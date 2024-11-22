package com.example.library.model;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class SoundUtil {

    private static final String HOVER_SOUND_PATH = "src/main/resources/audio/hover.mp3";
    private static final String PRESSED_SOUND_PATH = "src/main/resources/audio/button_pressed.mp3";

    private static MediaPlayer hoverMediaPlayer;
    private static MediaPlayer pressedMediaPlayer;

    public static MediaPlayer getHoverMediaPlayer() {
        return hoverMediaPlayer;
    }

    public static void setHoverMediaPlayer(MediaPlayer hoverMediaPlayer) {
        SoundUtil.hoverMediaPlayer = hoverMediaPlayer;
    }

    public static MediaPlayer getPressedMediaPlayer() {
        return pressedMediaPlayer;
    }

    public static void setPressedMediaPlayer(MediaPlayer pressedMediaPlayer) {
        SoundUtil.pressedMediaPlayer = pressedMediaPlayer;
    }

    static {
        Media hoverSound = new Media(Paths.get(HOVER_SOUND_PATH).toUri().toString());
        hoverMediaPlayer = new MediaPlayer(hoverSound);

        Media pressedSound = new Media(Paths.get(PRESSED_SOUND_PATH).toUri().toString());
        pressedMediaPlayer = new MediaPlayer(pressedSound);
    }

    public static void playHoverSound() {
        hoverMediaPlayer.stop();
        hoverMediaPlayer.play();
    }

    public static void playPressedSound() {
        pressedMediaPlayer.stop();
        pressedMediaPlayer.play();
    }

    public static void applySoundEffectsToButtons(Pane pane) {
        for (Node node : pane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setOnMouseEntered(event -> SoundUtil.playHoverSound());
                button.setOnMousePressed(event -> SoundUtil.playPressedSound());
            } else if (node instanceof Pane) {
                applySoundEffectsToButtons((Pane) node); // Recursively apply to nested panes
            }
        }
    }

    public static void setSfxVolume(double volume) {
        if (volume >= 0.0 && volume <= 1.0) {
            hoverMediaPlayer.setVolume(volume);
            pressedMediaPlayer.setVolume(volume);
        }
    }
} 