 package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public final class MusicLibrary {

    private static final SongStack SONGS = new SongStack();
    private static final SongQueue LIKED = new SongQueue();
    private static final DLL LL = new DLL();

    private static final Path SONGS_FILE = Paths.get("AllSongs.txt");
    private static final Path LIKED_FILE = Paths.get("LikedSongs.txt");

    private MusicLibrary() {
    }

    public static SongStack getSongs() {
        return SONGS;
    }

    public static SongQueue getLikedSongs() {
        return LIKED;
    }
    
    public static DLL getLLSongs() {
    	return LL;
    }

    /** Loads both files. */
    public static void loadAll() {
        loadSongs();
        loadLikedSongs();
    }



    public static boolean addSong(Song song) {
        if (SONGS.contains(song)) {
            return false;
        }
        SONGS.push(song);
        try {
            Files.writeString(SONGS_FILE, toLine(song) + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Could not write " + SONGS_FILE + ": " + e.getMessage());
        }
        return true;
    }


    public static void loadSongs() {
        SONGS.clear();
        for (Song song : readSongs(SONGS_FILE)) {
            if (!SONGS.contains(song)) {
                SONGS.push(song);
            }
        }
    }
    
    public static void loadSongsLL() {
        for (Song song : readSongs(SONGS_FILE)) {
            LL.insert(song);
            }
        }


    public static boolean isLiked(Node current) {
        return LIKED.contains(current.song);
    }

    public static boolean toggleLike(Node current) {
        boolean liked;
        if (LIKED.contains(current.song)) {
            LIKED.remove(current.song);
            liked = false;
        } else {
            LIKED.enqueue(current.song);
            liked = true;
        }
        saveLikedSongs();
        return liked;
    }

    public static void removeFirstLiked() {
        if (!LIKED.isEmpty()) {
            LIKED.dequeue();
            saveLikedSongs();
        }
    }

    public static void loadLikedSongs() {
        LIKED.clear();
        for (Song song : readSongs(LIKED_FILE)) {
            if (!LIKED.contains(song)) {
                LIKED.enqueue(song);
            }
        }
    }

    private static void saveLikedSongs() {
        List<String> lines = new ArrayList<>();
        for (Song song : LIKED.toList()) {
            lines.add(toLine(song));
        }
        try {
            Files.write(LIKED_FILE, lines);
        } catch (IOException e) {
      }
    }

    private static String toLine(Song song) {
        return String.join("|",
                clean(song.getTitle()),
                clean(song.getArtist()),
                clean(song.getAudioPath()),
                clean(song.getCoverPath()));
    }

    private static String clean(String value) {
        return value == null ? "" : value.replace('|', ' ').replaceAll("[\\r\\n]+", " ").trim();
    }

    private static String emptyToNull(String value) {
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
    

private static void countingSort(List<Song> s, int pos) {

    int n = s.size();

    List<Song> output = new ArrayList<>(
        java.util.Collections.nCopies(n, null)
    );

    int[] count = new int[129];

    for (Song song : s) {

        String title = song.getTitle().toLowerCase();

        int index;

        if (pos >= title.length()) {
            index = 0;
        } else {
            index = title.charAt(pos) + 1;
        }

        count[index]++;
    }

    for (int i = 1; i < count.length; i++) {
        count[i] += count[i - 1];
    }
    for (int i = n - 1; i >= 0; i--) {

        String title = s.get(i).getTitle().toLowerCase();

        int index;

        if (pos >= title.length()) {
            index = 0;
        } else {
            index = title.charAt(pos) + 1;
        }

        output.set(--count[index], s.get(i));
    }

    for (int i = 0; i < n; i++) {
        s.set(i, output.get(i));
    }
}

    
public static List<Song> radixSort(List<Song> s) {

    int max = 0;

    for (int i = 0; i < s.size(); i++) {

        int length = s.get(i).getTitle().length();

        if (max < length) {
            max = length;
        }
    }

    for (int pos = max - 1; pos >= 0; pos--) {

        countingSort(s, pos);
    }

    return s;
}
    

    private static List<Song> readSongs(Path path) {
        List<Song> songs = new ArrayList<>();
        if (!Files.exists(path)) {
            return songs;
        }
        try {
            for (String line : Files.readAllLines(path)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length < 2) {
                    continue;
                }
                String audio = parts.length > 2 ? emptyToNull(parts[2]) : null;
                String cover = parts.length > 3 ? emptyToNull(parts[3]) : null;
                songs.add(new Song(parts[0].trim(), parts[1].trim(), audio, cover));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return songs;
    }
}