package application;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class EntryController {

    @FXML
    private void switchToChoice(MouseEvent event) {
        Navigator.show(event, "Choice.fxml");
    }
}