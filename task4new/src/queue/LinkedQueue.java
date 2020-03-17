package queue;

// Inv: queue == queue[0]...queue[size-1] ^ size >= 0 ^ queue[i] != null
public class LinkedQueue extends AbstractQueue {

    private class Node {
        private Node prev;
        private Object data;

        public Node(Node prev, Object data) {
            this.prev = prev;
            this.data = data;
        }
    }

    private Node head;
    private Node tail;

    //Pre: element != null
    //Post: size' = size + 1 ^ queue'[size] = element ^ 0 <= i < size ^ queue'[i] = queue[i]
    public void enqueue(Object element) {
        assert element != null;
        if (head == null) {
           tail = new Node(null, element);
           head = tail;
        } else {
            tail.prev = new Node(null, element);
            tail = tail.prev;
        }
        size++;
    }

    //Pre: size > 0
    //Post: R = queue[0] ^ (0 < i < size) ^ queue'[i - 1] = queue[i] ^ size' = size - 1
    public Object dequeue() {
        assert size > 0;
        Object res = head.data;
        head = head.prev;
        size--;
        return res;
    }

    // Pre: size > 0
    // Post: R = queue[0] ^ queue immutable
    public Object element() {
        assert size() > 0;
        return head.data;
    }

    // Pre: True
    // Post: True
    @Override
    public void clear() {
        size = 0;
        head = null;
        tail = null;
    }

    //Pre: True
    //Post: R = [queue[0],...., queue[size - 1]]
    @Override
    public Object[] toArray() {
        Object[] res = new Object[size()];
        Node cur = head;
        for (int i = 0; i < size(); i++) {
            res[i] = cur.data;
            cur = cur.prev;
        }
        return res;
    }
}
