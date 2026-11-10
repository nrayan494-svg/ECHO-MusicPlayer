package application;

class Node {

    Song song;
    Node Next;
    Node Prev;

    Node(Song song) {
        this.song = song;
        Next = null;
        Prev = null;
    }
}