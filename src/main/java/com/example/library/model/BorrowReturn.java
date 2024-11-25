package com.example.library.model;

import java.time.LocalDate;

public class BorrowReturn {
    private int id;
    private int userId;
    private int documentId;
    private int quantityBorrow;
    private LocalDate borrowDate;
    private LocalDate returnDate;


    // Constructors, getters, and setters
    public BorrowReturn(int id, int userId, int documentId, int quantityBorrow, LocalDate returnDate, LocalDate borrowDate) {
        this.id = id;
        this.userId = userId;
        this.documentId = documentId;
        this.quantityBorrow = quantityBorrow;
        this.returnDate = returnDate;
        this.borrowDate = borrowDate;
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

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
