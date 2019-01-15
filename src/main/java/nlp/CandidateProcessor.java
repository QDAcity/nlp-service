package nlp;

import de.linguatools.disco.CorruptConfigFileException;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.trees.Tree;
import models.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CandidateProcessor {
    private final Logger logger = LoggerFactory.getLogger(CandidateProcessor.class);
    private final CorpusAdapter referenceCorpus;

    protected CandidateProcessor(String corpusFile) throws IOException, CorruptConfigFileException {
        referenceCorpus = new FrequencyListAdapter(corpusFile);
    }

    protected Candidate filterByPOSTag(Candidate candidate) {
        return restrictTo(candidate, this::hasValidPosTag);
    }

    private Candidate restrictToNouns(Candidate candidate) {
        return restrictTo(candidate, this::isNoun);
    }

    private Candidate restrictTo(Candidate candidate, Predicate<CoreLabel> pred) {
        return candidate.withLabels(
                candidate
                        .getLabels()
                        .stream()
                        .filter(pred)
                        .collect(Collectors.toList()));
    }

    protected boolean containsNoun(Candidate candidate) {
        return candidate
                .getLabels()
                .stream()
                .anyMatch(this::isNoun);
    }

    private boolean isNoun (CoreLabel coreLabel) {
        return coreLabel.value().contains("N");
    }

    private boolean hasValidPosTag(CoreLabel coreLabel) {
        String labelVal = coreLabel.value();
        return (labelVal.contains("NN")
                || labelVal.contains("VB")
                || labelVal.equals("ADJA")
                || labelVal.equals("NE"));
    }

    /**
     * Creates a keyword candidate from a given stanford dependency tree.
     * Before creating, punctuation is removed from the labels (labelValue "$").
     * @param tree The tree from which the candidate shall be created.
     * @return The newly created candidate.
     */
    protected Candidate createCandidate(Tree tree) {
        List<CoreLabel> filteredLabels = tree
                .taggedLabeledYield()
                .stream()
                .filter(l -> !l.value().equals("$"))
                .collect(Collectors.toList());
        return new Candidate(tree, filteredLabels);
    }

    protected Candidate evaluateSpecificity(Candidate candidate, CoreDocument doc) {
        long globalWordCount = referenceCorpus.numberOfWords();
        long localWordCount = doc.tokens().size();

        try {
            double maxFrequency = 0;
            for(CoreLabel label: restrictToNouns(candidate).getLabels()) {
                String word = label.lemma().toLowerCase();
                long localOccurrences = countOccurences(word, doc);
                long globalOccurrences = referenceCorpus.countOccurrences(word);
                double ratio = globalWordCount / globalOccurrences;
                double idf = Math.log( ratio );
                double tf = localOccurrences / (double) localWordCount;
                double tfidf = tf * idf;
                logger.info("Computing word frequency for " + word + ": " + tfidf);
                if(tfidf > maxFrequency) {
                    maxFrequency = tfidf;
                }
            }
            candidate.setFrequencyRating(maxFrequency);
        } catch (IOException e) {
            logger.warn(e.toString());
        }
        return candidate;
    }

    private long countOccurences(String s, CoreDocument doc) {
        return doc
                .tokens()
                .stream()
                .filter(t -> t.lemma().equals(s))
                .count();
    }
}
