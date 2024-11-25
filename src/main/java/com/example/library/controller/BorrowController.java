package com.example.library.controller;

import com.example.library.model.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.example.library.model.DatabaseHelper;
import com.example.library.model.Document;

import java.awt.Desktop;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.library.model.SoundUtil.applySoundEffectsToButtons;

public class BorrowController extends Controller {
    @FXML
    private HBox root;
    @FXML
    private TextField searchExploreField;
    @FXML
    private ListView<String> suggestionsList;
    @FXML
    private ImageView myImageView;
    @FXML
    private TextField documentIdField;
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
    private Button bookButton;
    @FXML
    private Button gameButton;
    @FXML
    private Button borrowButton;
    @FXML
    private Button returnButton;


    private DatabaseHelper databaseHelper;

    public BorrowController() {
        // Khởi tạo databaseHelper
        this.databaseHelper = new DatabaseHelper(); // Đảm bảo khởi tạo đúng cách
    }


    private boolean isListViewVisible = false; // Track visibility state
    private String googleSearchUrl;

    @Override
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
        bookButton.setOnAction(actionEvent -> handleBookButton());
        gameButton.setOnAction(actionEvent -> handleGameButton());
        borrowButton.setOnAction(actionEvent -> handleBorrowButton());
        returnButton.setOnAction(actionEvent -> handleReturnButton());

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

    public void loadDocumentDetails(String title) {
        Document document = databaseHelper.getDocumentByTitle(title); // Lấy tài liệu theo tiêu đề
        if (document != null) {
            // Cập nhật các trường với thông tin tài liệu
            documentIdField.setText(String.valueOf(document.getId()));
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
                ManageDocumentsController.showWarningAlert("Vui lòng chọn nội dung tìm kiếm");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleBorrowButton() {
        String username = Login_LoginController.usernameToBorrow;
        Account account = databaseHelper.getAccountByUserName(username);

        int userId = account.getId();
        int documentId = Integer.parseInt(documentIdField.getText());
        LocalDate borrowDate = LocalDate.now();

        // Hiện thị hộp thoại lấy số lượng
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nhập số lượng mượn");
        dialog.setHeaderText("Nhập số lượng bạn muốn mượn:");
        dialog.setContentText("Số lượng:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int quantityToBorrow = Integer.parseInt(result.get());

                Document document = databaseHelper.getDocumentById(documentId);
                if (document != null) {
                    int currentQuantity = document.getQuantity();
                    if (currentQuantity >= quantityToBorrow) {
                        String sqlBorrow = "INSERT INTO Borrow_Return (user_id, document_id, borrow_date, quantityBorrow) VALUES (?, ?, ?, ?)";
                        String sqlUpdateQuantity = "UPDATE Documents SET quantity = quantity - ? WHERE id = ?";

                        try (Connection conn = databaseHelper.connect();
                             PreparedStatement pstmtBorrow = conn.prepareStatement(sqlBorrow);
                             PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdateQuantity)) {

                            // Thêm thông tin trả sách
                            pstmtBorrow.setInt(1, userId);
                            pstmtBorrow.setInt(2, documentId);
                            pstmtBorrow.setDate(3, Date.valueOf(borrowDate));
                            pstmtBorrow.setInt(4, quantityToBorrow); // Record the borrowed quantity
                            pstmtBorrow.executeUpdate();

                            // Cập nhật số lượng tài liệu
                            pstmtUpdate.setInt(1, quantityToBorrow);
                            pstmtUpdate.setInt(2, documentId);
                            pstmtUpdate.executeUpdate();

                            System.out.println("Mượn sách thành công!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Số lượng mượn vượt quá số lượng có sẵn.");
                    }
                } else {
                    System.out.println("Không tìm thấy tài liệu.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số lượng hợp lệ.");
            }
        }
    }

    private void handleReturnButton() {
        String username = Login_LoginController.usernameToBorrow;
        Account account = databaseHelper.getAccountByUserName(username);

        int userId = account.getId();
        int documentId = Integer.parseInt(documentIdField.getText());

        // Hiển thị hộp thoại yêu cầu nhập số lượng
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nhập số lượng trả");
        dialog.setHeaderText("Nhập số lượng bạn muốn trả:");
        dialog.setContentText("Số lượng:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int quantityToReturn = Integer.parseInt(result.get());

                // Lấy thông tin tài liệu để kiểm tra số lượng đã mượn
                Document document = databaseHelper.getDocumentById(documentId);
                if (document != null) {
                    // Giả sử bạn có một phương thức để lấy số lượng đã mượn của người dùng
                    int borrowedQuantity = databaseHelper.getBorrowedQuantity(userId, documentId); // Phương thức này cần được định nghĩa

                    if (borrowedQuantity >= quantityToReturn) {
                        String sqlUpdateReturnDate = "UPDATE borrow_return SET return_date = NOW(), quantityBorrow = quantityBorrow - ? " +
                                "WHERE user_id = ? AND document_id = ?";
                        String sqlDeleteReturn = "DELETE FROM borrow_return WHERE user_id = ? AND document_id = ?";
                        String sqlUpdateQuantity = "UPDATE Documents SET quantity = quantity + ? WHERE id = ?";

                        try (Connection conn = databaseHelper.connect();
                             PreparedStatement pstmtUpdateDate = conn.prepareStatement(sqlUpdateReturnDate);
                             PreparedStatement pstmtDelete = conn.prepareStatement(sqlDeleteReturn);
                             PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdateQuantity)) {

                            if (borrowedQuantity == quantityToReturn) {
                                // Xóa thông tin trả sách vì đã trả đủ số lượng
                                pstmtDelete.setInt(1, userId);
                                pstmtDelete.setInt(2, documentId);
                                pstmtDelete.executeUpdate();
                            } else {
                                // Cập nhật ngày trả sách
                                pstmtUpdateDate.setInt(1, quantityToReturn);
                                pstmtUpdateDate.setInt(2, userId);
                                pstmtUpdateDate.setInt(3, documentId);
                                pstmtUpdateDate.executeUpdate();
                            }

                            // Cập nhật số lượng tài liệu
                            pstmtUpdate.setInt(1, quantityToReturn);
                            pstmtUpdate.setInt(2, documentId);
                            pstmtUpdate.executeUpdate();

                            SearchController.showInfoAlert("Thành công!", "Thông báo", "Trả sách thành công");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            SearchController.showWarningAlert("Đã xảy ra lỗi khi trả sách.");
                        }
                    } else {
                        SearchController.showWarningAlert("Số lượng trả vượt quá số lượng đã mượn.");
                    }
                } else {
                    SearchController.showWarningAlert("Không tìm thấy tài liệu.");
                }
            } catch (NumberFormatException e) {
                SearchController.showWarningAlert("Vui lòng nhập một số hợp lệ.");
            }
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