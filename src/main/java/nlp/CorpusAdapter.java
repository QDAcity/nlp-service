package nlp;

import java.io.IOException;

public interface CorpusAdapter {

    long numberOfWords();

    long countOccurrences(String word) throws IOException;
}
