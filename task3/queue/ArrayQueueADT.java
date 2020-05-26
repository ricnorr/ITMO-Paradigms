package queue;

import java.lang.reflect.Array;
import java.util.Arrays;

import static java.lang.Integer.min;

// Inv: queue = queue[0]...queue[size-1] ^ size >= 0 ^ queue[i] != null
public class ArrayQueueADT {
    private int end = 0;
    private int size = 0;
    private Object[] elements = new Object[5];

    //Pre: element != null ^ arr != null
    //Post: size' = size + 1 ^ queue'[size] = element ^ 0 <= i < size ^ queue'[i] = queue[i]
    public static void enqueue(ArrayQueueADT arr, Object element) {
        assert element != null;
        ensureCapacity(arr, arr.size + 1);
        arr.size++;
        arr.elements[arr.end] = element;
        arr.end = (arr.end + 1) % arr.elements.length;
    }

    private static void ensureCapacity(ArrayQueueADT arr, int capacity) {
        if (capacity > arr.elements.length) {
            Object[] data = new Object[2 * capacity];
            int head = getHead(arr);
            System.arraycopy(arr.elements, head, data, 0, arr.elements.length - head);
            System.arraycopy(arr.elements, 0, data, arr.elements.length - head,  head);
            arr.end =  arr.elements.length;
            arr.elements = data;
        }
    }

    //Pre: size > 0 ^ arr != null
    //Post: R = queue[0] ^ (0 < i < size) ^ queue'[i - 1] = queue[i] ^ size' = size - 1
    public static Object dequeue(ArrayQueueADT arr) {
        assert arr.size > 0;
        Object result = element(arr);
        arr.elements[getHead(arr)] = null;
        arr.size--;
        return result;
    }

    private static int getHead(ArrayQueueADT arr) {
        return arr.end - arr.size >= 0 ? arr.end - arr.size : arr.elements.length + arr.end - arr.size;
    }

    //Pre: arr != null
    //Post: R == size ^ queue immutable
    public static int size(ArrayQueueADT arr) {
        return arr.size;
    }

    // Pre: arr != null
    // Post: R = (size == 0) ^ queue immutable
    public static boolean isEmpty(ArrayQueueADT arr) {
        return arr.size == 0;
    }

    // Pre: arr != null
    // Post: size' = 0
    public static void clear(ArrayQueueADT arr) {
        arr.elements = new Object[5];
        arr.end = 0;
        arr.size = 0;
    }

    // Pre: size > 0 ^ arr != null
    // Post: R = queue[0] ^ queue immutable
    public static Object element(ArrayQueueADT arr) {
        assert arr.size > 0;
        return arr.elements[getHead(arr)];
    }

    // Pre: element != null ^ arr != null
    // Post: size' = size ^ 1 < i < size' ^ queue'[i] = queue[i - 1] ^ queue'[0] = element
    public static void push(ArrayQueueADT arr, Object element) {
        assert element != null;
        ensureCapacity(arr, arr.size + 1);
        int head = getHead(arr);
        head = head - 1 < 0 ? arr.elements.length - 1 : head - 1;
        arr.size++;
        arr.elements[head] = element;
    }

    // Pre: size > 0 ^ arr != null
    // Post: R == queue[size - 1] ^ queue immutable
    public static Object peek(ArrayQueueADT arr) {
        assert arr.size > 0;
        int tail = (arr.end - 1 >= 0) ? arr.end - 1 : arr.elements.length - 1;
        return arr.elements[tail];
    }

    // Pre: size > 0 ^ arr != null
    // Post: R == queue[size - 1] ^ size' = size - 1 ^ -1 < i < size' ^ queue'[i] = queue[i]
    public static Object remove(ArrayQueueADT arr) {
        assert arr.size > 0;
        arr.end = (arr.end - 1 >= 0) ? arr.end - 1 : arr.elements.length - 1;
        Object res = arr.elements[arr.end];
        arr.elements[arr.end] = null;
        arr.size--;
        return res;
    }

    //Pre: arr != null
    //Post: R = "[queue[0], ..., queue[size-1]]" ^ queue immutable
    public static String toStr(ArrayQueueADT arr) {
        StringBuilder str = new StringBuilder();
        str.append("[");
        int begin = getHead(arr);
        if (arr.size > 0) {
            str.append(arr.elements[begin]);
        }
        for (int i = 1; i < arr.size; i++) {
            str.append(", ");
            str.append(arr.elements[(begin + i) % arr.elements.length]);
        }
        str.append("]");
        return str.toString();
    }
}
