package application;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class SongStack {

private static class Node {
  final Song song;
  final Node next;

     Node(Song song, Node next) {
         this.song = song;
         this.next = next;
       }
    }

    private Node top;
    private int size;

    /** Adds a song on top of the stack. */
    public void push(Song song) {
        top = new Node(song, top);
        size++;
    }

    public Song pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        Song song = top.song;
        top = top.next;
        size--;
        return song;
    }

    /** Returns the song on top without removing it. */
    public Song peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return top.song;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    public void clear() {
        top = null;
        size = 0;
    }

    public boolean contains(Song song) {
        for (Node current = top; current != null; current = current.next) {
            if (current.song.equals(song)) {
                return true;
            }
        }
        return false;
    }
    public List<Song> toList() {
        List<Song> songs = new ArrayList<>();
        for (Node current = top; current != null; current = current.next) {
            songs.add(current.song);
        }
        return songs;
    }
}