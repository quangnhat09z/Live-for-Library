package org.example.library.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.library.Controller.ManageAccountController;


public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root"; // Thay bằng tên người dùng của bạn
    private static final String PASSWORD = "Anhphuoc1@"; // Thay bằng mật khẩu của bạn

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
            return null;
        }
    }

    public void addDocument(Document document) throws SQLException {
        String checkSql = "SELECT * FROM documents WHERE title = ?";
        String updateSql = "UPDATE documents SET quantity = quantity + ? WHERE title = ?";
        String insertSql = "INSERT INTO documents (title, author, publicYear, publisher, genre, quantity) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Kiểm tra xem tài liệu có tồn tại không
            checkStmt.setString(1, document.getTitle());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Tài liệu đã tồn tại, cập nhật số lượng
                updateStmt.setInt(1, document.getQuantity()); // Số lượng cần thêm vào
                updateStmt.setString(2, document.getTitle());
                updateStmt.executeUpdate();
            } else {
                // Tài liệu chưa tồn tại, thêm tài liệu mới
                insertStmt.setString(1, document.getTitle());
                insertStmt.setString(2, document.getAuthor());
                insertStmt.setString(3, document.getPublicYear());
                insertStmt.setString(4, document.getPublisher());
                insertStmt.setString(5, document.getGenre());
                insertStmt.setInt(6, document.getQuantity()); // Số lượng ban đầu
                insertStmt.executeUpdate();
            }
        }
    }

    public List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents";

        try (Connection conn = connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publicYear = rs.getString("publicYear");
                String publisher = rs.getString("publisher");
                String genre = rs.getString("genre");
                int quantity = rs.getInt("quantity");
                documents.add(new Document(id, title, author, publicYear, publisher, genre, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public void deleteDocument(int id, int quantityToDelete) {
        // SQL statements
        String checkSql = "SELECT quantity FROM documents WHERE id = ?";
        String updateSql = "UPDATE documents SET quantity = quantity - ? WHERE id = ?";
        String deleteSql = "DELETE FROM documents WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            // Check the current quantity
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int currentQuantity = rs.getInt("quantity");
                if (currentQuantity > 0) {
                    if (currentQuantity > quantityToDelete) {
                        // Nếu số lượng hiện tại lớn hơn số lượng cần xóa, chỉ cần giảm đi
                        updateStmt.setInt(1, quantityToDelete);
                        updateStmt.setInt(2, id);
                        updateStmt.executeUpdate();
                        showSuccessAlert("Đã xóa " + quantityToDelete + " tài liệu.");
                    } else {
                        // Nếu số lượng hiện tại nhỏ hơn hoặc bằng số lượng cần xóa, xóa tài liệu
                        deleteStmt.setInt(1, id);
                        deleteStmt.executeUpdate();
                        showSuccessAlert("Đã xóa " + currentQuantity + " tài liệu.");
                    }
                } else {
                    showWarningAlert("Không có tài liệu nào để xóa.");
                }
            } else {
                showWarningAlert("Tài liệu không tồn tại.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showWarningAlert("Đã xảy ra lỗi khi xóa tài liệu.");
        }
    }



    //Account
    public void addAccount(Account account, ManageAccountController controller) throws SQLException {
        String checkSql = "SELECT * FROM accounts WHERE username = ?";
        String insertSql = "INSERT INTO accounts (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = connect();
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Kiểm tra xem username đã tồn tại chưa
            checkStmt.setString(1, account.getUsername());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Username đã tồn tại, hiển thị cảnh báo và xóa trường nhập liệu
                ManageAccountController.showWarningAlert("Username already exists, please enter another UserName");
                controller.clearUsernameField();
                System.out.println("Please enter a different username");
                throw new SQLException("Username already exists.");
            } else {
                // Username chưa tồn tại, thêm tài khoản mới
                insertStmt.setString(1, account.getUsername());
                insertStmt.setString(2, account.getPassword());
                insertStmt.setString(3, account.getRole());
                insertStmt.executeUpdate();
                System.out.println("Account added successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add Account.");
            throw e;
        }
    }




    public void deleteAccount(int id) throws SQLException {
        String checkSql = "SELECT * FROM accounts WHERE id = ?";
        String deleteSql = "DELETE FROM accounts WHERE id= ?";
        try (Connection conn = connect();
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                //Account exists
                //=> Can delete
                deleteStmt.setInt(1, id);
                int affectedRows = deleteStmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Delete successfully");
                } else {
                    System.out.println("Account not exists !");
                }
            } else {
                System.out.println("Account not exists !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Have some problems when delete Account");
        }
    }

    public List<Account> getAllAcounts()  {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                accounts.add(new Account(id, username, password, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showWarningAlert("Cann't getAllAcounts");
        }
        return accounts;
    }
    public static void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Sử dụng INFORMATION
        alert.setTitle("Success");
        alert.setHeaderText("SUCCESS ADD ACCOUNT");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInfoAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait(); // Hiển thị và chờ người dùng đóng
    }
}