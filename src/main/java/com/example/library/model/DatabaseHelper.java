package com.example.library.model;

import static com.example.library.controller.UpdateController.showSuccessAlert;
import static com.example.library.controller.UpdateController.showWarningAlert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

}











