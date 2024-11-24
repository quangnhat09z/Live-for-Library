module com.example.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires java.desktop;
    requires javafx.media;
    requires com.jfoenix;
    requires com.google.gson;


    opens com.example.library to javafx.fxml;
    exports com.example.library;
    exports com.example.library.model;
    opens com.example.library.model to javafx.fxml;
    exports com.example.library.controller;
    opens com.example.library.controller to javafx.fxml;


}