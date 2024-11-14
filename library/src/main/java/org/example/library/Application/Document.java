package org.example.library.Application;

public class Document {

    private int id;
    private String title;
    private String author;
    private String publicYear;
    private String publisher;
    private String genre;
    private int quantity;

    // Constructor
    public Document(int id, String title, String author, String publicYear,
                    String publisher, String genre, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicYear = publicYear;
        this.publisher = publisher;
        this.genre = genre;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format(
                id+ " - " + title + " - " + author + " - " + publicYear + " - "
                        + publisher + " - " + genre + " - "  + quantity);
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
}
