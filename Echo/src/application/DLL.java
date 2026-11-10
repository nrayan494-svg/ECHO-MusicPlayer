package application;

public class DLL {

    Node Head;
    Node Tail;

    DLL() {
        Head = Tail = null;
    }

    boolean isEmpty() {
        return Head == null;
    }

    void insert(Song song) {

        Node newNode = new Node(song);

        if (Head == null) {

            Head = Tail = newNode;

            Head.Prev = Tail;
            Tail.Next = Head;

            return;
        }

        Tail.Next = newNode;
        newNode.Prev = Tail;

        Tail = newNode;

        Tail.Next = Head;
        Head.Prev = Tail;
    }

    Node goToNext(Node current) {

        if (current == null) {
            return null;
        }

        return current.Next;
    }

    Node goToPrev(Node current) {

        if (current == null) {
            return null;
        }

        return current.Prev;
    }

    Node getNodeAt(int index) {

        if (index < 0 || Head == null) {
            return null;
        }

        Node current = Head;

        for (int i = 0; i < index; i++) {
            current = current.Next;
        }

        return current;
    }

    void clear() {

        Head = null;
        Tail = null;
    }
}