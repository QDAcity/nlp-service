package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.nlp.ling.CoreLabel;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CandidateTest {

    @Test
    public void testCandidateSerialization() {
        CoreLabel label1 = new CoreLabel();
        label1.setOriginalText("Hans");
        CoreLabel label2 = new CoreLabel();
        label2.setOriginalText("Wurst");
        Candidate candidate = new Candidate(Arrays.asList(label1, label2));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = mapper.valueToTree(candidate);

        String expected = "{\"originalText\":\"Hans Wurst\",\"confidence\":0.0}";
        assertEquals(expected, result.toString());
    }
}
