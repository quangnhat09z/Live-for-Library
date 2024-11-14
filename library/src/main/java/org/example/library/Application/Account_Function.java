package org.example.library.Application;

import static org.example.library.Controller.ManageDocumentController.showSuccessAlert;
import static org.example.library.Controller.ManageDocumentController.showWarningAlert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Account_Function extends DatabaseHelper {

  public void addAccount(Account account) throws SQLException {
    String checkSql = "SELECT * FROM accounts WHERE username = ?";
    String insertSql = "INSERT INTO accounts (id, username, password, role) VALUES (?, ?, ?, ?)";

    try (Connection conn = connect();
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

      // Kiểm tra xem username đã tồn tại chưa
      checkStmt.setString(1, account.getUsername());
      ResultSet rs = checkStmt.executeQuery();

      if (rs.next()) {
        // Username đã tồn tại, hiển thị cảnh báo
        showWarningAlert("Username already exists, please enter other UserName");
      } else {
        // Username chưa tồn tại, thêm tài khoản mới
        insertStmt.setInt(1, account.getId());
        insertStmt.setString(2, account.getUsername());
        insertStmt.setString(3, account.getPassword());
        insertStmt.setString(4, account.getRole());
        insertStmt.executeUpdate();
        showSuccessAlert("Account added successfully.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      showWarningAlert("Failed to add Account.");
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
          showSuccessAlert("Delete successfully");
        } else {
          showWarningAlert("Account not exists !");
        }
      } else {
        showWarningAlert("Account not exists !");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      showWarningAlert("Have some problems when delete Account");
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
        String role = rs.getString("role");
        accounts.add(new Account(id, username, password, role));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      showWarningAlert("Cann't getAllAcounts");
    }
    return accounts;
  }

}
