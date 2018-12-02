package nlp;

import de.linguatools.disco.CorruptConfigFileException;
import de.linguatools.disco.DISCO;

import java.io.IOException;

public class DiscoAdapter implements CorpusAdapter {
    private final DISCO disco;

    public DiscoAdapter(String configFile) throws IOException, CorruptConfigFileException {
        disco = DISCO.load(configFile);
    }

    @Override
    public long numberOfWords() {
        return disco.numberOfWords();
    }

    @Override
    public long countOccurences(String word) throws IOException {
        return disco.frequency(word);
    }
}
