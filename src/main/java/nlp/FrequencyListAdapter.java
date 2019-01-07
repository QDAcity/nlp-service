package nlp;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;

public class FrequencyListAdapter implements CorpusAdapter {

    private static final int LINE_BUF = 128;

    private static final int NEWLINE = 10;
    private static final int TAB = 9;

    private final HashMap<String, Long> frequencies = new HashMap<>(1000000);
    private long numberOfWords;

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
        numberOfWords = lineCount;
    }

    @Override
    public long numberOfWords() {
        return numberOfWords;
    }

    @Override
    public long countOccurrences(String word) throws IOException {
        Optional<Long> count = Optional.ofNullable(frequencies.get(word));
        return count.orElse(0L);
    }
}
