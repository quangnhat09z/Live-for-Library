package com.example.library.controller.mainScreen;

import com.example.library.model.ChangeView;
import com.example.library.model.DatabaseHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static com.example.library.model.DatabaseHelper.connect;
import static com.example.library.model.SoundUtil.applySoundEffectsToButtons;

public class MainController extends Controller {
    @FXML
    private HBox root;
    @FXML
    private TextArea speech;
    @FXML
    private Label bookCount;
    @FXML
    private Label accountCount;
    @FXML
    private Label borrowedCount;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private ImageView bookImageView;
    @FXML
    private Button bookButton;
    @FXML
    private Button exploreButton;
    @FXML
    private Button gameButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button adminButton;
    @FXML
    private Button borrowedButton;
    @FXML
    private Button settingButton;
    @FXML
    private Button userButton;
    @FXML
    private Pane userActionPane;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button paneQuitButton;

    public static Stage primaryStage;
    private static MediaPlayer mediaPlayer;

    @Override
    public void initialize() {
        setSpeech("src/main/resources/quote.txt");
        bookButton.setOnAction(actionEvent -> handleBookButton());
        exploreButton.setOnAction(actionEvent -> handleExploreButton());
        gameButton.setOnAction(actionEvent -> handleGameButton());
        if (!isAdmin()) {
            adminButton.setVisible(false);
        } else {
            adminButton.setOnAction(actionEvent -> handleAdminButton());
        }
        quitButton.setOnAction(actionEvent -> handleQuitButton());
        borrowedButton.setOnAction(actionEvent -> handleBorrowedButton());
        settingButton.setOnAction(actionEvent -> handleSettingButton());
        userButton.setOnAction(actionEvent -> handleUserButton());
        logoutButton.setOnAction(actionEvent -> handleLogout());
        paneQuitButton.setOnAction(actionEvent -> handleQuitButton());

        setUsername();
        setBookCount();
        setAccountCount();
        setBorrowedCount();
        setBookInfo();

        if (Controller.isDarkMode()) {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/dark_main.css")).toExternalForm());

        } else {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/main.css")).toExternalForm());
        }

        String musicFile = "src/main/resources/audio/music.mp3";
        Media sound = new Media(Paths.get(musicFile).toUri().toString());
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
            mediaPlayer.setVolume(0);
        } else if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            mediaPlayer.play();
        }

        applySoundEffectsToButtons(root);
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        MainController.mediaPlayer = mediaPlayer;
    }

    private void handleAdminButton() {
        changeScene("/com/example/library/manage-documents-view.fxml", "Update");
    }

    private void handleQuitButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to quit?");

        // Thêm nút xác nhận và hủy
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Xử lý kết quả
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            Stage stage = (Stage) quitButton.getScene().getWindow();
            stage.close();
        }
    }

    // Tạo một view tên như dưới, sau khi xong thì bỏ comment này và mở hàm changeScene.
    private void handleBorrowedButton() {
        try {
            if (primaryStage == null) {
                primaryStage = (Stage) root.getScene().getWindow();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/borrowed-books-view.fxml"));
            Parent root = loader.load();

            // Tạo một cửa sổ mới
            Stage borrowedBooksStage = new Stage();
            borrowedBooksStage.setTitle("Borrowed Books");
            borrowedBooksStage.setScene(new Scene(root, 400, 600)); // Kích thước cửa sổ mới

            // Căn giữa cửa sổ mới trên màn hình
            borrowedBooksStage.setOnShown(event -> {
                borrowedBooksStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - borrowedBooksStage.getWidth()) / 2);
                borrowedBooksStage.setY((Screen.getPrimary().getVisualBounds().getHeight() - borrowedBooksStage.getHeight()) / 2);
            });

            borrowedBooksStage.show(); // Hiển thị cửa sổ
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSettingButton() {
//        changeScene("/com/example/library/settings-view.fxml", "Settings");
        Stage stage = (Stage) settingButton.getScene().getWindow();
        ChangeView.changeViewFXML("/com/example/library/settings-view.fxml", stage);
    }

    private void handleUserButton() {
        // Toggle the visibility of the userActionPane
        boolean isVisible = userActionPane.isVisible();
        userActionPane.setVisible(!isVisible);
    }

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        // Thêm nút xác nhận và hủy
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Xử lý kết quả
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            changeScene("/com/example/library/login-login-view.fxml", "Login", 565, 471);
        }
    }

    private void setUsername() {
        // Implement your method to get the username
        String username = "Username";

        int id = SettingsController.getId();
        String query = "select username from accounts where id = ?";

        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(query)) {

            checkStmt.setString(1, String.valueOf(id));
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                username = resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Controller.isAdmin()) {
            username += " (Admin)";
        }
        usernameLabel.setText(username);
    }

    private void setSpeech(String filePath) {
        try {
            String randomQuote = null;
            List<String> quotes = Files.readAllLines(Paths.get(filePath));
            while (randomQuote == null) {
                Random random = new Random();
                int randomIndex = random.nextInt(quotes.size());
                randomQuote = quotes.get(randomIndex);
            }
            System.out.println(randomQuote);
            speech.setText(randomQuote);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBookCount() {
        String query = "SELECT COUNT(*) AS count FROM documents";

        try (Connection conn = DriverManager.getConnection(DatabaseHelper.URL, DatabaseHelper.USER, DatabaseHelper.PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                int count = rs.getInt("count");
                bookCount.setText(String.valueOf(count));
            }

        } catch (Exception e) {
            e.printStackTrace();
            bookCount.setText("");
            System.out.println("Không thể lấy số lượng sách");
        }
    }

    private void setAccountCount() {

        String query = "SELECT COUNT(*) AS count FROM accounts";

        try (Connection conn = DriverManager.getConnection(
                DatabaseHelper.URL, DatabaseHelper.USER, DatabaseHelper.PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                int count = rs.getInt("count");
                accountCount.setText(String.valueOf(count));
            }

        } catch (Exception e) {
            e.printStackTrace();
            bookCount.setText("");
            System.out.println("Không thể lấy số lượng tài khoản");
        }
    }

    private void setBorrowedCount() {
        String query = "SELECT IFNULL(SUM(quantityBorrow), 0) AS sum FROM Borrow_Return WHERE user_id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, SettingsController.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("sum");
                borrowedCount.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            borrowedCount.setText("");
            System.out.println("Không thể lấy số lượng sách đã mượn");
        }
    }

    private void setBookInfo() {
        // String query = "SELECT * FROM documents ORDER BY RAND() LIMIT 1";
        String query = "select * FROM documents WHERE id BETWEEN 100000 AND 101775 ORDER BY RAND() LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DatabaseHelper.URL, DatabaseHelper.USER, DatabaseHelper.PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publishYear = rs.getString("publicYear");

                titleLabel.setText(title);
                authorLabel.setText(author);
                yearLabel.setText(publishYear);

                String imageLink = rs.getString("imageLink");
                if (imageLink != null && !imageLink.isEmpty()) {
                    Image image = new Image(imageLink);
                    bookImageView.setImage(image);
                } else {
                    bookImageView.setImage(null);
                    System.out.println("Couldn't load image");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            bookCount.setText("");
            System.out.println("BookInfo Error");
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

    protected void changeScene(String fxmlPath, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) bookButton.getScene().getWindow();
            Scene scene = new Scene(root, width, height);
            Screen screen = Screen.getPrimary();
            double screenWidth = screen.getVisualBounds().getWidth();
            double screenHeight = screen.getVisualBounds().getHeight();
            stage.setX((screenWidth - width) / 2);
            stage.setY((screenHeight - height) / 2);
            stage.show();

            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
