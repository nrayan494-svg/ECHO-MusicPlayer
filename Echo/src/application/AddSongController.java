package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class AddSongController {

    private static final String SUCCESS_STYLE = "-fx-text-fill: #86efac; -fx-font-size: 13px;";
    private static final String ERROR_STYLE = "-fx-text-fill: #fca5a5; -fx-font-size: 13px;";
    private static final String NO_FILE = "No file chosen";

    @FXML
    private TextField titleField;
    @FXML
    private TextField artistField;
    @FXML
    private Label audioLabel;
    @FXML
    private Label coverLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label stackInfoLabel;

    private File audioFile;
    private File coverFile;

    @FXML
    public void initialize() {
        MusicLibrary.loadSongs();
        refreshStackInfo();
    }

    @FXML
    private void onChooseAudio(ActionEvent event) {
        File file = choose("Choose a song file",
                new FileChooser.ExtensionFilter("Audio files", "*.mp3", "*.wav", "*.aac", "*.m4a"));
        if (file == null) {
            return;
        }
        audioFile = file;
        audioLabel.setText(file.getName());

        if (titleField.getText().trim().isEmpty()) {
            titleField.setText(withoutExtension(file.getName()));
        }
    }

    @FXML
    private void onChooseCover(ActionEvent event) {
        File file = choose("Choose a cover photo",
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        if (file == null) {
            return;
        }
        coverFile = file;
        coverLabel.setText(file.getName());
    }

    @FXML
    private void onAdd(ActionEvent event) {
        String title = titleField.getText().trim();
        String artist = artistField.getText().trim();

        if (title.isEmpty() || artist.isEmpty()) {
            showStatus("Enter both a title and an artist.", false);
            return;
        }

        Song song = new Song(title, artist,
                audioFile == null ? null : audioFile.getAbsolutePath(),
                coverFile == null ? null : coverFile.getAbsolutePath());

        if (!MusicLibrary.addSong(song)) {
            showStatus("That song is already in your library.", false);
            return;
        }

        String note = audioFile == null ? " (no audio file, playback will be simulated)" : "";
        showStatus("Added: " + song + note, true);

        titleField.clear();
        artistField.clear();
        titleField.requestFocus();
        audioFile = null;
        coverFile = null;
        audioLabel.setText(NO_FILE);
        coverLabel.setText(NO_FILE);
        refreshStackInfo();
    }

    @FXML
    private void onBack(ActionEvent event) {
        Navigator.show(event, "Choice.fxml");
    }

    private File choose(String title, FileChooser.ExtensionFilter filter) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.getExtensionFilters().add(filter);
        return chooser.showOpenDialog(titleField.getScene().getWindow());
    }

    private static String withoutExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot > 0 ? fileName.substring(0, dot) : fileName;
    }

    private void showStatus(String message, boolean success) {
        statusLabel.setStyle(success ? SUCCESS_STYLE : ERROR_STYLE);
        statusLabel.setText(message);
    }

    private void refreshStackInfo() {
        SongStack songs = MusicLibrary.getSongs();
        String top = songs.isEmpty() ? "(empty)" : songs.peek().toString();
        stackInfoLabel.setText("Songs in stack: " + songs.size() + "\nTop of stack: " + top);
    }
}