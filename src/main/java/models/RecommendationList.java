package models;

import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.trees.Tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RecommendationList {
    private Map<Integer, Candidate> candidates = new HashMap<>();

    public RecommendationList(List<CoreSentence> sentences) {
//        System.out.println(sentences.get(0).constituencyParse());
//        List<CoreLabel> leaves = sentences.get(0).constituencyParse().taggedLabeledYield();
//        for(CoreLabel label: leaves) {
//            System.out.println(label.value() + "||" + label.lemma());
//        }
        for(CoreSentence sentence: sentences) {
            candidates.putAll(extractNounPhrases(sentence));
        }
    }

    private Map<Integer, Candidate> extractNounPhrases(CoreSentence sentence) {
        HashMap<Integer, Candidate> nps = new HashMap<>();
        //filter candidates
        for(Tree tree: sentence.constituencyParse()) {
            if(tree.value().equals("NP")) {
                //construct candidate
                Tree sentiments = null;
                Candidate np = new Candidate(tree, tree);
                nps.put(np.hashCode(), np);
            }
        }
        return nps;
    }

    public void restrictToNouns() {
        restrictTo(Candidate::containsNoun);
    }

    public void restrictToSize(int length) {
        restrictTo(c -> c.getLabels().size() < length);
    }

    private void restrictTo(Predicate<Candidate> pred) {
        candidates = candidates
                .entrySet()
                .stream()
                .filter(e -> pred.test(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<Integer, Candidate> getCandidates() {
        return candidates;
    }
}
