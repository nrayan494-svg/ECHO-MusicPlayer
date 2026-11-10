package application;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class ChoiceController {

    @FXML
    private void switchToAllSongs(MouseEvent event) {
        Navigator.show(event, "AllSongs.fxml");
    }

    @FXML
    private void switchToLikedSongs(MouseEvent event) {
        Navigator.show(event, "LikedSongs.fxml");
    }

    @FXML
    private void switchToAddSongs(MouseEvent event) {
        Navigator.show(event, "AddSong.fxml");
    }
}