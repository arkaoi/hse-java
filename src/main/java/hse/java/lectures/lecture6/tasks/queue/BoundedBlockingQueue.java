package hse.java.lectures.lecture6.tasks.queue;

import java.util.ArrayDeque;
import java.util.Queue;

public class BoundedBlockingQueue<T> {
    private final Queue<T> queue = new ArrayDeque<>();
    private final int capacity;

    public BoundedBlockingQueue(int capacity) {
        if(capacity <= 0){
            throw new IllegalArgumentException("Capacity <= 0 :^(");
        }
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException{
        if(item == null){
            throw new NullPointerException("not null pls");
        }

        while(queue.size() == capacity()){
            wait();
        }

        queue.add(item);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException{
        while(queue.isEmpty()){
            wait();
        }

        T item = queue.remove();
        notifyAll();

        return item;
    }

    public synchronized int size() {
        return queue.size();
    }

    public int capacity() {
        return capacity;
    }
}