package queue;

import static java.lang.Integer.min;

// Inv: queue == queue[0]...queue[size-1] ^ size >= 0 ^ queue[i] != null
public class ArrayQueue extends AbstractQueue {

    private int end = 0;
    private Object[] elements = new Object[5];

    @Override
    protected void enqueueReal(Object element) {
        ensureCapacity(size + 1);
        elements[end] = element;
        end = (end + 1) % elements.length;
    }

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] data = new Object[2 * capacity];
            int head = getHeadNum();
            System.arraycopy(elements, head, data, 0, elements.length - head);
            System.arraycopy(elements, 0, data, elements.length - head,  head);
            end =  elements.length;
            elements = data;
        }
    }

    @Override
    protected Object dequeueReal() {
        assert size > 0;
        Object result = element();
        elements[getHeadNum()] = null;
        size--;
        return result;
    }

    private int getHeadNum() {
        return end - size >= 0 ? end - size : elements.length + end - size;
    }

    @Override
    protected void clearReal() {
        elements = new Object[5];
        end = 0;
    }

    @Override
    protected Object elementReal() {
        return elements[getHeadNum()];
    }

    //Pre: True
    //Post: R = [queue[0],...., queue[size - 1]]
    public Object[] toArray() {
        Object[] res = new Object[size];
        int cnt = min(elements.length - getHeadNum(), size);
        System.arraycopy(elements, getHeadNum(), res, 0, cnt);
        int cnt1 = min(size -  cnt, elements.length - cnt);
        System.arraycopy(elements, 0, res, cnt, cnt1);
        return res;
    }

}
