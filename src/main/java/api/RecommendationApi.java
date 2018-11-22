package api;

import models.ProcessingRequest;
import models.RecommendationList;

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
    @Path("/RecommendationList")
    public RecommendationList getRecommendations(ProcessingRequest request) throws IOException {
        return RecommendationList
                .create(request.getText())
                .containingNouns()
                .filterPosTags()
                .shorterThan(4);
    }

    @POST
    @Path("/strings")
    public List<String> getRecommendationStrings(ProcessingRequest request) throws IOException {
        return getRecommendations(request).asStringList();
    }

}
