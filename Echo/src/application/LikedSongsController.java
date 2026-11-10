package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class LikedSongsController {

    @FXML
    private ListView<Song> songsList;
    @FXML
    private Label queueInfoLabel;

    @FXML
    public void initialize() {
        Label emptyMessage = new Label("No liked songs yet.\nTap the heart while a song plays.");
        emptyMessage.setStyle("-fx-text-fill: #aaaaaa;");
        songsList.setPlaceholder(emptyMessage);

        songsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                playSelected();
            }
        });

        MusicLibrary.loadLikedSongs();
        refresh();
    }

    @FXML
    private void onPlaySelected(ActionEvent event) {
        playSelected();
    }

    @FXML
    private void onRemoveFront(ActionEvent event) {
        MusicLibrary.removeFirstLiked();
        refresh();
    }

    @FXML
    private void onBack(ActionEvent event) {
        Navigator.show(event, "Choice.fxml");
    }

    private void refresh() {
        SongQueue liked = MusicLibrary.getLikedSongs();
        songsList.getItems().setAll(liked.toList());

        String front = liked.isEmpty() ? "(empty)" : liked.peek().toString();
        queueInfoLabel.setText("Songs in queue: " + liked.size() + "\nFront of queue: " + front);
    }

    private void playSelected() {
        Song selected = songsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        MusicPlayerController player = Navigator.show(songsList, "MusicPlayer.fxml");
        player.start(selected, MusicLibrary.getLikedSongs().toList(), "LikedSongs.fxml");
    }
}