package org.example.library.Application;

public class Account {

  private int id;
  private String username;
  private String password;
  private String role; // Có thể là "user", "admin", v.v.

  // Constructor
  public Account(int id, String username, String password, String role) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public String toString() {
    return String.format(
        id + " - " + username + " - " + password + " - " + role);
  }

  // Getters và Setter
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

}
