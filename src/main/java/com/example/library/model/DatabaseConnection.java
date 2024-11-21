package com.example.library.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

  private Connection databaseLink;

  public Connection getConnection() {
    String databaseName = "library";
    String databaseUser = "root";
    String databasePassword = "Lequangmien10";
    String url = "jdbc:mysql://localhost/" + databaseName;

    try {
      // Load MySQL JDBC Driver (optional for JDBC 4.0+)
      Class.forName("com.mysql.cj.jdbc.Driver");

      // Establish the connection
      databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
      System.out.println("Database connection successful!");

    } catch (ClassNotFoundException e) {
      System.out.println("MySQL JDBC Driver not found!");
      e.printStackTrace();
    } catch (SQLException e) {
      System.out.println("Error connecting to the database!");
      e.printStackTrace();
    }

    return databaseLink;
  }


}
