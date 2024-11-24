package com.example.library.model;

public class Document {

    private int id;
    private String title;
    private String author;
    private String publicYear;
    private String publisher;
    private String genre;
    private int quantity;
    private String imageLink;
    private String description;

    // Constructor
    public Document() {

    }

    public Document(int id, String title, String author, String publicYear,
                    String publisher, String genre, int quantity, String imageLink) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicYear = publicYear;
        this.publisher = publisher;
        this.genre = genre;
        this.quantity = quantity;
        this.imageLink = imageLink;
    }

    public Document(String title, String author, String publicYear, String publisher, String genre, int quantity, String imageLink) {
        this.title = title;
        this.author = author;
        this.publicYear = publicYear;
        this.publisher = publisher;
        this.genre = genre;
        this.quantity = quantity;
        this.imageLink = imageLink;
    }

    public Document(String title, String author, String publicYear, String publisher, String genre, String imageLink, String description) {
        this.title = title;
        this.author = author;
        this.publicYear = publicYear;
        this.publisher = publisher;
        this.imageLink = imageLink;
        this.description = description;
        this.genre = genre;
    }


    @Override
    public String toString() {
        return String.format(
                id + " - " + title + " - " + author + " - " + publicYear + " - "
                        + publisher + " - " + genre + " - " + quantity);
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublicYear() {
        return publicYear;
    }

    public void setPublicYear(String publicYear) {
        this.publicYear = publicYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
