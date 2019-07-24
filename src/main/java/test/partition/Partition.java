package test.partition;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Partition<T> {
    private final BlockingQueue<T> partitionQueue;

    public Partition (BlockingQueue<T> partitionQueue) {
        this.partitionQueue = partitionQueue;
    }

    public void put (T data) throws InterruptedException {
        partitionQueue.put(data);
    }

    public T poll (long timeout, TimeUnit timeUnit) throws InterruptedException {
        return partitionQueue.poll(timeout, timeUnit);
    }

    public boolean isEmpty () {
        return partitionQueue.isEmpty();
    }
}
