package com.example.library.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountChangeInfor {
  public boolean updateAccount(
      int id, String username, String password, String email,
      String fullName, String address, String role,
      String phoneNumber, String status) {

    String updateAccountsSQL = "UPDATE accounts SET username = ?, password = ?, email = ?, role = ?, status = ? WHERE id = ?";
    String updateUserVerificationSQL = "UPDATE user_verification SET full_name = ?, address = ?, phone_number = ? WHERE id = ?";

    try (Connection connection = DatabaseHelper.connect()) {
      // Disable auto-commit to manage transaction manually
      connection.setAutoCommit(false);

      // Update the accounts table
      try (PreparedStatement pstmtAccounts = connection.prepareStatement(updateAccountsSQL)) {
        pstmtAccounts.setString(1, username);
        pstmtAccounts.setString(2, password);
        pstmtAccounts.setString(3, email);
        pstmtAccounts.setString(4, role);
        pstmtAccounts.setString(5, status);
        pstmtAccounts.setInt(6, id);
        pstmtAccounts.executeUpdate();
      }

      // Update the user_verification table
      try (PreparedStatement pstmtUserVerification = connection.prepareStatement(updateUserVerificationSQL)) {
        pstmtUserVerification.setString(1, fullName);
        pstmtUserVerification.setString(2, address);
        pstmtUserVerification.setString(3, phoneNumber);
        pstmtUserVerification.setInt(4, id);
        pstmtUserVerification.executeUpdate();
      }

      // Commit transaction
      connection.commit();
      return true;

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

}
