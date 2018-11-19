package api;

import models.ProcessingRequest;
import nlp.KeywordExtraction;
import nlp.NLProcessorOld;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/recommendations")
public class RecommendationApi {

    @POST
    @Path("/create")
    public List<String> getRecommendations(ProcessingRequest request) throws IOException {
        return NLProcessorOld
                .nounPhrases(request.getText())
                .recommendations();
    }

    @POST
    @Path("/newapi")
    public List<String> newRecommendations(ProcessingRequest request) throws IOException {
        return KeywordExtraction
                .nounPhrases(request.getText())
                .containingNouns()
                .shorterThan(4)
                .recommendations();
    }

}
