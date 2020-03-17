package queue;

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

    @Override
    protected void enqueueReal(Object element) {
        assert element != null;
        if (head == null) {
           tail = new Node(null, element);
           head = tail;
        } else {
            tail.prev = new Node(null, element);
            tail = tail.prev;
        }
    }

    @Override
    protected Object dequeueReal() {
        Object res = head.data;
        head = head.prev;
        return res;
    }

    @Override
    protected Object elementReal() {
        return head.data;
    }

    @Override
    public void clearReal() {
        head = null;
        tail = null;
    }

    //Pre: True
    //Post: R = [queue[0],...., queue[size - 1]]
    public Object[] toArray() {
        Object[] res = new Object[size];
        Node cur = head;
        for (int i = 0; i < size; i++) {
            res[i] = cur.data;
            cur = cur.prev;
        }
        return res;
    }
}
