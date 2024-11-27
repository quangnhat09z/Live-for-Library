package com.example.library.model;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Alert {
    public static void showInfoAlert(String title, String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait(); // Hiển thị và chờ người dùng đóng
    }

    public static void showWarningAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setHeaderText("WARNING");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccessAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION); // Sử dụng INFORMATION
        alert.setTitle("Success");
        alert.setHeaderText("SUCCESS");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void temptLabel(Label label, String message, Color color) {
        // Cập nhật nội dung của Label
        label.setText(message);

        // Cập nhật màu sắc
        label.setTextFill(color);

        // Hiển thị Label
        label.setVisible(true);

        // Sử dụng PauseTransition để ẩn Label sau khoảng thời gian
        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        pause.setOnFinished(event -> label.setVisible(false));
        pause.play();
    }


}
