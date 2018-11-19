package nlp;

import java.io.IOException;

public class KeywordExtraction {
    public static NLProcessor nounPhrases(String text) throws IOException {
        NLProcessorNew proc = new NLProcessorNew();
        proc.init(text);
        return proc;
    }
}
