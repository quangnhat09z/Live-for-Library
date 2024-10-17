module login.tets.demo1 {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires com.almasb.fxgl.all;
  requires java.sql;

  opens login.tets.demo1 to javafx.fxml;
  exports login.tets.demo1;

}