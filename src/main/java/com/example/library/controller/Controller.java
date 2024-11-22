package com.example.library.controller;

import javafx.fxml.FXML;

public abstract class Controller {
    public static final boolean ADMIN = true;

    @FXML
    public abstract void initialize();
    public abstract void handleHomeButton();
    public abstract void handleBookButton();
    public abstract void handleExploreButton();
    public abstract void handleGameButton();
    protected abstract void changeScene(String fxmlPath, String title);
}

