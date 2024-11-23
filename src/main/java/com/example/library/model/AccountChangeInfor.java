package com.example.library.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountChangeInfor {
  public boolean updateAccount(int id,String username, String password, String email, String fullName, String address, String role, String phoneNumber, String status) {
    String sql = "UPDATE accounts SET username = ?, password = ?, email = ?, "
        + "full_name = ?, address = ?, role = ?, phone_number = ?, status = ? "
        + "WHERE id = ?";
    try (Connection connection = DatabaseHelper.connect(); // Sử dụng DatabaseHelper
        PreparedStatement pstmt = connection.prepareStatement(sql)) {

      pstmt.setString(1, username);
      pstmt.setString(2, password);
      pstmt.setString(3, email);
      pstmt.setString(4, fullName);
      pstmt.setString(5, address);
      pstmt.setString(6, role);
      pstmt.setString(7, phoneNumber);
      pstmt.setString(8, status);
      pstmt.setInt(9, id); // Gán ID cho tham số cuối cùng

      int rowsAffected = pstmt.executeUpdate();
      return rowsAffected > 0; // Trả về true nếu có ít nhất 1 bản ghi được cập nhật
    } catch (SQLException e) {
      e.printStackTrace();
      return false; // Trả về false nếu có lỗi xảy ra
    }
  }
}
