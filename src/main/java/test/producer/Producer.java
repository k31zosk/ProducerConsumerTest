package test.producer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import test.partition.Partition;

import java.io.*;
import java.util.Map;
import java.util.regex.Pattern;

public class Producer extends Thread {
    private static Logger logger = LogManager.getLogger(Producer.class);

    private final String filePath;
    private final Map<Integer, Partition<String>> partitionMap;
    private final Pattern pattern;

    public Producer (String filePath, Map<Integer, Partition<String>> partitionMap) {
        this.filePath = filePath;
        this.partitionMap = partitionMap;
        this.pattern = Pattern.compile("^([a-zA-Z0-9])\\S*$");
    }

    private boolean validationCheck (String word) {
        return pattern.matcher(word).find();
    }

    private int getHashIndex (String word, int partitionNumber) {
        int index = word.toLowerCase().hashCode() % partitionNumber;

        if (index < 0) return index * -1;
        else           return index;
    }

    @Override
    public void run() {
        File file = new File(filePath);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String word;
            int index;
            while ((word = bufferedReader.readLine()) != null) {
                if (!validationCheck(word)) continue;

                index = getHashIndex(word, partitionMap.size());
                partitionMap.get(index).put(word);
            }
        }
        catch (IOException | InterruptedException ex) {
            logger.error(ex);
        }
    }
}
