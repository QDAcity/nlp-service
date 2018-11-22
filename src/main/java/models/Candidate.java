package models;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.trees.Tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Candidate {
    private List<CoreLabel> labels = new LinkedList<>();
    private final Tree tree;
    private int confidence = 0;

    public Candidate(CoreSentence sentence) {
        this.labels.addAll(sentence.tokens());
        this.tree = sentence.constituencyParse();
    }

    public Candidate(Tree constituents) {
        this.labels.addAll(constituents.taggedLabeledYield());
        this.tree = constituents;
    }

    public void setLabels(List<CoreLabel> labels) {
        this.labels = labels;
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
        for(CoreLabel label: labels) {
            builder.append(label.lemma())
                    .append("|")
                    .append(label.tag())
                    .append(" ");
        }
        return builder.toString();
    }
}
