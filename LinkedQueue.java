import java.util.NoSuchElementException;

public class LinkedQueue<T> {

    private class Node {
        T data;
        Node next;
        Node(T d) { data = d; }
    }

    private Node head, tail;
    private int size;

    public void enqueue(T x) {
        Node n = new Node(x);
        if (tail == null) head = tail = n; else { tail.next = n; tail = n; }
        size++;
    }

    public T dequeue() {
        if (head == null) throw new NoSuchElementException("Queue is empty");
        T v = head.data;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return v;
    }

    public T peek() {
        if (head == null) throw new NoSuchElementException("Queue is empty");
        return head.data;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }
}
