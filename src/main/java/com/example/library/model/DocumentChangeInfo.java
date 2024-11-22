package com.example.library.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DocumentChangeInfo {
    public boolean updateDocument(int id, String title, String author, String publicYear, String publisher, String genre, int quantity, String imageLink) {
        String sql = "UPDATE documents SET title = ?, author = ?, publicYear = ?, publisher = ?, genre = ?, quantity = ?, imageLink = ? WHERE id = ?";
        try (Connection connection = DatabaseHelper.connect(); // Sử dụng DatabaseHelper
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, publicYear);
            pstmt.setString(4, publisher);
            pstmt.setString(5, genre);
            pstmt.setInt(6, quantity);
            pstmt.setString(7, imageLink);
            pstmt.setInt(8, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất 1 bản ghi được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }
}