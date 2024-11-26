package com.example.library.controller;


import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import static com.example.library.model.SoundUtil.applySoundEffectsToButtons;
import java.io.IOException;

public class GameProgressController extends Controller{
    @FXML
    private HBox root;
    @FXML
    private Button homeButton;
    @FXML
    private Button bookButton;
    @FXML
    private Button exploreButton;
    @FXML
    private Button mainButton;
    @FXML
    private JFXButton playButton;
    @FXML
    private Circle myCircle;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize() {
        homeButton.setOnAction(actionEvent -> handleHomeButton());
        bookButton.setOnAction(actionEvent -> handleBookButton());
        exploreButton.setOnAction(actionEvent -> handleExploreButton());
        mainButton.setOnAction(actionEvent -> handleGameButton());
        playButton.setOnAction(actionEvent -> handlePlayButton());

        applySoundEffectsToButtons(root);
    }


    @FXML
    private void handlePlayButton() {
        // Hiện hình tròn bằng hiệu ứng mờ dần
        myCircle.setOpacity(0); // Bắt đầu với độ mờ 0 (ẩn)
        myCircle.setVisible(true); // Đảm bảo hình tròn có thể nhìn thấy

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), myCircle);
        fadeIn.setFromValue(0); // Độ mờ bắt đầu
        fadeIn.setToValue(1); // Độ mờ kết thúc
        fadeIn.setOnFinished(event -> {
            // Tạo hiệu ứng xoay cho hình tròn
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), myCircle);
            rotateTransition.setByAngle(360); // Xoay 360 độ
            rotateTransition.setCycleCount(1); // Chỉ lặp lại 1 lần

            // Khi hiệu ứng hoàn tất, chuyển màn hình
            rotateTransition.setOnFinished(e -> {
                 changeScene("/com/example/library/game-view.fxml", "Game");
                // music();
            });

            rotateTransition.play(); // Bắt đầu hiệu ứng xoay
        });

        fadeIn.play(); // Bắt đầu hiệu ứng mờ dần
    }

//    private void music() {
//        String musicFile = "src/main/resources/audio/gameMusic.mp3";
//        Media sound = new Media(Paths.get(musicFile).toUri().toString());
//        if (mediaPlayer == null) {
//            mediaPlayer = new MediaPlayer(sound);
//            mediaPlayer.play();
//            mediaPlayer.setVolume(50);
//        } else if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
//            mediaPlayer.play();
//        }
//    }
//

    @Override
    public void changeScene(String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 650);
            Stage stage = (Stage) mainButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}