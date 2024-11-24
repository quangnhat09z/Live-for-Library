package com.example.library.model;

public class AccountDetail extends Account {

  private String fullName = null;
  private String address = null;
  private String phoneNumber = null;
  private String status = "inactive";


  public AccountDetail(int id, String username, String password, String email, String role) {
    super(id, username, password, email, role);
  }

  public AccountDetail(int id, String username, String password, String email, String role,
      String fullName, String address, String phoneNumber, String status) {
    super(id, username, password, email, role);
    this.fullName = fullName;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.status = status;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return String.format(
        super.getId() + " - " + super.getUsername() + " - " + super.getPassword() + " - "
            + super.getEmail() + " - " + this.fullName + " - " + this.address + " - "
            + super.getRole() + " - " + this.phoneNumber + " - " + this.status);
  }

}
