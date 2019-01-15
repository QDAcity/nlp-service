package nlp;

import de.linguatools.disco.CorruptConfigFileException;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import models.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(BasicNLProcessor.class);
    private CoreDocument doc;
    private final StanfordCoreNLP pipeline;
    private final CandidateProcessor candidateProcessor;


    public BasicNLProcessor(String configFile, String corpusFile) throws IOException, CorruptConfigFileException {
        InputStream propStream = new FileInputStream(configFile);
        Properties props = new Properties();
        props.load(propStream);
        pipeline = new StanfordCoreNLP(props);
        candidateProcessor = new CandidateProcessor(corpusFile);
    }

    @Override
    public Map<Integer, Candidate> extractNounPhrases(String text) {
        doc = new CoreDocument(text);
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
                Candidate np = candidateProcessor.createCandidate(tree);
                nps.put(np.hashCode(), np);
            }
        }
        return nps;
    }

    @Override
    public Map<Integer, Candidate> filterByRating(Map<Integer, Candidate> candidates, double confVal) {
        return restrictTo(candidates, cand -> cand.confidence() >= confVal);
    }

    @Override
    public Map<Integer, Candidate> restrictToNouns(Map<Integer, Candidate> candidates) {
        return restrictTo(candidates, candidateProcessor::containsNoun);
    }

    @Override
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

    @Override
    public Map<Integer, Candidate> filterPOSTags(Map<Integer, Candidate> candidates) {
        return candidates
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Entry::getKey, v -> candidateProcessor.filterByPOSTag(v.getValue())));
    }

    @Override
    public Map<Integer, Candidate> evaluateSpecificity(Map<Integer, Candidate> candidates) {
        return candidates
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Entry::getKey, v -> candidateProcessor.evaluateSpecificity(v.getValue(), doc)));
    }

}
