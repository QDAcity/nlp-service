package models;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Tree;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Candidate {
    private List<CoreLabel> labels;
    private final Tree tree;

    private double frequencyRating = 0;

    public Candidate(Tree constituents, List<CoreLabel> labels) {
        this.labels = Collections.unmodifiableList(labels);
        this.tree = constituents;
    }

    /**
     * Get a confidence of this candidates probability of being a glossary keyword.
     * @return the keyword probability
     */
    public double confidence() {
        return frequencyRating;
    }

    public Candidate withLabels(List<CoreLabel> labels) {
        return new Candidate(tree, labels);
    }

    public List<CoreLabel> getLabels() {
        return labels;
    }

    public Tree getTree() {
        return tree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate that = (Candidate) o;
        return Objects.equals(labels, that.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labels);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (CoreLabel label : labels) {
            builder.append(label.originalText())
                    .append(" ");
        }
        builder.append("|").append(confidence());
        return builder.toString();
    }

    public String originalText() {
        StringBuilder builder = new StringBuilder();
        for (CoreLabel label : labels) {
            builder.append(label.originalText())
                    .append(" ");
        }
        String oText = builder.toString();
        return oText.substring(0, oText.length() - 1); //remove last space
    }

    public String lemmatizedText() {
        StringBuilder builder = new StringBuilder();
        for (CoreLabel label : labels) {
            builder.append(label.lemma())
                    .append(" ");
        }
        String oText = builder.toString();
        return oText.substring(0, oText.length() - 1); //remove last space
    }

    public void setFrequencyRating(double frequencyRating) {
        this.frequencyRating = frequencyRating;
    }
}
