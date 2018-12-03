package nlp;

import de.linguatools.disco.CorruptConfigFileException;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import models.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.Collectors;

public class CandidateProcessor {
    private final Logger logger = LoggerFactory.getLogger(CandidateProcessor.class);
    private final CorpusAdapter referenceCorpus;

    protected CandidateProcessor(String corpusFile) throws IOException, CorruptConfigFileException {
        referenceCorpus = new DiscoAdapter(corpusFile);
    }

    protected Candidate evaluateSpecificity(Candidate candidate, CoreDocument doc) {
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

    protected boolean containsNoun(Candidate candidate) {
        return candidate
                .getLabels()
                .stream()
                .anyMatch(l -> isNoun(l.value()));
    }

    private boolean isNoun (String labelVal) {
        return labelVal.contains("N");
    }

    protected Candidate filterByPOSTag(Candidate candidate) {
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
