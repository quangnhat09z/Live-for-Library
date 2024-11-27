package com.example.library.controller.mainScreen;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.library.model.Alert.showWarningAlert;

public class ListBorrowBooksController {
    @FXML
    private ListView<HBox> listView;

    @FXML
    public void initialize() {
        List<BorrowReturn> borrowReturns = fetchData();

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

                // Thay đổi Scene của Stage hiện tại
                Stage stage = (Stage) listView.getScene().getWindow();
                Scene scene = new Scene(root, 1300, 650);
                stage.setScene(scene); // Thay thế Scene hiện tại

                // Gọi phương thức với title
                if (borrowController != null) {
                    String title = selectedDocument.getTitle();
                    borrowController.loadDocumentDetails(title); // Gọi phương thức để tải chi tiết
                } else {
                    System.out.println("BorrowController chưa được khởi tạo.");
                }

                // Căn giữa cửa sổ (tùy chọn)
                stage.centerOnScreen(); // Căn giữa cửa sổ trên màn hình

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showWarningAlert("Vui lòng chọn một cuốn sách để mượn");
        }
    }


    private List<BorrowReturn> fetchData() {
        List<BorrowReturn> loans = new ArrayList<>();
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT imageLink, title, quantityBorrow, borrow_date, required_date FROM Borrow_Return")) {

            while (rs.next()) {
                loans.add(new BorrowReturn(
                        rs.getString("imageLink"),
                        rs.getString("title"),
                        rs.getInt("quantityBorrow"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("required_date").toLocalDate()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loans;
    }
}