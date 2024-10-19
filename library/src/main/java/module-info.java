module org.example.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires org.json;
    requires java.sql;

    opens org.example.library to javafx.fxml;
    exports org.example.library;
//    exports org.example.library.Application;  // Đảm bảo package này được export
    exports org.example.library.Controller;
    opens org.example.library.Controller to javafx.fxml;
    exports org.example.library.Application;
    opens org.example.library.Application;
}
