package com.example.library.controller.mainScreen;

import com.example.library.model.AudioManagement;
import com.example.library.model.GetQuiz;
import com.example.library.model.Quiz;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GameController extends Controller {
    @FXML
    private Button homeButton;
    @FXML
    private Button bookButton;
    @FXML
    private Button exploreButton;
    @FXML
    private Button mainButton;
    @FXML
    private JFXButton nextButton;
    @FXML
    private Label questionPilot;
    @FXML
    private Label questionNumber;
    @FXML
    private TextField answerA;
    @FXML
    private TextField answerB;
    @FXML
    private TextField answerC;
    @FXML
    private TextField answerD;
    @FXML
    private Button chooseA;
    @FXML
    private Button chooseB;
    @FXML
    private Button chooseC;
    @FXML
    private Button chooseD;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView quizImage;
    @FXML
    private ImageView window;
    @FXML
    private ImageView sunImage1, sunImage2, sunImage3;
    @FXML
    private AnchorPane tutorialPane;
    @FXML
    private AnchorPane endPane;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label scoreLabelCopy;
    @FXML
    private Label timeConsumeLabel;


    @FXML
    private Button buttonAgree;
    private Button chosenButton;
    private String chosenAnswer = "";
    private int wrongTimes = 0;
    private int timeSeconds = 21; // Thời gian đếm ngược (giây)
    private int scorePoint = 0;
    private int timeConsume = 0;

    @FXML
    private Label countdownLabel;
    @FXML
    private Label currentQuizIndexLabel;

    private AudioManagement gameMusic, wrongAnswerSound, correctAnswerSound;

    private List<Quiz> quizzes;

    private int currentQuizIndex = 0;

    private boolean isNextButtonClicked = false; // Biến để theo dõi trạng thái của nextButton
    private boolean hasAnswered = false;

    private Timeline timeline; // Khai báo Timeline ở cấp độ lớp để truy cập dễ dàng

    public GameController() {
        // Khởi tạo AudioManagement với đường dẫn đến tệp âm thanh
        gameMusic = new AudioManagement("src/main/resources/audio/gameMusic.mp3");
        wrongAnswerSound = new AudioManagement("src/main/resources/audio/wrongAnswer.mp3");
        correctAnswerSound = new AudioManagement("src/main/resources/audio/correctAnswer.mp3");
    }

    @Override
    public void initialize() {
        MainController.getMediaPlayer().pause();

        gameMusic.playAudio();
        endPane.setVisible(false);
        buttonAgree.setOnAction(event -> {
            fadeTrans(tutorialPane);
            loadQuiz(currentQuizIndex);
        });

        GetQuiz quiz = new GetQuiz();
        quizzes = quiz.getNumberOfQuiz();
        // Khởi tạo hiệu ứng quay cho các hình ảnh mặt trời
        sunTrans(sunImage1);
        sunTrans(sunImage2);
        sunTrans(sunImage3);

        homeButton.setOnAction(actionEvent -> handleHomeButton());
        bookButton.setOnAction(actionEvent -> handleBookButton());
        exploreButton.setOnAction(actionEvent -> handleExploreButton());
        mainButton.setOnAction(actionEvent -> handleGameButton());
        nextButton.setOnAction(actionEvent -> handleNextButton());
        exitButton.setOnAction(actionEvent -> handleHomeButton());
    }



    private void sunTrans(ImageView sunImage) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), sunImage);
        rotateTransition.setFromAngle(-15); // Góc bắt đầu
        rotateTransition.setToAngle(15);    // Góc kết thúc
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Lặp lại vô hạn
        rotateTransition.setAutoReverse(true); // Đảo ngược hiệu ứng
        rotateTransition.play(); // Bắt đầu hiệu ứng
    }

    private void loadQuiz(int index) {
        if (quizzes != null && !quizzes.isEmpty() && index < quizzes.size()) {

            Quiz quiz = quizzes.get(index);
            questionPilot.setText(quiz.getQuestion());
            questionNumber.setText("Câu hỏi " + (index + 1));

            // Tạo danh sách chứa tất cả các đáp án
            List<String> answers = new ArrayList<>();
            answers.add(quiz.getRightAnswer());
            answers.add(quiz.getWrongAnswer1());
            answers.add(quiz.getWrongAnswer2());
            answers.add(quiz.getWrongAnswer3());

            // Xáo trộn danh sách đáp án
            Collections.shuffle(answers);

            // Gán các đáp án đã xáo trộn cho các TextField
            answerA.setText(answers.get(0));
            answerB.setText(answers.get(1));
            answerC.setText(answers.get(2));
            answerD.setText(answers.get(3));

            // In ra để kiểm tra (tùy chọn)
            System.out.println("Answers: " + answers);
        }
        timeSeconds = 21;
        startCountdown();
    }

    private void changeColorAnswer(boolean check) {
        // Thay đổi màu sắc cho button và textField
        Quiz quiz = quizzes.get(currentQuizIndex);
        if (answerA.getText().equals(quiz.getRightAnswer())) {
            answerA.setStyle("-fx-border-color: green;");
            chooseA.setStyle("-fx-background-color: green;");
        }
        if (answerB.getText().equals(quiz.getRightAnswer())) {
            answerB.setStyle("-fx-border-color: green;");
            chooseB.setStyle("-fx-background-color: green;");
        }
        if (answerC.getText().equals(quiz.getRightAnswer())) {
            answerC.setStyle("-fx-border-color: green;");
            chooseC.setStyle("-fx-background-color: green;");
        }
        if (answerD.getText().equals(quiz.getRightAnswer())) {
            answerD.setStyle("-fx-border-color: green;");
            chooseD.setStyle("-fx-background-color: green;");
        }

        if (check) {
            if (!chosenAnswer.isEmpty()) chosenButton.setStyle("-fx-background-color: green;");
            if (chosenButton == chooseA) {
                answerA.setStyle("-fx-border-color: green;");
            } else if (chosenButton == chooseB) {
                answerB.setStyle("-fx-border-color: green;");
            } else if (chosenButton == chooseC) {
                answerC.setStyle("-fx-border-color: green;");
            } else if (chosenButton == chooseD) {
                answerD.setStyle("-fx-border-color: green;");
            }
        } else if (chosenButton != null || chosenAnswer.isEmpty()) {
            if (!chosenAnswer.isEmpty()) chosenButton.setStyle("-fx-background-color: red;");
            if (chosenButton == chooseA && !chosenAnswer.isEmpty() ) {
                answerA.setStyle("-fx-border-color: red;");
            } else if (chosenButton == chooseB && !chosenAnswer.isEmpty()) {
                answerB.setStyle("-fx-border-color: red;");
            } else if (chosenButton == chooseC && !chosenAnswer.isEmpty()) {
                answerC.setStyle("-fx-border-color: red;");
            } else if (chosenButton == chooseD && !chosenAnswer.isEmpty()) {
                answerD.setStyle("-fx-border-color: red;");
            }
        }
    }

    private void restoreColorAnswer(Button chosenButton, ActionEvent event) {
        // Nếu đã nhấn nút nextButton, không thực hiện gì thêm
        if (isNextButtonClicked) {
            return;
        } else if (wrongTimes == 3) {
            hasAnswered = true;
            // Dừng 3 giây trước khi hiển thị endPane
            new Timer(3000, e -> {
                endPane.setVisible(true);
            }).start();
            scoreLabelCopy.setText(String.valueOf(scorePoint));
            currentQuizIndexLabel.setText(String.valueOf(scorePoint + 3));
            timeConsumeLabel.setText(timeConsume + "s");
            return;
        }

        // Tạo và khởi động Timeline
        timeline = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            restore(chosenButton);// Khôi phục màu sắc ban đầu
            handleNextButton(); // Chuyển sang câu hỏi tiếp theo
        }));
        timeline.play();
    }

    private void restore(Button chosenButton) {
        hasAnswered = false;
        window.setVisible(true);

        List<TextField> textFieldList = new ArrayList<>();
        textFieldList.add(answerA);
        textFieldList.add(answerB);
        textFieldList.add(answerC);
        textFieldList.add(answerD);
        for (TextField x:textFieldList) {
            x.setStyle("");
        }

        List<Button> buttonList = new ArrayList<>();
        buttonList.add(chooseA);
        buttonList.add(chooseB);
        buttonList.add(chooseC);
        buttonList.add(chooseD);
        for (Button x:buttonList) {
            x.setStyle("");
        }

        // Xóa hình ảnh
        quizImage.setImage(null);

    }

    @FXML
    private void chooseAnswer(ActionEvent event) {
        window.setVisible(false);
        // Kiểm tra xem người chơi đã chọn đáp án chưa
        if (hasAnswered) {
            System.out.println("Đã chọn");
            if (chosenButton != null ) System.out.println("You have already answered this question." + chosenButton.getText());
            return; // Nếu đã trả lời, không làm gì thêm
        }

        if (event == null) {
            changeColorAnswer(false);
            chosenButton = null;
        }
        else {
            // Đặt trạng thái đã trả lời
            hasAnswered = true;
            chosenButton = (Button) event.getSource();
            timeline.stop();
        }

        chosenAnswer = ""; // Khởi tạo biến để lưu đáp án
        // Lấy giá trị đúng từ nút nhấn
        if (chosenButton == chooseA) {
            chosenAnswer = answerA.getText();
        } else if (chosenButton == chooseB) {
            chosenAnswer = answerB.getText();
        } else if (chosenButton == chooseC) {
            chosenAnswer = answerC.getText();
        } else if (chosenButton == chooseD) {
            chosenAnswer = answerD.getText();
        } else if (chosenButton == null) {
            chosenAnswer = "";
        }

        if(!Objects.equals(chosenAnswer, "")) {
            System.out.println("Chosen answer: " + chosenButton.getText() + " " + chosenAnswer);
        } else {
            System.out.println("Chosen answer: null");
        }
        Quiz currentQuiz = quizzes.get(currentQuizIndex);

        Quiz quiz = quizzes.get(currentQuizIndex);
        String imageUrl = quiz.getImageLink();
        boolean hasImage = (imageUrl != null && !imageUrl.isEmpty());


        // Tải hình ảnh nếu có
        if (hasImage) {
            Image image = new Image(imageUrl);
            quizImage.setImage(image);
            quizImage.setFitWidth(294);
            quizImage.setFitHeight(294);
            quizImage.setPreserveRatio(true);

            // Hiệu ứng mờ dần
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), quizImage);
            fadeIn.setFromValue(0); // Bắt đầu từ độ mờ
            fadeIn.setToValue(1);   // Kết thúc ở độ rõ
            fadeIn.setCycleCount(1); // Chỉ chạy một lần
            fadeIn.play(); // Bắt đầu hiệu ứng
        } else {
            quizImage.setImage(null); // Xóa hình ảnh nếu không có
        }

        // Kiểm tra đáp án
        if (chosenAnswer.equals(currentQuiz.getRightAnswer())) {
            correctAnswerSound.playAudio();
            scorePoint++;
            scoreLabel.setText(String.valueOf(scorePoint));
            System.out.println("Correct answer!");
            changeColorAnswer( true); // Thay đổi màu sắc cho button và textField
            restoreColorAnswer(chosenButton, event); // Tạo hiệu ứng khôi phục màu sắc
        } else {
            wrongAnswerSound.playAudio();
            wrongTimes++;
            lostSun(wrongTimes);
            System.out.println("wrongTimes: " + wrongTimes);
            changeColorAnswer(false);
            restoreColorAnswer(chosenButton, event);
            System.out.println("Wrong answer! The correct answer is: " + currentQuiz.getRightAnswer());
        }
    }

    private void fadeTrans(ImageView imageView) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), imageView);
        fadeOut.setFromValue(1); // Bắt đầu từ độ rõ
        fadeOut.setToValue(0);    // Kết thúc ở độ mờ
        fadeOut.setCycleCount(1); // Chỉ chạy một lần

        // Ẩn hình ảnh khi hiệu ứng mờ hoàn tất
        fadeOut.setOnFinished(event -> imageView.setVisible(false));

        fadeOut.play(); // Bắt đầu hiệu ứng
    }
    private void fadeTrans(AnchorPane tutorialPane) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), tutorialPane);
        fadeOut.setFromValue(1); // Bắt đầu từ độ rõ
        fadeOut.setToValue(0);    // Kết thúc ở độ mờ
        fadeOut.setCycleCount(1); // Chỉ chạy một lần

        // Ẩn hình ảnh khi hiệu ứng mờ hoàn tất
        fadeOut.setOnFinished(event -> tutorialPane.setVisible(false));

        fadeOut.play(); // Bắt đầu hiệu ứng
    }

    private void lostSun(int wrongTimes) {
        switch (wrongTimes) {
            case 1:
                fadeTrans(sunImage3);
                break;
            case 2:
                fadeTrans(sunImage2);
                break;
            default:
                fadeTrans(sunImage1);
                break;
        }
    }

    // Phương thức để gán trạng thái cho nextButton
    public void handleNextButton() {
        isNextButtonClicked = true; // Đánh dấu rằng nút nextButton đã được nhấn
        if (wrongTimes == 3) return;
        // Hủy Timeline nếu nó đang chạy
        if (timeline != null) {
            timeline.stop(); // Dừng Timeline
        }
        restore(chosenButton);
        // Logic chuyển sang câu hỏi tiếp theo
        if (currentQuizIndex < quizzes.size() - 1) {
            currentQuizIndex++;
            loadQuiz(currentQuizIndex);
        } else {
            currentQuizIndex = 0; // Reset lại
            System.out.println("End of quiz. Restarting...");
            loadQuiz(currentQuizIndex);
        }

        startCountdown();

        // Đặt lại trạng thái cho nextButton
        isNextButtonClicked = false; // Đặt lại trạng thái sau khi xử lý
    }

    private void startCountdown() {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            return; // Nếu đang chạy, không bắt đầu lại
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (timeSeconds > 0) {
                timeSeconds--;
                timeConsume++;
                countdownLabel.setText( timeSeconds + " s");
            } else {
                countdownLabel.setText("0");
                timeline.stop(); // Dừng countdown khi hết thời gian
                chosenAnswer = "";
                restore(null);
                chooseAnswer(null);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Lặp lại vô hạn
        timeline.play(); // Bắt đầu đếm ngược
    }

    @Override
    public void changeScene(String fxmlPath, String title) {
        try {
            // Dừng âm nhạc và đếm ngược khi chuyển màn hình
            if (timeline != null) {
                timeline.stop(); // Dừng Timeline
            }
            gameMusic.stopAudio(); // Dừng âm nhạc

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) exitButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);

            // Optional: Set minimum size
            stage.setMinWidth(800);
            stage.setMinHeight(600);

            // Reset các biến liên quan đến câu hỏi và thời gian
            currentQuizIndex = 0;
            timeSeconds = 21;
            scorePoint = 0;
            wrongTimes = 0;
            hasAnswered = false;
            isNextButtonClicked = false;

            MainController.getMediaPlayer().play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}