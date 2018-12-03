package models;

import de.linguatools.disco.CorruptConfigFileException;
import nlp.NLProcessor;
import nlp.BasicNLProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RecommendationList {
    private Map<Integer, Candidate> candidates;
    private final Logger logger = LoggerFactory.getLogger(RecommendationList.class);
    private final NLProcessor processor;

    private RecommendationList(String text, String configFile, String corpusFile) throws IOException, CorruptConfigFileException {
        processor = new BasicNLProcessor(configFile, corpusFile);
        candidates = processor.extractNounPhrases(text);
        logger.debug("Noun phrase extraction produced list of " + candidates.values().size() + " candidates: " + String.valueOf(this.asStringList()));
    }

    public RecommendationList filterPosTags() {
        candidates = processor.filterPOSTags(candidates);
        logger.debug("POSTag filtering restricted candidate list to " + candidates.values().size() + ": " + String.valueOf(this.asStringList()));
        return this;
    }


    public RecommendationList shorterThan(int length) {
        candidates = processor.restrictToTermLength(candidates, length);
        logger.debug("Length restricted candidate list to " + candidates.values().size() + ": " + String.valueOf(this.asStringList()));
        return this;
    }


    public RecommendationList containingNouns() {
        candidates = processor.restrictToNouns(candidates);
        logger.debug("ContainingNouns restricted candidate list to " + candidates.values().size() + ": " + String.valueOf(this.asStringList()));
        return this;
    }

    public RecommendationList evaluateSpecificity() {
        candidates = processor.evaluateSpecificity(candidates);
        return this;
    }

    public static RecommendationList create(String text, String configFile, String corpusFile) throws IOException, CorruptConfigFileException {
        return new RecommendationList(text, configFile, corpusFile);
    }

    public RecommendationList confidence(int confVal) {
        candidates = processor.filterByRating(candidates, confVal);
        logger.debug("Confidence filtering restricted candidate list to " + candidates.values().size() + ": " + String.valueOf(this.asStringList()));
        return this;
    }

    public List<String> asStringList() {
        return candidates
                .values()
                .stream()
                .map(Candidate::toString)
                .collect(Collectors.toList());
    }
}
