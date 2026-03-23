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

<<<<<<< HEAD
    public void put(T item) throws InterruptedException {
=======
    public synchronized void put(T item) throws InterruptedException{
        if(item == null){
            throw new NullPointerException("not null pls");
        }
>>>>>>> 1a7546912660f7ad62cf67c9b3a8480d7bfe173b

        while(queue.size() == capacity()){
            wait();
        }

        queue.add(item);
        notifyAll();
    }

<<<<<<< HEAD
    public T take() throws InterruptedException {
        return null;
=======
    public synchronized T take() throws InterruptedException{
        while(queue.isEmpty()){
            wait();
        }

        T item = queue.remove();
        notifyAll();

        return item;
>>>>>>> 1a7546912660f7ad62cf67c9b3a8480d7bfe173b
    }

    public synchronized int size() {
        return queue.size();
    }

    public int capacity() {
        return capacity;
    }
}