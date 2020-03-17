package queue;

// Inv: queue == queue[0]...queue[size-1] ^ size >= 0 ^ queue[i] != null
public abstract class AbstractQueue implements Queue {
    protected int size;

    abstract protected void enqueueReal(Object element);
    abstract protected Object dequeueReal();
    abstract protected void clearReal();
    abstract protected Object elementReal();

    // True
    // array [queue[0],..., queue[size - 1]]
    abstract public Object[] toArray();

    //Pre: element != null
    //Post: size' = size + 1 ^ queue'[size] = element ^ 0 <= i < size ^ queue'[i] = queue[i]
    public void enqueue(Object element) {
        assert element != null;
        enqueueReal(element);
        size++;
    }

    //Pre: size > 0
    //Post: R = queue[0] ^ (0 < i < size) ^ queue'[i - 1] = queue[i] ^ size' = size - 1
    public Object dequeue() {
        assert size > 0;
        Object ans = dequeueReal();
        size--;
        return ans;
    }

    // Pre: size > 0
    // Post: R = queue[0] ^ queue immutable
    public Object element() {
        assert size > 0;
        return elementReal();
    }

    // Pre: True
    // Post: size' = 0
    public void clear() {
        size = 0;
        clearReal();
    }

    //Pre: True
    //Post: R == size ^ queue immutable
    public int size() {
        return size;
    }

    // Pre: True
    // Post: R = (size == 0) ^ queue immutable
    public boolean isEmpty() {
        return (size == 0);
    }

}
