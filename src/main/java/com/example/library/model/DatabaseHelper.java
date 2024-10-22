package com.example.library.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/lib";
    private static final String USER = "root"; // Thay bằng tên người dùng của bạn
    private static final String PASSWORD = "09022005"; // Thay bằng mật khẩu của bạn

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
            return null;
        }
    }

    public void addDocument(Document document) throws SQLException {
        String checkSql = "SELECT * FROM docs WHERE title = ?";
        String updateSql = "UPDATE docs SET quantity = quantity + ? WHERE title = ?";
        String insertSql = "INSERT INTO docs (title, author, publicYear, publisher, genre, quantity) VALUES (?, ?, ?, ?, ?, ?)";

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
        String sql = "SELECT * FROM docs";

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

    public void deleteDocument(int id) {
        // SQL statements
        String checkSql = "SELECT quantity FROM docs WHERE id = ?";
        String updateSql = "UPDATE docs SET quantity = quantity - 1 WHERE id = ?";
        String deleteSql = "DELETE FROM docs WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            // Check the current quantity
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int quantity = rs.getInt("quantity");

                if (quantity > 1) {
                    // If quantity is greater than 1, decrease it by 1
                    updateStmt.setInt(1, id);
                    updateStmt.executeUpdate();
                } else {
                    // If quantity is 1, delete the document
                    deleteStmt.setInt(1, id);
                    deleteStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}