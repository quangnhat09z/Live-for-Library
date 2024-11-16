package com.example.library.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class searchDocument {

    public List<Document> searchBooks(String id, String title, String author, String publicYear, String publisher, String genre, String quantity, String imageLink) {
        List<Document> books = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM documents WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (!id.isEmpty()) {
            queryBuilder.append(" AND id = ?");
            params.add(id);
        }
        if (!title.isEmpty()) {
            queryBuilder.append(" AND title LIKE ?");
            params.add("%" + title + "%");
        }
        if (!author.isEmpty()) {
            queryBuilder.append(" AND author LIKE ?");
            params.add("%" + author + "%");
        }
        if (!publicYear.isEmpty()) {
            queryBuilder.append(" AND publicYear = ?");
            params.add(publicYear);
        }
        if (!publisher.isEmpty()) {
            queryBuilder.append(" AND publisher LIKE ?");
            params.add("%" + publisher + "%");
        }
        if (!genre.isEmpty()) {
            queryBuilder.append(" AND genre LIKE ?");
            params.add("%" + genre + "%");
        }
        if (!quantity.isEmpty()) {
            queryBuilder.append(" AND quantity = ?");
            params.add(quantity);
        }

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int bookId = resultSet.getInt("id");
                String bookTitle = resultSet.getString("title");
                String bookAuthor = resultSet.getString("author");
                String bookPublicYear= resultSet.getString("publicYear");
                String bookPublisher = resultSet.getString("publisher");
                String bookGenre = resultSet.getString("genre");
                int bookQuantity = resultSet.getInt("quantity");
                String bookImageLink = resultSet.getString("imageLink");


                // Thêm các trường khác nếu cần thiết

                Document book = new Document(bookId, bookTitle, bookAuthor, bookPublicYear, bookPublisher, bookGenre, bookQuantity, bookImageLink);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

}
