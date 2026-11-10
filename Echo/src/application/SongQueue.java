package application;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SongQueue {

    private static class Node {
        final Song song;
        Node next;

        Node(Song song) {
            this.song = song;
        }
    }

    private Node front;
    private Node back;
    private int size;

    /** Adds a song to the back of the queue. */
    public void enqueue(Song song) {
        Node node = new Node(song);
        if (back == null) {
            front = node;
        } else {
            back.next = node;
        }
        back = node;
        size++;
    }

    /** Removes and returns the song at the front. */
    public Song dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }
        Song song = front.song;
        front = front.next;
        if (front == null) {
            back = null;
        }
        size--;
        return song;
    }

    /** Returns the song at the front without removing it. */
    public Song peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }
        return front.song;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        return size;
    }

    public void clear() {
        front = null;
        back = null;
        size = 0;
    }

    /** Looks through the queue for a song. O(n). */
    public boolean contains(Song song) {
        for (Node current = front; current != null; current = current.next) {
            if (current.song.equals(song)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a song from anywhere in the queue, keeping the order of the others.
     * Every song is dequeued once and enqueued again, except the one being removed. O(n).
     */
    public boolean remove(Song song) {
        boolean found = false;
        int count = size;
        for (int i = 0; i < count; i++) {
            Song current = dequeue();
            if (!found && current.equals(song)) {
                found = true;
            } else {
                enqueue(current);
            }
        }
        return found;
    }

    /** Copies the queue into a list, front first. The queue itself is not changed. */
    public List<Song> toList() {
        List<Song> songs = new ArrayList<>();
        for (Node current = front; current != null; current = current.next) {
            songs.add(current.song);
        }
        return songs;
    }
}