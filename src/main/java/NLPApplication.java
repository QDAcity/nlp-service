import api.RecommendationApi;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class NLPApplication extends Application<NLPConfig> {

    public static void main(String[] args) throws Exception {
        new NLPApplication().run(args);
    }

    public void run(NLPConfig nlpConfig, Environment environment) throws Exception {
        final RecommendationApi recommendationApi = new RecommendationApi(nlpConfig.getConfigFile(), nlpConfig.getCorpusFile());
        environment.jersey().register(recommendationApi);
    }




}
