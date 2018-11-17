package nlp;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NLProcessor {
    private final CoreDocument doc;
    private final Properties props = new Properties();
    private String annotators;
    private String language = "de";

    private NLProcessor(String text) {
        this.doc = new CoreDocument(text);
        annotators = "tokenize,ssplit,parse";
    }

    public NLProcessor english() {
        language = "en";
        return this;
    }

    public List<String> recommendations() {
        props.setProperty("annotators", annotators);
        props.setProperty("language", language);
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.annotate(doc);
        return doc
                .sentences()
                .stream()
                .map(s -> s.constituencyParse().toString())
                .collect(Collectors.toList());
    }

    public static NLProcessor nounPhrases(String text) {
        return new NLProcessor(text);
    }

}
