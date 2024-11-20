package com.example.library.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Book {
    private final SimpleIntegerProperty bookId;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleIntegerProperty publishYear;
    private final SimpleStringProperty publisher;
    private final SimpleBooleanProperty isBorrowed;
    private final SimpleStringProperty imageLink;

    public Book(SimpleIntegerProperty bookId, SimpleStringProperty title,
                SimpleStringProperty author, SimpleIntegerProperty publishYear,
                SimpleStringProperty publisher, SimpleBooleanProperty isBorrowed,
                SimpleStringProperty imageLink) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.publisher = publisher;
        this.isBorrowed = isBorrowed;
        this.imageLink = imageLink;
    }

    public int getBookId() {
        return bookId.get();
    }

    public SimpleIntegerProperty bookIdProperty() {
        return bookId;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public int getPublishYear() {
        return publishYear.get();
    }

    public SimpleIntegerProperty publishYearProperty() {
        return publishYear;
    }

    public String getPublisher() {
        return publisher.get();
    }

    public SimpleStringProperty publisherProperty() {
        return publisher;
    }

    public boolean isIsBorrowed() {
        return isBorrowed.get();
    }

    public SimpleBooleanProperty isBorrowedProperty() {
        return isBorrowed;
    }

    public String getImageLink() {
        return imageLink.get();
    }

    public SimpleStringProperty imageLinkProperty() {
        return imageLink;
    }
}