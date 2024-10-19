package com.example.library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {

    @FXML
    private Button homeButton;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button setting_button;
    @FXML
    private Button user_button;

    @FXML
    public void initialize() {
        homeButton.setOnAction(actionEvent -> handleWorkbenchButton());
        button1.setOnAction(actionEvent -> handleButton1());
        button2.setOnAction(actionEvent -> handleButton2());
        button3.setOnAction(actionEvent -> handleButton3());
        button4.setOnAction(actionEvent -> handleButton4());
        setting_button.setOnAction(actionEvent -> handleSettingButton());
        user_button.setOnAction(actionEvent -> handleUserButton());
    }

    private void handleWorkbenchButton() {
        // Xử lý cho nút Home
        System.out.println("Home button clicked");
    }

    private void handleButton1() {
        // Xử lý cho nút Button1
        System.out.println("Button1 clicked");
    }

    private void handleButton2() {
        // Xử lý cho nút Button2
        System.out.println("Button2 clicked");
    }

    private void handleButton3() {
        // Xử lý cho nút Button3
        System.out.println("Button3 clicked");
    }

    private void handleButton4() {
        // Xử lý cho nút Button4
        System.out.println("Button4 clicked");
    }

    private void handleSettingButton() {
        //Xử lý cho nút setting
        System.out.println("SettingButton clicked");
    }

    private void handleUserButton() {
        System.out.println("UserButton clicked");
    }
}
