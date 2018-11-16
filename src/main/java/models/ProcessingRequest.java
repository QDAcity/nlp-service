package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessingRequest {
    @JsonProperty
    private String text;

    @JsonCreator
    public ProcessingRequest(@JsonProperty("text") String text) {
        this.text = text;
    }

    @JsonIgnore
    public String getText() {
        return text;
    }
}
