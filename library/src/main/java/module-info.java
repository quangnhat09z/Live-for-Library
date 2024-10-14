module org.example.library {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires org.json;

    opens org.example.library to javafx.fxml;
    exports org.example.library;
}