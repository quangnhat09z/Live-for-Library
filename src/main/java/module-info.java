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
    exports com.example.library.controller.login;
    opens com.example.library.controller.login to javafx.fxml;
    exports com.example.library.controller.search;
    opens com.example.library.controller.search to javafx.fxml;
    exports com.example.library.controller.manage;
    opens com.example.library.controller.manage to javafx.fxml;
    exports com.example.library.controller.mainScreen;
    opens com.example.library.controller.mainScreen to javafx.fxml;


}