package queue;

import java.util.Arrays;

import static java.lang.Integer.min;

// Inv: queue = queue[0]...queue[size-1] ^ size >= 0 ^ queue[i] != null
public class ArrayQueueModule {
    private static int end = 0;
    private static int size = 0;
    private static Object[] elements = new Object[5];

    //Pre: element != null
    //Post: size' = size + 1 ^ queue'[size] = element ^ 0 <= i < size ^ queue'[i] = queue[i]
    public static void enqueue(Object element) {
        assert element != null;
        ensureCapacity(size + 1);
        size++;
        elements[end] = element;
        end = (end + 1) % elements.length;
    }

    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] data = new Object[2 * capacity];
            int head = getHead();
            System.arraycopy(elements, head, data, 0, elements.length - head);
            System.arraycopy(elements, 0, data, elements.length - head,  head);
            end =  elements.length;
            elements = data;
        }
    }

    //Pre: size > 0
    //Post: R = queue[0] ^ (0 < i < size) ^ queue'[i - 1] = queue[i] ^ size' = size - 1
    public static Object dequeue() {
        assert size > 0;
        Object result = element();
        elements[getHead()] = null;
        size--;
        return result;
    }

    private static int getHead() {
        return end - size >= 0 ? end - size : elements.length + end - size;
    }

    //Pre: True
    //Post: R == size ^ queue immutable
    public static int size() {
        return size;
    }

    // Pre: True
    // Post: R = (size == 0) ^ queue immutable
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: True
    // Post: size' = 0
    public static void clear() {
        elements = new Object[5];
        end = 0;
        size = 0;
    }

    // Pre: size > 0
    // Post: R = queue[0] ^ queue immutable
    public static Object element() {
        assert size > 0;
        return elements[getHead()];
    }

    // Pre: element != null
    // Post: size' = size ^ 1 < i < size' ^ queue'[i] = queue[i - 1] ^ queue'[0] = element
    public static void push(Object element) {
        assert element != null;
        ensureCapacity(size + 1);
        int head = getHead();
        head = head - 1 < 0 ? elements.length - 1 : head - 1;
        elements[head] = element;
        size++;
    }

    // Pre: size > 0
    // Post: R = queue[size - 1] ^ queue immutable
    public static Object peek() {
        assert size > 0;
        int tail = (end - 1 >= 0) ? end - 1 : elements.length - 1;
        return elements[tail];
    }

    // Pre: size > 0
    // Post: R = queue[size - 1] ^ size' = size - 1 ^ -1 < i < size' ^ queue'[i] = queue[i]
    public static Object remove() {
        assert size > 0;
        end = (end - 1 >= 0) ? end - 1 : elements.length - 1;
        Object res = elements[end];
        elements[end] = null;
        size--;
        return res;
    }

    //Pre: True
    //Post: R = "[queue[0], ..., queue[size-1]]" ^ queue immutable
    public static String toStr() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        int begin = getHead();
        if (size > 0) {
            str.append(elements[begin]);
        }
        for (int i = 1; i < size; i++) {
            str.append(", ");
            str.append(elements[(begin + i) % elements.length]);
        }
        str.append("]");
        return str.toString();
    }
    //String joiner
}
