package com.example.library.controller;

import com.example.library.model.DatabaseHelper;
import com.example.library.model.Document;
import com.example.library.model.GoogleBookAPI;
import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.library.model.SoundUtil.applySoundEffectsToButtons;

public class APIController extends Controller{
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
    private Label descriptionField;
    @FXML
    private Hyperlink myHyperlink;

    @FXML
    private Button showButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button bookButton;
    @FXML
    private Button gameButton;


    private boolean isListViewVisible = false; // Track visibility state
    private String googleSearchUrl;

    @FXML
    public void initialize() {
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
                // Không đặt lại giá trị của searchExploreField
                suggestionsList.setVisible(false); // Ẩn ListView sau khi chọn
                showButton.setText("Show");
            } else {
                System.out.println("Không có mục nào được chọn.");
            }
        });

        // Thêm sự kiện cho việc nhấp chuột để ẩn danh sách gợi ý
        searchExploreField.setOnMouseClicked(event -> {
            if (suggestionsList.isVisible()) {
                suggestionsList.setVisible(false); // Ẩn nếu đã hiển thị
            }
        });
        myHyperlink.setOnAction(event -> handleHyperlinkAction());

        homeButton.setOnAction(actionEvent -> handleHomeButton());
        bookButton.setOnAction(actionEvent -> handleBookButton());
        gameButton.setOnAction(actionEvent -> handleGameButton());

        if (Controller.isDarkMode()) {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/dark_explore.css")).toExternalForm());
        } else {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/explore.css")).toExternalForm());
        }

        applySoundEffectsToButtons(root);
    }

    @FXML
    private void onToggleListView() {
        isListViewVisible = !isListViewVisible; // Toggle visibility state
        suggestionsList.setVisible(isListViewVisible);
        suggestionsList.setManaged(isListViewVisible);
        showButton.setText(isListViewVisible ? "Hide" : "Show"); // Update button text
    }

    private void updateSuggestions(String query) {
        // Tạo một Task để gọi API
        Task<List<String>> task = new Task<>() {
            protected List<String> call() throws Exception {
                // Gọi API để tìm kiếm sách và nhận phản hồi JSON
                String jsonResponse = GoogleBookAPI.searchBooks(query);
                // Phân tích JSON để lấy danh sách tiêu đề
                return extractTitlesFromJson(jsonResponse);
            }

            protected void succeeded() {
                try {
                    // Lấy gợi ý từ kết quả
                    List<String> suggestions = get();
                    ObservableList<String> observableSuggestions = FXCollections.observableArrayList(suggestions);

                    // Cập nhật ListView với gợi ý mới
                    suggestionsList.setItems(observableSuggestions);
                    suggestionsList.setVisible(!observableSuggestions.isEmpty());
                    suggestionsList.setManaged(!observableSuggestions.isEmpty());
                } catch (Exception e) {
                    e.printStackTrace();
                    // Hiển thị thông báo lỗi cho người dùng
                }
            }

            @Override
            protected void failed() {
                System.out.println("fail");
            }
        };

        // Chạy Task trong một luồng riêng
        new Thread(task).start();
    }

    // Phương thức để trích xuất tiêu đề từ chuỗi JSON
    private List<String> extractTitlesFromJson(String jsonResponse) {
        List<String> titles = new ArrayList<>();
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray items = jsonObject.getAsJsonArray("items");

            if (items != null) {
                for (JsonElement item : items) {
                    JsonObject volumeInfo = item.getAsJsonObject().getAsJsonObject("volumeInfo");
                    String title = volumeInfo.get("title").getAsString();
                    titles.add(title);
                }
            }
        } catch (JsonSyntaxException | IllegalStateException e) {
            System.out.println("Lỗi khi phân tích JSON: " + e.getMessage());
        }
        return titles;
    }


    public Document parseDocumentFromJson(String jsonResponse) {
        try {
            // In ra nội dung JSON
            //System.out.println("JSON Response: " + jsonResponse);
            // Kiểm tra chuỗi JSON
            if (jsonResponse == null || jsonResponse.isEmpty()) {
                System.out.println("JSON không hợp lệ: Chuỗi rỗng hoặc null.");
                return null;
            }
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

            // Lấy mảng "items"
            JsonArray items = jsonObject.getAsJsonArray("items");
            if (items != null && items.size() > 0) {
                JsonObject book = items.get(0).getAsJsonObject(); // Lấy cuốn sách đầu tiên
                JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");

                // Trích xuất thông tin cần thiết
                String title = volumeInfo.get("title").getAsString();
                String author = volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : "Unknown";
                String publicYear = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString().split("-")[0] : "Unknown";
                String publisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "Unknown";
                String genre = volumeInfo.has("categories") ? volumeInfo.getAsJsonArray("categories").get(0).getAsString() : "Unknown";
                String imageLink = volumeInfo.has("imageLinks") ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString() : "";
                String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "N/A"; // Sửa đổi tại đây

                return new Document(title, author, publicYear,publisher , genre,  imageLink, description);
            } else {
                System.out.println("Không tìm thấy tài liệu nào trong phản hồi.");
            }
        } //catch (JsonSyntaxException e) {
            //System.out.println("Lỗi cú pháp JSON: " + e.getMessage());
         catch (IllegalStateException e) {
            System.out.println("Lỗi khi truy cập JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi khi phân tích JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Không tìm thấy tài liệu hoặc có lỗi phân tích
    }

    private void loadDocumentDetails(String title) {
        try {
            String jsonData = String.valueOf(GoogleBookAPI.searchBooks(title)); // Gọi API để tìm sách
            Document document = parseDocumentFromJson(jsonData); // Phân tích JSON để tạo đối tượng Document

            if (document != null) {
                // Cập nhật các trường với thông tin tài liệu
                titleField.setText(document.getTitle());
                authorField.setText(document.getAuthor());
                publicYearField.setText(String.valueOf(document.getPublicYear()));
                publisherField.setText(document.getPublisher());
                genreField.setText(document.getGenre());
                descriptionField.setText(document.getDescription());

                // Cập nhật hình ảnh document.getImageLink()
                String imageLink = document.getImageLink();
                if (imageLink == null || imageLink.isEmpty()) {
                    SearchController.showWarningAlert("Không có hình ảnh");
                } else {
                    Image image = new Image(imageLink, true);
                    myImageView.setImage(image);
                    // myImageView.setSmooth(true); // Làm mịn nếu cần
                }

                googleSearchUrl = "https://www.google.com/search?q=" + (title + " book").replace(" ", "+");
            } else {
                // Hiển thị thông báo lỗi cho người dùng
                //System.out.println("Không tìm thấy tài liệu. Vui lòng thử với tiêu đề khác.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra chi tiết lỗi
            // Hiển thị thông báo lỗi cho người dùng
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
               SearchController.showWarningAlert("Vui lòng chọn nội dung tìm kiếm");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void changeScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) bookButton.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}