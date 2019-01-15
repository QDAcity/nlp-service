package api;

import de.linguatools.disco.CorruptConfigFileException;
import models.ProcessingRequest;
import models.RecommendationList;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/recommendations")
public class RecommendationApi {
    private final String configFile;
    private final String corpusFile;

    public RecommendationApi(String configFile, String corpusFile) {
        this.configFile = configFile;
        this.corpusFile = corpusFile;
    }

    @POST
    @Path("/create")
    public List<String> getRecommendationStrings(
            ProcessingRequest request,
            @QueryParam("length") @DefaultValue("4") int length,
            @QueryParam("confidence") @DefaultValue("0.05") float confidence) throws IOException, CorruptConfigFileException {
        return RecommendationList
                .create(request.getText(), configFile, corpusFile)
                .containingNouns()
                .shorterThan(length)
                .evaluateSpecificity()
                .confidence(confidence)
                .asStringList();
    }

}
