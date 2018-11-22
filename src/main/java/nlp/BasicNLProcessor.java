package nlp;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import models.Candidate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BasicNLProcessor implements NLProcessor{
    private final StanfordCoreNLP pipeline;

    public BasicNLProcessor() throws IOException {
        InputStream propStream = new FileInputStream("german.properties");
        Properties props = new Properties();
        props.load(propStream);
        pipeline = new StanfordCoreNLP(props);
    }

    public Map<Integer, Candidate> extractNounPhrases(String text) {
        CoreDocument doc = new CoreDocument(text);
        pipeline.annotate(doc);

        HashMap<Integer, Candidate> nps = new HashMap<>();

        for(CoreSentence sentence: doc.sentences()) {
            nps.putAll(doExtractNounPhrases(sentence));
        }

        return nps;
    }

    private Map<Integer, Candidate> doExtractNounPhrases(CoreSentence sentence) {
        HashMap<Integer, Candidate> nps = new HashMap<>();
        //filter candidates
        for(Tree tree: sentence.constituencyParse()) {
            if(tree.value().equals("NP")) {
                //construct candidate
                Candidate np = new Candidate(tree);
                nps.put(np.hashCode(), np);
            }
        }
        return nps;
    }


    public Map<Integer, Candidate> restrictToNouns(Map<Integer, Candidate> candidates) {
        return restrictTo(candidates, this::containsNoun);
    }

    public Map<Integer, Candidate> restrictToTermLength(Map<Integer, Candidate> candidates, int length) {
        return restrictTo(candidates, c -> c.getLabels().size() < length);
    }

    private Map<Integer, Candidate> restrictTo(Map<Integer, Candidate> candidates, Predicate<Candidate> pred) {
        return candidates
                .entrySet()
                .stream()
                .filter(e -> pred.test(e.getValue()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    public Map<Integer, Candidate> filterPOSTags(Map<Integer, Candidate> candidates) {
        return candidates
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Entry::getKey, v -> filterByPOSTag(v.getValue())));
    }

    private boolean containsNoun(Candidate candidate) {
        return candidate
                .getLabels()
                .stream()
                .anyMatch(l -> isNoun(l.value()));
    }

    private boolean isNoun (String labelVal) {
        return labelVal.contains("N");
    }

    private Candidate filterByPOSTag(Candidate candidate) {
        candidate.setLabels(
                candidate
                    .getLabels()
                        .stream()
                        .filter(l -> hasValidPosTag(l.value()))
                        .collect(Collectors.toList()));
        return candidate;
    }

    private boolean hasValidPosTag(String labelVal) {
        return (labelVal.contains("NN")
                || labelVal.contains("VB")
                || labelVal.equals("ADJA")
                || labelVal.equals("NE"));
    }
}
