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
    public void handleHomeButton() {
        changeScene("/com/example/library/main-view.fxml", "Live for Library");
    }

    @FXML
    public void handleBookButton() {
        // Xử lý cho nút Button1
        System.out.println("Button1 clicked");
    }

    @FXML
    public void handleExploreButton() {
        // Xử lý cho nút Button2
        changeScene("/com/example/library/explore-view.fxml", "Explore");
    }

    @FXML
    public void handleGameButton() {
        changeScene("/com/example/library/gameProgress-view.fxml", "Game");
    }

    @FXML
    protected abstract void changeScene(String fxmlPath, String title);
}

