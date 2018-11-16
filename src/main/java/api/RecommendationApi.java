package api;

import models.ProcessingRequest;
import nlp.SimpleNLProcessor;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/recommendations")
public class RecommendationApi {

    @POST
    @Path("/create")
    public List<String> getRecommendations(ProcessingRequest request) {
        return new SimpleNLProcessor(request.getText())
                .getNounPhrases();
    }


}
