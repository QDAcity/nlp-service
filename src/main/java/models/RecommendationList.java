package models;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.trees.Tree;
import nlp.NLProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        HashMap<Integer, Candidate> restrictedMap = new HashMap<>();
        for(Candidate candidate: candidates.values()) {
            if(candidate.containsNoun()) {
                restrictedMap.put(candidate.hashCode(), candidate);
            }
        }
        candidates = restrictedMap;
    }

    public Map<Integer, Candidate> getCandidates() {
        return candidates;
    }

    public void restrictToSize(int length) {
        HashMap<Integer, Candidate> restrictedMap = new HashMap<>();
        for(Candidate candidate: candidates.values()) {
            if(candidate.getLabels().size() < length) {
                restrictedMap.put(candidate.hashCode(), candidate);
            }
        }
        candidates = restrictedMap;
    }
}
