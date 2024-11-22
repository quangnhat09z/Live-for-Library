package com.example.library.controller;

import javafx.fxml.FXML;

public abstract class Controller {
    private static boolean darkMode;
    public static boolean admin;

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean darkMode) {
        Controller.darkMode = darkMode;
    }

    public static boolean isAdmin() {
        return admin;
    }

    public static void setAdmin(boolean admin) {
        Controller.admin = admin;
    }

    @FXML
    public abstract void initialize();

    @FXML
    public abstract void handleHomeButton();

    @FXML
    public abstract void handleBookButton();

    @FXML
    public abstract void handleExploreButton();

    @FXML
    public abstract void handleGameButton();

    @FXML
    protected abstract void changeScene(String fxmlPath, String title);
}

