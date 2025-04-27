package roboticoffee.utils;

import java.util.LinkedList;

public class LimitedQueue<E> extends LinkedList<E> {
    private final int maxSize;

    public LimitedQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        if (size() >= maxSize) {
            return false;
        }
        return super.add(e);
    }

    public boolean isFull() {
        return size() >= maxSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

}
