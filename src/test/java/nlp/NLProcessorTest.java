package nlp;


import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class NLProcessorTest {
    private static String TEXT =
            "As it turned out, there was no mischief." +
                    " The mystery box, seen in photos all over social media," +
                    " contained not ballots but supplies — pens, envelopes, " +
                    "signs advising people to Vote Here. But it was too late: " +
                    "the fluid vote tally in three statewide races and repeated" +
                    " claims of possible fraud by Florida’s Republican governor, " +
                    "Rick Scott — who is running in a still-contested Senate race — " +
                    "were enough to convince many Floridians that widespread election theft was underway.";


    @Test
    public void testNounPhrases() {
        TEXT = "This sentence is short.";
        SimpleNLProcessor proc = new SimpleNLProcessor(TEXT);
        List<String> result = proc.getNounPhrases();

        for (String np: result) {
//            System.out.println(np);
        }
    }


}
