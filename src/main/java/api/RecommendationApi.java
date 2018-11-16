package api;

import models.ProcessingRequest;
import models.Tree;
import nlp.RequestManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

@Path("requests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecommendationApi {
    private final RequestManager requestManager = RequestManager.getInstance();

    @GET
    public Collection<ProcessingRequest> getAllRequests() {
        return requestManager.getAll();
    }

    @POST
    public int addRequest(ProcessingRequest request) {
        requestManager.addRequest(request);
        return request.getId();
    }

    @GET
    @Path("{requestId}")
    public ProcessingRequest getRequest(@PathParam("requestId") int id) {
        return requestManager.getRequest(id);
    }

//    @GET
//    @Path("{requestId}/tree")
//    public Tree getTree(@PathParam("requestId") int id) {
//        return new Tree(requestManager
//                .getRequest(id)
//                .getTree());
//    }

    @GET
    @Path("{requestId}/nounphrases")
    public List<String> getNounPhrases(@PathParam("requestId") int id) {
        return requestManager
                .getRequest(id)
                .getNounPhrases();
    }
}
