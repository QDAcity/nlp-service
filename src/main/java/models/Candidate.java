package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Candidate {
    private List<CoreLabel> labels;

    private double frequencyRating = 0;

    public Candidate(List<CoreLabel> labels) {
        this.labels = Collections.unmodifiableList(labels);
    }

    /**
     * Get a confidence of this candidates probability of being a glossary keyword.
     * @return the keyword probability
     */
    @JsonProperty
    public double confidence() {
        return frequencyRating;
    }

    @JsonIgnore
    public Candidate withLabels(List<CoreLabel> labels) {
        return new Candidate(labels);
    }

    @JsonIgnore
    public List<CoreLabel> getLabels() {
        return labels;
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

    @JsonProperty
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
