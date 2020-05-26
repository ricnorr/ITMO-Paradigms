package queue;


public interface Queue {
    //Pre: element != null
    //Post: size' = size + 1 ^ queue'[size] = element ^ 0 <= i < size ^ queue'[i] = queue[i]
    void enqueue(Object element);

    //Pre: size > 0
    //Post: R = queue[0] ^ (0 < i < size) ^ queue'[i - 1] = queue[i] ^ size' = size - 1
    Object dequeue();

    // Pre: size > 0
    // Post: R = queue[0] ^ queue immutable
    Object element();

    // Pre: True
    // Post: size' = 0
    void clear();

    //Pre: True
    //Post: R == size ^ queue immutable
    int size();

    // Pre: True
    // Post: ((R == true ^ size == 0) || (R == false)) ^ queue immutable
    boolean isEmpty();

    //Pre: True
    //Post = array [queue[0],..., queue[size - 1]] ^ queue immutable
    Object[] toArray();
}
