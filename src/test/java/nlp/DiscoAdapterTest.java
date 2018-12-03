package nlp;

import de.linguatools.disco.CorruptConfigFileException;
import de.linguatools.disco.DISCO;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class DiscoAdapterTest {
    private static DISCO disco;

    @Test
    public void testEnglishCorpus() throws IOException, CorruptConfigFileException {
        disco = DISCO.load("discoresources/cc.en.300-COL.denseMatrix");
        int highFreqencyExpected = disco.frequency("people");
        System.out.println(highFreqencyExpected);
        int lowFrequencyExpected = disco.frequency("hallux");
        System.out.println(lowFrequencyExpected);
        Assert.assertTrue(highFreqencyExpected > lowFrequencyExpected);
    }

    @Test
    public void testGermanCorpus() throws IOException, CorruptConfigFileException {
        disco = DISCO.load("discoresources/de.denseMatrix");
        int highFrequencyExpected = disco.frequency("mensch");
        System.out.println(highFrequencyExpected);
        int lowFrequencyExpected = disco.frequency("banane");
        System.out.println(lowFrequencyExpected);

        Assert.assertTrue(highFrequencyExpected > lowFrequencyExpected);
    }
}
