package nlp;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import models.Candidate;
import models.RecommendationList;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class NLProcessorNew implements NLProcessor{
    private CoreDocument doc;
    private RecommendationList recommendations;

    @Override
    public NLProcessor init(String text) throws IOException {
        this.doc = new CoreDocument(text);
        InputStream propStream = new FileInputStream("german.properties");
        Properties props = new Properties();
        props.load(propStream);
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.annotate(doc);
        recommendations = new RecommendationList(doc.sentences());
        return this;
    }

    @Override
    public NLProcessor shorterThan(int length) {
        recommendations.restrictToSize(length);
        return this;
    }

    @Override
    public NLProcessor containingNouns() {
        recommendations.restrictToNouns();
        return this;
    }

    @Override
    public List<String> recommendations() {
        return recommendations
                .getCandidates()
                .values()
                .stream()
                .map(Candidate::toString)
                .collect(Collectors.toList());
    }
}
