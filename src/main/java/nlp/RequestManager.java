package nlp;

import models.ProcessingRequest;

import java.util.Collection;
import java.util.HashMap;

public class RequestManager {
    private static final RequestManager instance = new RequestManager();
    private final HashMap<Integer, ProcessingRequest> requestsMap = new HashMap<>();

    public static RequestManager getInstance() {
        return instance;
    }

    public Collection<ProcessingRequest> getAll() {
        return requestsMap.values();
    }

    public void addRequest(ProcessingRequest request) {
        requestsMap.put(request.getId(), request);
    }

    public ProcessingRequest getRequest(int id) {
        return requestsMap.get(id);
    }





}
