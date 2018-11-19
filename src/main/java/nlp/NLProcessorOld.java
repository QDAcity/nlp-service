package nlp;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import models.RecommendationList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class NLProcessorOld implements NLProcessor {
    private CoreDocument doc;
    private final Properties props = new Properties();
    private final List<Tree> recommendations = new LinkedList<>();
    private String language = "de";
    private RecommendationList recommendationList;

    private NLProcessorOld(String text) throws IOException {
        init(text);
    }

    public NLProcessorOld english() {
        language = "en";
        return this;
    }

    @Override
    public NLProcessor init(String text) throws IOException {
        this.doc = new CoreDocument(text);
        InputStream propStream = new FileInputStream("german.properties");
        props.load(propStream);
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.annotate(doc);
        return this;
    }

    public List<String> recommendations() {
        return recommendations
                .stream()
                .map(Tree::toString)
                .collect(Collectors.toList());

    }

    public static NLProcessorOld nounPhrases(String text) throws IOException {
        NLProcessorOld processor = new NLProcessorOld(text);
        processor.doGetNounPhrases();
        return processor;
    }

    private void doGetNounPhrases() {
        for(CoreSentence sentence: doc.sentences()) {
            recommendations.addAll(getNounPhraseTree(sentence.constituencyParse()));
        }
    }

    public void test() {
//        doc.tokens()
    }

    /**
     * Extracts noun phrase subtrees from a tree
     * @param sentence the sentence to extract from
     * @return a list of noun phrase trees
     */
    private List<Tree> getNounPhraseTree(Tree sentence) {
        List<Tree> nps = new LinkedList<>();
        for (Tree nextTree : sentence) {
            System.out.println(nextTree);
            if (nextTree.value().equals("NP")) {
                nps.add(nextTree);
            }
        }
        return nps;
    }



}
