import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dexter on 2014-03-26.
 */

public class SongUnitGenerator {

    public static final List<SongUnit> generate(Emotion emotion, String input, Document params, Document pitchPatterns, Document rhythmPatterns) throws XPathExpressionException {
        List<SongUnit> songUnits = new ArrayList<SongUnit>();
        NodeList words = params.getElementsByTagName("t");
        // Loop every word
        for (int i = 0; i < words.getLength(); i++){
            Element word = (Element) words.item(i);
            System.out.println("Current word: " + word.getTextContent());
            NodeList syllables = word.getElementsByTagName("syllable");
            if (syllables.getLength() == 0){
                break;
            }

            // Loop every syllable in the word
            for (int j = 0; j < syllables.getLength(); j++) {
                Node syllable = syllables.item(j);
                //TODO Use syllable attributes (formants etc) to determine pitch pattern
            }
            System.out.println("Word length: " + syllables.getLength());
            boolean end = (i == words.getLength());

            String rhythmPatternForWord = findPatternForWord(rhythmPatterns, emotion, syllables.getLength(), end);
            System.out.println("Rhytm pattern: " + rhythmPatternForWord);

            String pitchPatternForWord = findPatternForWord(pitchPatterns, emotion, syllables.getLength(), end);
            System.out.println("Pitch patterns: " + pitchPatternForWord);

            String[] splitRhytms = rhythmPatternForWord.split(" ");
            String[] splitPitches = pitchPatternForWord.split(" ");
            for (int j = 0; j < splitRhytms.length; j++) {
                float pitch = Note.getFrequency(Note.A3, Integer.valueOf(splitPitches[j]));
                float duration = Float.valueOf(splitRhytms[j]);
                SongUnit unit = new SongUnit(pitch, duration, 200);
                songUnits.add(unit);
            }
            System.out.println();
        }

        return songUnits;
    }

    private static String findPatternForWord(Document patterns, Emotion emotion, int length, boolean end)
            throws XPathExpressionException {
        String emotionString = emotion.toString().toLowerCase();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr = null;
        expr = xPath.compile(String.format("//emotion[@type='%s']/patterns[@length='%d']/p", emotionString, length));
        NodeList nl = (NodeList) expr.evaluate(patterns, XPathConstants.NODESET);
        Random rand = new Random();
        String randomPattern = nl.item(rand.nextInt(nl.getLength())).getTextContent();
        return randomPattern;
    }
}
