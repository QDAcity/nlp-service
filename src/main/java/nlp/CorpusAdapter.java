package nlp;

import java.io.IOException;

public interface CorpusAdapter {


    long numberOfWords();

    long countOccurences(String word) throws IOException;
}
