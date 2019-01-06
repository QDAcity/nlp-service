package nlp;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class FrequencyListAdapterTest {

    private static CorpusAdapter adapter;

    @BeforeClass
    public static void setUp() throws IOException {
        adapter = new FrequencyListAdapter("resources/frequencyList");
    }

    @Test
    public void testNumberOfWords() {
        Assert.assertEquals(1065374, adapter.numberOfWords());
    }

    @Test
    public void testCountOccurrences() throws IOException {
        Assert.assertEquals(826051, adapter.countOccurrences("another"));
    }


}
