package com.example.library.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import com.example.library.model.DatabaseHelper;
import com.example.library.model.Document;

import java.awt.Desktop;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.example.library.model.SoundUtil.applySoundEffectsToButtons;

public class ExploreController {
    @FXML
    private HBox root;
    @FXML
    private TextField searchExploreField;
    @FXML
    private ListView<String> suggestionsList;
    @FXML
    private ImageView myImageView;
    @FXML
    private TextField idField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publicYearField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField quantityField;
    @FXML
    private Hyperlink myHyperlink;
    @FXML
    private Button showButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button button1;
    @FXML
    private Button button3;


    private DatabaseHelper databaseHelper;
    private boolean isListViewVisible = false; // Track visibility state
    private String googleSearchUrl;

    @FXML
    public void initialize() {
        databaseHelper = new DatabaseHelper();

        // Lắng nghe sự kiện nhập liệu trong TextField
        suggestionsList.setVisible(false); // Ẩn ListView
        searchExploreField.textProperty().addListener((observable, oldValue, newValue) -> {
            showButton.setText("Hide");
            if (newValue != null && !newValue.isEmpty()) {
                updateSuggestions(newValue);
            } else {
                suggestionsList.getItems().clear(); // Xóa gợi ý khi không có nội dung
                suggestionsList.setVisible(false); // Ẩn ListView
            }
        });

        // Lắng nghe sự kiện chọn mục trong ListView
        suggestionsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadDocumentDetails(newValue); // Gọi phương thức để nạp thông tin tài liệu
                searchExploreField.setText(newValue);
                suggestionsList.setVisible(false); // Ẩn ListView sau khi chọn
                showButton.setText("Show");
            } else {
                System.out.println("Không có mục nào được chọn.");
            }
        });

        searchExploreField.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            String currentText = searchExploreField.getText();
            // Kiểm tra nếu không phải là dấu cách ở cuối
            if (!currentText.endsWith(" ")) {
                searchExploreField.setText(currentText + " "); // Thêm dấu cách
                // Đặt con trỏ ở cuối văn bản
                searchExploreField.positionCaret(currentText.length() + 1); // +1 để đặt con trỏ sau dấu cách
            }
            showButton.setText("Show");
        });

        myHyperlink.setOnAction(event -> handleHyperlinkAction());

        showButton.setOnAction(actionEvent -> handleShowButton());
        homeButton.setOnAction(actionEvent -> handleHomeButton());
        button1.setOnAction(actionEvent -> handleButton1());
        button3.setOnAction(actionEvent -> handleButton3());

        applySoundEffectsToButtons(root);
    }

    private void handleHomeButton() {
        changeScene("/com/example/library/main-view.fxml", "Live for Library");
    }

    private void handleButton1() {
        // Xử lý cho nút Button1
        System.out.println("Button1 clicked");
    }

    private void handleButton3() {
        // Xử lý cho nút Button3
        System.out.println("Button3 clicked");
    }

    private void handleShowButton() {
        isListViewVisible = !isListViewVisible; // Toggle visibility state
        suggestionsList.setVisible(isListViewVisible);
        suggestionsList.setManaged(isListViewVisible);
        showButton.setText(isListViewVisible ? "Hide" : "Show"); // Update button text
    }

    // Phương thức cập nhật gợi ý
    private void updateSuggestions(String query) {
        List<String> suggestions = databaseHelper.getSuggestions(query);
        ObservableList<String> observableSuggestions = FXCollections.observableArrayList(suggestions);

        // Cập nhật ListView với gợi ý mới
        suggestionsList.setItems(observableSuggestions);
        suggestionsList.setVisible(!observableSuggestions.isEmpty()); // Hiển thị ListView khi có gợi ý
        suggestionsList.setManaged(!observableSuggestions.isEmpty()); // Quản lý layout

        System.out.println("Số lượng gợi ý: " + observableSuggestions.size());
    }

    private void loadDocumentDetails(String title) {
        Document document = databaseHelper.getDocumentByTitle(title); // Lấy tài liệu theo tiêu đề

        if (document != null) {
            // Cập nhật các trường với thông tin tài liệu
            idField.setText(String.valueOf(document.getId()));
            titleField.setText(document.getTitle());
            authorField.setText(document.getAuthor());
            publicYearField.setText(String.valueOf(document.getPublicYear()));
            publisherField.setText(document.getPublisher());
            genreField.setText(document.getGenre());
            quantityField.setText(String.valueOf(document.getQuantity()));

            // Tải hình ảnh từ liên kết
            Image image = new Image(document.getImageLink(), true); // Tải hình ảnh bất đồng bộ
            myImageView.setImage(image);
            googleSearchUrl = "https://www.google.com/search?q=" + (title + " book").replace(" ", "+");
        } else {
            // Hiển thị thông báo lỗi cho người dùng
            System.out.println("Không tìm thấy tài liệu. Vui lòng thử với tiêu đề khác.");
        }
    }

    @FXML
    private void handleHyperlinkAction() {
        searchInGoogleByLink(googleSearchUrl);
    }

    private void searchInGoogleByLink(String googleSearchUrl) {
        try {
            if (googleSearchUrl != null) {
                Desktop.getDesktop().browse(new URI(googleSearchUrl));
                System.out.println(googleSearchUrl);
            } else {
                UpdateController.showWarningAlert("Vui lòng chọn nội dung tìm kiếm");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void changeScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button1.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}