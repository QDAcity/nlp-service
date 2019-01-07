package nlp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;

public class FrequencyListAdapter implements CorpusAdapter {
    private final Logger logger = LoggerFactory.getLogger(FrequencyListAdapter.class);

    private static final int LINE_BUF = 128;

    private static final int NEWLINE = 10;
    private static final int TAB = 9;

    private final HashMap<String, Long> frequencies = new HashMap<>(1000000);
    private long numberOfIncludedWords;

    public FrequencyListAdapter(String filePath) throws IOException {
        init(filePath);
    }

    private void init(String filePath) throws IOException {
        InputStream stream = new BufferedInputStream(new FileInputStream(filePath));
        byte[] buffer = new byte[2048];
        int bufSize;
        long lineCount = 0;
        byte[] wordBytes = new byte[LINE_BUF];
        byte[] frequencyBytes = new byte[LINE_BUF];
        int wordIndex = 0;
        int frequencyIndex = 0;
        boolean tabOccured = false;

        do {
            bufSize = stream.read(buffer);
            for(int i = 0; i < bufSize; i++) {
                if(buffer[i] == NEWLINE) {
                    String word = new String(wordBytes, 0, wordIndex);
                    String frequency = new String(frequencyBytes, 0, frequencyIndex);
                    wordIndex = 0;
                    frequencyIndex = 0;
                    frequencies.put(word, Long.valueOf(frequency));
                    tabOccured = false;
                    lineCount++;
                }
                else if(buffer[i] == TAB) {
                    tabOccured = true;
                }
                else {
                    if(tabOccured) {
                        //construct word
                        wordBytes[wordIndex++] = buffer[i];
                    }
                    else {
                        //construct frequency
                        frequencyBytes[frequencyIndex++] = buffer[i];
                    }
                }
            }
        } while(bufSize != -1);
        numberOfIncludedWords = lineCount;
    }

    @Override
    public long numberOfWords() {
        return 100000000;
    }

    @Override
    public long countOccurrences(String word) {
        Optional<Long> count = Optional.ofNullable(frequencies.get(word));
        logger.info("Word Occurences for " + word + ": " + String.valueOf(count.orElse(0L)));
        return count.orElse(0L);
    }
}
