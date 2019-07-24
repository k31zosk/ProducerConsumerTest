package test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import test.consumer.Consumer;
import test.partition.Partition;
import test.producer.Producer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class MainClass {
    private static Logger logger = LogManager.getLogger(MainClass.class);
    public static void main (String[] args) {
        if (args.length < 3) {
            logger.error("this program must have 3 arguments");
            return;
        }

        String originFilePath = args[0];
        String resultDirectoryPath = args[1];

        int partitionNumber;
        try {
            partitionNumber = Integer.valueOf(args[2]);
        }
        catch (NumberFormatException nfEx) {
            logger.error("wrong format partition number");
            return;
        }

        if (partitionNumber < 1 || partitionNumber > 27) {
            logger.error("wrong range partition number");
            return;
        }

        Map<Integer, Partition<String>> partitionMap = new HashMap<>();
        List<Consumer> consumerList = new ArrayList<>();

        for (int index=0; index < partitionNumber; index++) {
            Partition<String> partition = new Partition<>(new LinkedBlockingQueue<>());
            partitionMap.put(index, partition);
            Consumer consumer = new Consumer(resultDirectoryPath, partition);
            consumerList.add(consumer);
            consumer.start();
        }

        Producer producer = new Producer(originFilePath, partitionMap);
        producer.start();

        try {
            producer.join();
            for (Consumer consumer : consumerList) {
                consumer.stopThread();
            }
            for (Consumer consumer : consumerList) {
                consumer.join();
            }
        }
        catch (InterruptedException iEx) {
            logger.error(iEx);
        }
    }
}
