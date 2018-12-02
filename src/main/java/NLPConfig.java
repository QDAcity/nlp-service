import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class NLPConfig extends Configuration {
    private String configFile;
    private String corpusFile;

    @JsonProperty
    public String getConfigFile() {
        return configFile;
    }

    @JsonProperty
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    @JsonProperty
    public String getCorpusFile() {
        return corpusFile;
    }

    @JsonProperty
    public void setCorpusFile(String corpusFile) {
        this.corpusFile = corpusFile;
    }
}
