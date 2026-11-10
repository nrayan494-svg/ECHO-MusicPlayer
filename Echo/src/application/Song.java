package application;

import java.io.File;
import java.util.Objects;

public class Song {

    private final String title;
    private final String artist;
    private final String audioPath;
    private final String coverPath;

    public Song(String title, String artist, String audioPath, String coverPath) {
        this.title = title;
        this.artist = artist;
        this.audioPath = audioPath;
        this.coverPath = coverPath;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public boolean hasAudio() {
        return audioPath != null && new File(audioPath).isFile();
    }

    public boolean hasCover() {
        return coverPath != null && new File(coverPath).isFile();
    }
    
    public int length() {
    	return title.length();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Song)) {
            return false;
        }
        Song song = (Song) other;
        return title.equalsIgnoreCase(song.title) && artist.equalsIgnoreCase(song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title.toLowerCase(), artist.toLowerCase());
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}