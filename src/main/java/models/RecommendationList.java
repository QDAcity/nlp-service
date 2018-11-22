package models;

import nlp.NLProcessor;
import nlp.BasicNLProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RecommendationList {
    private Map<Integer, Candidate> candidates;
    private final NLProcessor processor;

    private RecommendationList(String text) throws IOException {
        processor = new BasicNLProcessor();
        candidates = processor.extractNounPhrases(text);
    }

    public RecommendationList filterPosTags() {
        candidates = processor.filterPOSTags(candidates);
        return this;
    }


    public RecommendationList shorterThan(int length) {
        candidates = processor.restrictToTermLength(candidates, length);
        return this;
    }


    public RecommendationList containingNouns() {
        candidates = processor.restrictToNouns(candidates);
        return this;
    }

    public static RecommendationList create(String text) throws IOException {
        return new RecommendationList(text);
    }

    public List<String> asStringList() {
        return candidates
                .values()
                .stream()
                .map(Candidate::toString)
                .collect(Collectors.toList());
    }
}
