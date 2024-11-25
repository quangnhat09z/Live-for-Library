package com.example.library.model;

import com.example.library.controller.ManageAccountController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;

public class DatabaseHelper {

  public static final String URL = "jdbc:mysql://localhost:3306/library";
  public static final String USER = "root"; // Thay bằng tên người dùng của bạn
  public static final String PASSWORD = "Anhphuoc1@"; // Thay bằng mật khẩu của bạn

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
    String insertSql = "INSERT INTO documents (title, author, publicYear, publisher, genre, quantity, imageLink) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
        insertStmt.setString(7, document.getImageLink());
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
        String imageLink = rs.getString("imageLink");
        documents.add(
                new Document(id, title, author, publicYear, publisher, genre, quantity, imageLink));
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

  public List<String> getSuggestions(String query) {
    List<String> suggestions = new ArrayList<>();
    String sql = "SELECT title FROM documents WHERE title LIKE ?"; // Thay đổi tên bảng nếu cần

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, "%" + query + "%");
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        suggestions.add(rs.getString("title"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return suggestions;
  }


  public Document getDocumentByTitle(String title) {
    Document document = null;
    String query = "SELECT * FROM documents WHERE title = ?";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(query)) {
      pstmt.setString(1, title);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        document = new Document();
        document.setId(rs.getInt("id"));
        document.setTitle(rs.getString("title"));
        document.setAuthor(rs.getString("author"));
        document.setPublicYear(String.valueOf(rs.getInt("publicYear")));
        document.setPublisher(rs.getString("publisher"));
        document.setGenre(rs.getString("genre"));
        document.setQuantity(rs.getInt("quantity"));
        document.setImageLink(rs.getString("imageLink"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return document;
  }

  public Document getDocumentById(int documentId) {
    Document document = null;
    String sql = "SELECT * FROM Documents WHERE id = ?";

    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, documentId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        document = new Document(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("publicYear"),
                rs.getString("publisher"),
                rs.getString("genre"),
                rs.getInt("quantity"),
                rs.getString("imageLink")
        );
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return document;
  }

  public Account getAccountByUserName(String userName) {
    Account account = null;
    String sql = "SELECT * FROM accounts WHERE username = ?";

    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, userName);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        account = new Account(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("role")
        );
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return account;
  }
  //Account
  public void addAccount(Account account, ManageAccountController controller) throws SQLException {
    String checkSql = "SELECT * FROM accounts WHERE username = ?";
    String checkSql1 = "SELECT * FROM accounts WHERE email = ?";
    String insertSql = "INSERT INTO accounts (username, password, email, role) VALUES (?, ?, ?, ?)";

    //checkSql : username
    //checkSql1: email
    try (Connection conn = connect();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql);
         PreparedStatement checkStmt1 = conn.prepareStatement(checkSql1);
         PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

      // Kiểm tra xem username đã tồn tại chưa
      checkStmt.setString(1, account.getUsername());
      checkStmt1.setString(1, account.getEmail());
      ResultSet rs = checkStmt.executeQuery();
      ResultSet rs1 = checkStmt1.executeQuery();

      if (rs.next()) {
        // Username đã tồn tại, hiển thị cảnh báo và xóa trường nhập liệu
        showWarningAlert("Username already exists, please enter another UserName");
        controller.clearUsernameField();
        System.out.println("Please enter a different username");
        throw new SQLException("Username already exists.");
      } else if (rs1.next()) {
        // Email đã tồn tại, hiển thị cảnh báo và xóa trường nhập liệu
        showWarningAlert("Email already exists, please enter another Email");
        controller.clearEmailField();
        System.out.println("Please enter a different Email");
        throw new SQLException("Email already exists.");
      } else {
        // Username chưa tồn tại, thêm tài khoản mới
        insertStmt.setString(1, account.getUsername());
        insertStmt.setString(2, account.getPassword());
        insertStmt.setString(3, account.getEmail());
        insertStmt.setString(4, account.getRole());
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
    String deleteSql1 = "DELETE FROM user_verification WHERE id= ?";
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
          System.out.println("Delete !");
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

  public List<Account> getAllAcounts() {
    List<Account> accounts = new ArrayList<>();
    String sql = "SELECT * FROM accounts";
    try (Connection conn = connect();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        String role = rs.getString("role");
        accounts.add(new Account(id, username, password, email, role));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      showWarningAlert("CAN NOT GET ALL ACCOUNTS");
    }
    return accounts;
  }

  public int getBorrowedQuantity(int userId, int documentId) {
    int quantity = 0; // Khởi tạo số lượng mặc định
    String sql = "SELECT quantityBorrow FROM borrow_return WHERE user_id = ? AND document_id = ?";

    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, userId);
      pstmt.setInt(2, documentId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        quantity = rs.getInt("quantityBorrow"); // Lấy số lượng từ kết quả
      }
    } catch (SQLException e) {
      e.printStackTrace(); // Xử lý ngoại lệ
    }
    return quantity; // Trả về số lượng mượn
  }

  private static void showWarningAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private static void showSuccessAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION); // Sử dụng INFORMATION
    alert.setTitle("Success");
    alert.setHeaderText("SUCCESS ADD ACCOUNT");
    alert.setContentText(message);
    alert.showAndWait();
  }

}











