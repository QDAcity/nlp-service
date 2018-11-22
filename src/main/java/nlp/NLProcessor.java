package nlp;

import models.Candidate;

import java.util.Map;

public interface NLProcessor {

    Map<Integer, Candidate> extractNounPhrases(String text);

    Map<Integer, Candidate> restrictToNouns(Map<Integer, Candidate> candidates);

    Map<Integer, Candidate> restrictToTermLength(Map<Integer, Candidate> candidates, int length);

    Map<Integer, Candidate> filterPOSTags(Map<Integer, Candidate> candidates);

}
