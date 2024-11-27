package com.example.library.model;

import java.time.LocalDate;

public class BorrowReturn {
    private int id;
    private int userId;
    private int documentId;
    private String title;
    private int quantityBorrow;
    private LocalDate borrowDate;
    private LocalDate requiredDate;
    private String imageLink;


    // Constructors, getters, and setters
    public BorrowReturn(int id, int userId, int documentId, String title, int quantityBorrow, LocalDate requiredDate, LocalDate borrowDate, String imageLink) {
        this.id = id;
        this.userId = userId;
        this.documentId = documentId;
        this.title = title;
        this.quantityBorrow = quantityBorrow;
        this.requiredDate = requiredDate;
        this.borrowDate = borrowDate;
        this.imageLink = imageLink;
    }

    public BorrowReturn(String imageLink, String title, int quantityBorrow, LocalDate borrowDate, LocalDate requiredDate) {
        this.imageLink = imageLink;
        this.title = title;
        this.quantityBorrow = quantityBorrow;
        this.borrowDate = borrowDate;
        this.requiredDate = requiredDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantityBorrow() {
        return quantityBorrow;
    }

    public void setQuantityBorrow(int quantityBorrow) {
        this.quantityBorrow = quantityBorrow;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(LocalDate requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
