package com.example.library.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Book {
    private final SimpleStringProperty bookId;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleIntegerProperty publishYear;
    private final SimpleBooleanProperty isBorrowed;

    public Book(String bookId, String title, String author, int publishYear) {
        this.bookId = new SimpleStringProperty(bookId);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publishYear = new SimpleIntegerProperty(publishYear);
        this.isBorrowed = new SimpleBooleanProperty(false);
    }

    // Getters và setters
    public String getBookId() {
        return bookId.get();
    }

    public void setBookId(String bookId) {
        this.bookId.set(bookId);
    }

    public SimpleStringProperty bookIdProperty() {
        return bookId;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public int getPublishYear() {
        return publishYear.get();
    }

    public void setPublishYear(int publishYear) {
        this.publishYear.set(publishYear);
    }

    public SimpleIntegerProperty publishYearProperty() {
        return publishYear;
    }

    public boolean getIsBorrowed() {
        return isBorrowed.get();
    }

    public void setIsBorrowed(boolean isBorrowed) {
        this.isBorrowed.set(isBorrowed);
    }

    public SimpleBooleanProperty isBorrowedProperty() {
        return isBorrowed;
    }
}