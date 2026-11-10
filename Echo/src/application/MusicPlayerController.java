package application;

import java.io.File;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class MusicPlayerController {

    private static final double SONG_SECONDS = 30.0;
    private static final double TICK_SECONDS = 0.1;
    private static final int SONG_TICKS = (int) Math.round(SONG_SECONDS / TICK_SECONDS);

    private static double savedVolume = 0.3;

    @FXML private ImageView coverImage;
    @FXML  private Label songLabel;
    @FXML  private Label artistLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Button playPauseButton;
    @FXML private Button likeButton;
    @FXML private Slider volumeSlider;
    @FXML private Label volumeLabel;
    private DLL LL = new DLL();

    private String returnTo = "AllSongs.fxml";

    private Node current;

    private int ticksPlayed;

    private boolean playing;

    private Image defaultCover;

    private MediaPlayer mediaPlayer;

    @SuppressWarnings("unused")
	private final Timeline ticker = new Timeline(
            new KeyFrame(Duration.seconds(TICK_SECONDS), e -> tick())
    );

    @SuppressWarnings("unused")
	@FXML
    public void initialize() {

        ticker.setCycleCount(Animation.INDEFINITE);

        defaultCover = coverImage.getImage();

        songLabel.setText("");
        artistLabel.setText("");

        volumeSlider.setValue(savedVolume * 100);

        volumeLabel.setText(Math.round(savedVolume * 100) + "%");

        volumeSlider.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        setVolume(newValue.doubleValue() / 100.0)
        );
    }

    public void start(Song selected, List<Song> playlist, String returnTo) {
        this.returnTo = returnTo;
        pause();
        ticker.stop();
        disposeAudio();
        LL.clear();
        if (playlist == null || playlist.isEmpty()) {
            current = null;
            return;
        }
        int index = playlist.indexOf(selected);
        if (index < 0) {
            index = 0;
        }
        for (Song song : playlist) {
            LL.insert(song);
        }
        current = LL.getNodeAt(index);
        ticksPlayed = 0;
        showCurrent();
        play();
    }

    @FXML
    private void onPlayPause(ActionEvent event) {
        if (current == null) {
            return;
        }
        if (playing) {
            pause();
        } else {
            play();
        }
    }

    @FXML
    private void onNext(ActionEvent event) {
        if (current == null) {
            return;
        }
        goToNext();
        showCurrent();
        play();
    }

    @FXML
    private void onPrevious(ActionEvent event) {

        if (current == null) {
            return;
        }
        current = LL.goToPrev(current);
        ticksPlayed = 0;
        showCurrent();
        play();
    }

    @FXML
    private void onLike(ActionEvent event) {
        if (current == null) {
            return;
        }
        MusicLibrary.toggleLike(current);
        refreshLikeButton();
    }

    @FXML
    private void onBack(ActionEvent event) {
        pause();
        ticker.stop();
        disposeAudio();
        Navigator.show(event, returnTo);
    }

    private void setVolume(double volume) {
        savedVolume = volume;
        volumeLabel.setText(Math.round(volume * 100) + "%");
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    private void goToNext() {
        if (current != null) {
            current = LL.goToNext(current);
            ticksPlayed = 0;
        }
    }

    private void tick() {
        ticksPlayed++;
        if (ticksPlayed >= SONG_TICKS) {
            songFinished();
            return;
        }

        progressBar.setProgress((double) ticksPlayed / SONG_TICKS );
    }

    private void songFinished() {
        if (current == null) {
            return;
        }
        goToNext();
        showCurrent();
        play();
    }

    private void play() {

        if (current == null) {
            return;
        }

        playing = true;

        if (mediaPlayer != null) {

            ticker.pause();

            mediaPlayer.play();

        } else {

            ticker.play();
        }

        playPauseButton.setText("| |");
    }

    private void pause() {

        playing = false;

        ticker.pause();

        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }

        playPauseButton.setText("►");
    }

    private void showCurrent() {

        if (current == null || current.song == null) {
            return;
        }

        songLabel.setText(current.song.getTitle());

        artistLabel.setText(current.song.getArtist());

        showCover();

        loadAudio();

        progressBar.setProgress(
                (double) ticksPlayed / SONG_TICKS
        );

        refreshLikeButton();
    }

    private void showCover() {

        Image cover = null;

        if (current.song.hasCover()) {

            Image image = new Image(
                    new File(current.song.getCoverPath()).toURI().toString()
            );

            if (!image.isError()) {
                cover = image;
            }
        }

        coverImage.setImage(
                cover != null ? cover : defaultCover
        );
    }

    @SuppressWarnings("unused")
	private void loadAudio() {

        disposeAudio();

        if (!current.song.hasAudio()) {
            return;
        }

        try {

            Media media = new Media(
                    new File(current.song.getAudioPath()).toURI().toString()
            );

            MediaPlayer player = new MediaPlayer(media);

            player.setVolume(savedVolume);

            player.currentTimeProperty().addListener(
                    (observable, oldTime, newTime) -> {

                        Duration total = player.getTotalDuration();

                        if (total != null
                                && !total.isUnknown()
                                && total.toMillis() > 0) {

                            progressBar.setProgress(
                                    Math.min(
                                            newTime.toMillis() / total.toMillis(),
                                            1.0
                                    )
                            );
                        }
                    }
            );
            player.setOnEndOfMedia(this::songFinished);
            player.setOnError(() -> {
                if (mediaPlayer == player) {
                    disposeAudio();
                    ticksPlayed = 0;
                    if (playing) {
                        ticker.play();
                    }
                }
            });
            mediaPlayer = player;

        } catch (RuntimeException e) {

            mediaPlayer = null;
        }
    }

    private void disposeAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    private void refreshLikeButton() {

        if (current == null || current.song == null) {
            return;
        }

        boolean liked = MusicLibrary.isLiked(current);

        likeButton.setText(liked ? "♥" : "♡" );

        likeButton.setTextFill(liked ? Color.PINK : Color.GREY);
    }
}