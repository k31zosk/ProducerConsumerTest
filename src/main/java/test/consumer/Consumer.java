package test.consumer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import test.partition.Partition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Consumer extends Thread {
    private static Logger logger = LogManager.getLogger(Consumer.class);

    private final String resultPath;
    private final Partition<String> partition;
    private boolean isRunning;
    private final Set<String> wordSet;

    public Consumer (String resultPath, Partition<String> partition) {
        this.resultPath = resultPath;
        this.partition = partition;
        this.isRunning = false;
        this.wordSet = new HashSet<>();
    }

    public void stopThread () {
        isRunning = false;
    }

    private boolean wordDuplicationCheck (String word) {
        if (wordSet.contains(word.toLowerCase())) {
            return true;
        }
        else {
            wordSet.add(word.toLowerCase());
            return false;
        }
    }

    private void writeWord (String filePath, String word) {
        File file = new File(filePath);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
            bufferedWriter.write(word+"\n");
        }
        catch (IOException ioEx) {
            logger.error(ioEx);
        }
    }

    private String getFilePath (String word) {
        char first = word.toCharArray()[0];
        if (first > 47 && first < 58) {
            return resultPath + "\\number.txt";
        }
        else {
            return resultPath + "\\" + word.substring(0, 1).toLowerCase() + ".txt";
        }
    }

    @Override
    public void run() {
        isRunning = true;
        String word;

        try {
            while (isRunning || !partition.isEmpty()) {
                word = partition.poll(100, TimeUnit.MILLISECONDS);
                if (word == null || wordDuplicationCheck(word)) continue;

                writeWord(getFilePath(word), word);
            }
        }
        catch (InterruptedException iEx) {
            logger.error(iEx);
        }
    }
}
