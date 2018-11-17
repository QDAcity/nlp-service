package nlp;

import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.trees.Tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleNLProcessor {
    private final Document doc;
    private List<Tree> recommendationTrees;

    private SimpleNLProcessor(Document doc) {
        this.doc = doc;
        recommendationTrees = getNounPhrases();
    }

    private List<Tree> getNounPhrases() {
        List<Tree> nounPhrases = new LinkedList<>();
        for(Sentence sentence: doc.sentences()) {
            Tree tree = sentence.parse();
            nounPhrases.addAll(getNounPhraseTree(tree));
        }
        return nounPhrases;
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


    public List<String> recommendations() {
        return recommendationTrees
                .stream()
                .map(Tree::toString)
                .collect(Collectors.toList());
    }



    public static SimpleNLProcessor nounPhrases(String text) {
        return new SimpleNLProcessor(new Document(text));
    }

}
