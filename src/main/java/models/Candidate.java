package models;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.trees.Tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Candidate {
    private List<CoreLabel> labels = new LinkedList<>();
    private final Tree tree;

    public Candidate(CoreSentence sentence) {
        this.labels.addAll(sentence.tokens());
        this.tree = sentence.constituencyParse();
    }

    public Candidate(Tree constituents) {
        this.labels.addAll(constituents.taggedLabeledYield());
        this.tree = constituents;
    }

    public boolean containsNoun() {
      return labels
              .stream()
              .anyMatch(l -> isNoun(l.value()));
    }

    private boolean isNoun (String labelVal) {
        return labelVal.contains("N");
    }

    public Candidate filterByPOSTag() {
        labels = labels
                .stream()
                .filter(l -> hasValidPosTag(l.value()))
                .collect(Collectors.toList());
        return this;
    }

    private boolean hasValidPosTag(String labelVal) {
        return (labelVal.contains("NN")
                || labelVal.contains("VB")
                || labelVal.equals("ADJA")
                || labelVal.equals("NE"));
    }

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
        for(CoreLabel label: labels) {
            builder.append(label.lemma())
                    .append("|")
                    .append(label.tag())
                    .append(" ");
        }
        return builder.toString();
    }
}
