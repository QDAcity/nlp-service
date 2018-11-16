package nlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;

import java.util.*;

public class NLProcessor {
    private final Properties props = new Properties();
    private final StanfordCoreNLP pipeline;
    private final CoreDocument doc;

    public NLProcessor(String text) {
        props.setProperty("annotators", "tokenize,ssplit,parse, pos,lemma");
        pipeline = new StanfordCoreNLP(props);
        doc = new CoreDocument(text);
    }

    public Tree getTreeSentence(int i) {
        pipeline.annotate(doc);
        return doc
                .annotation()
                .get(CoreAnnotations.SentencesAnnotation.class)
                .get(i)
                .get(TreeCoreAnnotations.TreeAnnotation.class);
    }

    public List<String> getNounPhrases() {
        List<String> nounPhrases = new LinkedList<>();

        for(int i = 0; i< 2; i++){
            Tree treeAnnotation = getTreeSentence(i);
            nounPhrases.addAll(getNounPhrasesFromSentence(treeAnnotation));
        }

        return nounPhrases;
    }

    private Collection<String> getNounPhrasesFromSentence(Tree treeAnnotation) {
        Collection<String> nps = new LinkedList<>();
        for (Tree nextPart : treeAnnotation) {
            if (nextPart.value().equals("NP")) {
                nps.add(nextPart.flatten().toString());
            }
        }
        return nps;
    }
}
