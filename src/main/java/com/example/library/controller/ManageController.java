package com.example.library.controller;


import javafx.scene.control.Alert;

public abstract class ManageController extends Controller {
    protected abstract void handleAddButton();
    protected abstract void handleShowButton();
    protected abstract void handleDeleteButton();
    protected abstract void handleSearchButton();
    protected abstract void handleChangeButton();

    protected static void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
