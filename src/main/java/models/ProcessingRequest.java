package models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nlp.NLProcessor;
import nlp.SimpleNLProcessor;

import java.util.List;
import java.util.Objects;

public class ProcessingRequest {
    @JsonIgnore
    private final SimpleNLProcessor processor;

    @JsonProperty()
    private final String text;

    @JsonProperty
    private final int id;

    @JsonCreator
    public ProcessingRequest(@JsonProperty("text") String text) {
        this.text = text;
        this.id = Objects.hash(text);
        this.processor = new SimpleNLProcessor(text);
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

//    @JsonIgnore
//    public String getTree() {
////        return processor.getTree().toString();
//        return null;
//    }

    @JsonIgnore
    public List<String> getNounPhrases() {
        return processor.getNounPhrases();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessingRequest that = (ProcessingRequest) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
