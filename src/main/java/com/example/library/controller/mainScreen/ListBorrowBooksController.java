package com.example.library.controller.mainScreen;

import com.example.library.controller.login.Login_LoginController;
import com.example.library.model.Account;
import com.example.library.model.BorrowReturn;
import com.example.library.model.DatabaseHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.library.model.Alert.showWarningAlert;

public class ListBorrowBooksController {
    @FXML
    private ListView<HBox> listView;
    private DatabaseHelper databaseHelper;

    public ListBorrowBooksController() {
        // Khởi tạo databaseHelper
        this.databaseHelper = new DatabaseHelper(); // Đảm bảo khởi tạo đúng cách
    }

    @FXML
    public void initialize() {
        String username = Login_LoginController.usernameToBorrow;
        Account account = databaseHelper.getAccountByUserName(username);
        int user_id = account.getId();

        List<BorrowReturn> borrowReturns = fetchData(user_id);

        for (BorrowReturn loan : borrowReturns) {
            HBox hbox = new HBox(10); // Khoảng cách giữa các phần tử
            ImageView imageView = new ImageView(new Image(loan.getImageLink()));
            imageView.setFitHeight(100);
            imageView.setFitWidth(75);

            VBox infoBox = new VBox();
            infoBox.setAlignment(Pos.CENTER_LEFT); // Căn giữa các thông tin theo chiều dọc

            // Tạo tiêu đề
            Label titleLabel = new Label(loan.getTitle());
            titleLabel.setStyle("-fx-font-weight: bold;"); // In đậm tiêu đề

            // Tạo các HBox cho thông tin
            HBox quantityBox = createInfoRow("Quantity:", String.valueOf(loan.getQuantityBorrow()));
            HBox borrowDateBox = createInfoRow("Borrow Date:", loan.getBorrowDate().toString());
            HBox requiredDateBox = createInfoRow("Due Date:", loan.getRequiredDate().toString());

            // Thêm tiêu đề và các thông tin vào VBox
            infoBox.getChildren().addAll(titleLabel, quantityBox, borrowDateBox, requiredDateBox);

            hbox.getChildren().addAll(imageView, infoBox);
            hbox.setAlignment(Pos.CENTER_LEFT); // Căn giữa theo chiều ngang cho HBox
            listView.getItems().add(hbox);

            // Xử lý sự kiện nhấp đúp chuột
            hbox.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    openBorrowReturnView(loan); // Gọi hàm mở view với loan hiện tại
                }
            });
        }
    }

    private HBox createInfoRow(String label, String value) {
        Label infoLabel = new Label(label);
        infoLabel.setPrefWidth(80); // Đặt chiều rộng cố định cho nhãn
        Label valueLabel = new Label(value);
        HBox row = new HBox(infoLabel, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void openBorrowReturnView(BorrowReturn selectedDocument) {
        if (selectedDocument != null) {
            try {
                // Tải FXML cho BorrowReturn-view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/borrowReturn-view.fxml"));
                Parent root = loader.load();

                // Lấy controller từ FXMLLoader
                BorrowController borrowController = loader.getController();

                // Lấy Stage hiện tại nơi listView đang được hiển thị
                Stage currentStage = (Stage) listView.getScene().getWindow();

                // Nếu bạn đang làm việc với một cửa sổ mới và muốn trở về cửa sổ chính,
                // bạn sẽ cần một tham chiếu đến cửa sổ chính.
                // Giả sử bạn lưu trữ một tham chiếu đến cửa sổ chính trong một biến static hoặc pass nó dưới dạng tham số tới controller này.

                // Tạo một cửa sổ chính mới từ một phương thức static hoặc truyền đến cửa sổ chính
                Stage primaryStage = MainController.primaryStage; // Giả thuyết là bạn lưu cửa sổ chính vào Main class

                // Thiết lập Scene mới cho cửa sổ chính
                Scene scene = new Scene(root, 1300, 650);
                primaryStage.setScene(scene);

                // Gọi phương thức với title
                if (borrowController != null) {
                    String title = selectedDocument.getTitle();
                    borrowController.loadDocumentDetails(title); // Gọi phương thức để tải chi tiết
                } else {
                    System.out.println("BorrowController chưa được khởi tạo.");
                }

                Screen screen = Screen.getPrimary();
                double screenWidth = screen.getVisualBounds().getWidth();
                double screenHeight = screen.getVisualBounds().getHeight();
                double stageWidth = primaryStage.getWidth();
                double stageHeight = primaryStage.getHeight();

                // center position
                primaryStage.setX((screenWidth - stageWidth) / 2);
                primaryStage.setY((screenHeight - stageHeight) / 2);

                // Đóng cửa sổ hiện tại (cửa sổ mới mở)
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showWarningAlert("Vui lòng chọn một cuốn sách để mượn");
        }
    }


    private List<BorrowReturn> fetchData(int userId) {
        List<BorrowReturn> loans = new ArrayList<>();
        String query = "SELECT imageLink, title, quantityBorrow, borrow_date, required_date FROM Borrow_Return WHERE user_id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Thiết lập giá trị cho tham số userId
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(new BorrowReturn(
                            rs.getString("imageLink"),
                            rs.getString("title"),
                            rs.getInt("quantityBorrow"),
                            rs.getDate("borrow_date").toLocalDate(),
                            rs.getDate("required_date").toLocalDate()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loans;
    }
}