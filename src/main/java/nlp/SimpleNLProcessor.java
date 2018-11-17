package nlp;

import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.trees.Tree;

import java.util.LinkedList;
import java.util.List;

public class SimpleNLProcessor  {
    List<Tree> getNounPhrases(Document doc) {
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
}
