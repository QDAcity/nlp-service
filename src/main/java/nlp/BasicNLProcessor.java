package nlp;

import de.linguatools.disco.CorruptConfigFileException;
import de.linguatools.disco.DISCO;
import edu.stanford.nlp.ling.CoreLabel;
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
    private final CorpusAdapter referenceCorpus;


    public BasicNLProcessor(String configFile) throws IOException, CorruptConfigFileException {
        InputStream propStream = new FileInputStream(configFile);
        Properties props = new Properties();
        props.load(propStream);
        pipeline = new StanfordCoreNLP(props);
        referenceCorpus = new DiscoAdapter("discoresources/en.denseMatrix");
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
                //construct candidate
                Candidate np = new Candidate(tree);
                nps.put(np.hashCode(), np);
            }
        }
        return nps;
    }

    @Override
    public Map<Integer, Candidate> restrictToNouns(Map<Integer, Candidate> candidates) {
        return restrictTo(candidates, this::containsNoun);
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
                .collect(Collectors.toMap(Entry::getKey, v -> filterByPOSTag(v.getValue())));
    }

    @Override
    public Map<Integer, Candidate> evaluateSpecificity(Map<Integer, Candidate> candidates) {
        return candidates
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Entry::getKey, v -> evaluateSpecificity(v.getValue())));
    }

    @Override
    public Map<Integer, Candidate> filterByRating(Map<Integer, Candidate> candidates, int confVal) {
        return restrictTo(candidates, cand -> cand.getConfidence() >= confVal);
    }

    private Candidate evaluateSpecificity(Candidate candidate) {
        long globalWordCount = referenceCorpus.numberOfWords();
        long localWordCount = doc.tokens().size();

        try {
            long maxFrequency = 0;
            for(CoreLabel label: restrictToNouns(candidate).getLabels()) {
                String word = label.lemma();
                long localOccurrences = countOccurences(word, doc);
                long globalOccurrences = referenceCorpus.countOccurences(word);
                long frequency = localOccurrences * globalWordCount - globalOccurrences * localWordCount; //overflow possible?
                logger.info("Computing word frequency for " + word + ": " + frequency);
                if(frequency > maxFrequency) {
                    maxFrequency = frequency;
                }
            }
            candidate.updateConfidence(maxFrequency);
        } catch (IOException e) {
            logger.warn(e.toString());
        }
        return candidate;
    }

    private long countOccurences(String s, CoreDocument doc) {
        return doc
                .tokens()
                .stream()
                .filter(t -> t.originalText().equals(s))
                .count();
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

    private Candidate restrictToNouns(Candidate candidate) {
        Candidate newCand = new Candidate(candidate);
        newCand.setLabels(
                candidate
                        .getLabels()
                        .stream()
                        .filter(coreLabel -> coreLabel.value().equals("NN"))
                        .collect(Collectors.toList())
        );
        return newCand;
    }

    private boolean hasValidPosTag(String labelVal) {
        return (labelVal.contains("NN")
                || labelVal.contains("VB")
                || labelVal.equals("ADJA")
                || labelVal.equals("NE"));
    }

}
