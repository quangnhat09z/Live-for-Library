package com.example.library.controller;

import javafx.scene.control.Alert;

public abstract class SearchController extends Controller {
    protected abstract void handleSearchButton();
    protected abstract void handleDeleteButton();
    protected abstract void handleChangeInfoButton();
    protected abstract void handleChangeToManageButton();
    protected abstract void handleChangeButton();

    protected static void showInfoAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait(); // Hiển thị và chờ người dùng đóng
    }

    protected static void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("WARNING");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
