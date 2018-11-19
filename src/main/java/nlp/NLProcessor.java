package nlp;

import java.io.IOException;
import java.util.List;

public interface NLProcessor {

    NLProcessor init(String text) throws IOException;

    default NLProcessor containingNouns() {
        return this;
    }

    default NLProcessor shorterThan(int length) {
        return this;
    }

    default NLProcessor filterPOSTags() {
        return this;
    }

    default NLProcessor withSpecifityThreshold() {
        return this;
    }

    List<String> recommendations();
}
