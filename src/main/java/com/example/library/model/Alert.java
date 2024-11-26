package com.example.library.model;

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
}
