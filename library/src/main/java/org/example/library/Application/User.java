package org.example.library.Application;

public class User {
    String userName;
    String passWord;

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }
}
