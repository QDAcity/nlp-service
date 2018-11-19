package models;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.trees.Tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Candidate {
    private final List<CoreLabel> labels = new LinkedList<>();
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
            builder.append(label.lemma());
            builder.append(" ");
        }
        return builder.toString();
    }
}
