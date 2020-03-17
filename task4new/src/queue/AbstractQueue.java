package queue;

// Inv: queue == queue[0]...queue[size-1] ^ size >= 0 ^ queue[i] != null
public abstract class AbstractQueue implements Queue {
    protected int size;

    //Pre: element != null
    //Post: queue'[size - 1] = element
    abstract public void enqueue(Object element);

    //Pre: size > 0
    //Post: R = queue[0] ^ (0 < i < size) ^ queue'[i - 1] = queue[i] ^ size' = size - 1
    abstract public Object dequeue();

    // Pre: size > 0
    // Post: R = queue[0] ^ queue immutable
    abstract public Object element();

    //Pre: True
    //Post = array [queue[0],..., queue[size - 1]] ^ queue immutable
    abstract public Object[] toArray();

    // Pre: True
    // Post: size' = 0
    abstract public void clear();

    // Pre: True
    // Post: R = size
    public int size() {
        return size;
    }

    // Pre: True
    // Post: R = (size == 0)
    public boolean isEmpty() {
        return (size() == 0);
    }
}
