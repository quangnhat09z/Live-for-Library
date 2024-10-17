package com.example.library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

public class MainController {

    public BorderPane borderPane;
    @FXML
    private Label usernameLabel; // Thêm Label

    @FXML
    private Button actionButton; // Thêm Button

    @FXML
    private ImageView backgroundImage; // Thêm ImageView

    @FXML
    public void initialize() {
        // Thiết lập hình ảnh nền từ controller
        //Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/background.png"))); // Đường dẫn đến hình ảnh
        //backgroundImage.setImage(image);

        DropShadow shadow = new DropShadow();
        //actionButton.setEffect(shadow);
        //actionButton.setStyle("-fx-background-color: #FF0000");

        actionButton.setOnMousePressed(e -> handleButtonPressed());
        actionButton.setOnMouseReleased(e -> handleButtonReleased());
    }

    @FXML
    private void handleButtonPressed() {
        actionButton.setStyle("-fx-background-color: #6EC2F7;");
    }

    private void handleButtonReleased() {
        actionButton.setStyle("-fx-background-color: #27a4f2;");
        System.out.println("Action button clicked");
        // Thêm logic xử lý cho nút ở đây
    }
}
