package com.example.library.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

  // Kiểm tra email
  public static boolean checkEmail(String email) {
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  // Kiểm tra username
  public static boolean checkUsername(String username) {
    String usernameRegex = "^[a-zA-Z0-9_]{3,15}$";
    Pattern pattern = Pattern.compile(usernameRegex);
    Matcher matcher = pattern.matcher(username);
    return matcher.matches();
  }

  // Kiểm tra password (có ít nhất một chữ viết hoa và một ký tự đặc biệt)
  public static boolean checkPassword(String password) {
    String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\|,.<>\\/?]).{8,}$";
    Pattern pattern = Pattern.compile(passwordRegex);
    Matcher matcher = pattern.matcher(password);
    return matcher.matches();
  }


  /**
   * Only for test
   * @param
   */



//  public static void main(String[] args) {
//    // Kiểm tra các ví dụ
//    System.out.println("Email valid: " + checkEmail("test@example.com"));
//    System.out.println("Username valid: " + checkUsername("username"));
//    System.out.println("Password valid: " + checkPassword("Paaaa3!"));
//    System.out.println("Password valid: " + checkPassword("password123"));
//  }
}

/**
 * Email phai co dang @gmail.com
 *
 * Mat khau co toi thieu 8 ki tu
 *
 * Username thi thoai mai
 */