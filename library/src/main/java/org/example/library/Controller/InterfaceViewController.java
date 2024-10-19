package org.example.library.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class InterfaceViewController {
    @FXML
    private ImageView barcaImage;
    @FXML
    private Button barcaButton;
    @FXML
    private Text barcaWelcomeText;
    @FXML
    private void handleBarca() {
        barcaWelcomeText.setVisible(true);
        barcaButton.setVisible(false);
        barcaImage.setVisible(true);
    }
}
