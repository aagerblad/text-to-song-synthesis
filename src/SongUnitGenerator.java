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

    public static final Float KEY = Note.A3;
    public static final int[] PATTERN_TIERS = {5, 10, 20, 40}; // Max number of syllables in 2^index patterns
    private static int tempo = 200;

    public static final List<SongUnit> generate(Emotion emotion, String input, Document params, Document pitchPatterns, Document rhythmPatterns) throws XPathExpressionException {
        List<SongUnit> songUnits = new ArrayList<SongUnit>();
        NodeList words = params.getElementsByTagName("t");
        NodeList allSyllables = params.getElementsByTagName("syllable");
        int numberOfPatterns = 1;

        for (int i = 0; i < PATTERN_TIERS.length; i++) {
            if (allSyllables.getLength() < PATTERN_TIERS[i]) {
                numberOfPatterns = (int) Math.pow(2, i);
                break;
            }
        }

        float syllablesPerPattern = allSyllables.getLength() / numberOfPatterns;
        List<List<Node>> syllableGroups = new ArrayList<List<Node>>();
        int syllableCounter = 0;
        int oldSyllableCounter = 0;
        List<Node> syllableGroup = new ArrayList<Node>();

        for (int i = 0; i < words.getLength(); i++) {
            Element word = (Element) words.item(i);
            NodeList syllables = word.getElementsByTagName("syllable");
            if (syllables.getLength() == 0) {
                break;
            }
            oldSyllableCounter = syllableCounter;
            syllableCounter += syllables.getLength();
            if (syllableCounter < syllablesPerPattern && i != words.getLength() - 1) {
                for (int j = 0; j < syllables.getLength(); j++) {
                    syllableGroup.add(syllables.item(j));
                }
            } else {
                if (syllablesPerPattern - oldSyllableCounter <= syllableCounter - syllablesPerPattern
                        || i == words.getLength() - 1) {
                    for (int j = 0; j < syllables.getLength(); j++) {
                        syllableGroup.add(syllables.item(j));
                    }
                    List<Node> fuckFace = new ArrayList<Node>();
                    fuckFace.addAll(syllableGroup);
                    syllableGroups.add(fuckFace);
                    syllableGroup.clear();
                    syllableCounter = 0;
                } else {
                    List<Node> fuckFace = new ArrayList<Node>();
                    fuckFace.addAll(syllableGroup);
                    syllableGroups.add(fuckFace);
                    syllableGroup.clear();
                    syllableCounter = syllables.getLength();
                    for (int j = 0; j < syllables.getLength(); j++) {
                        syllableGroup.add(syllables.item(j));
                    }
                }
            }
        }

        for (List<Node> group : syllableGroups) {
            for (Node syllable : group) {
                //TODO Use syllable attributes (formants etc) to determine pitch pattern
            }

            boolean end = (syllableGroups.indexOf(group) == syllableGroups.size() - 1);

            String rhythmPatternForWord = findPatternForWord(rhythmPatterns, emotion, group.size(), end);
            System.out.println("Rhythm pattern: " + rhythmPatternForWord);

            String pitchPatternForWord = findPatternForWord(pitchPatterns, emotion, group.size(), end);
            System.out.println("Pitch patterns: " + pitchPatternForWord);

            String[] splitRhythms = rhythmPatternForWord.split(" ");
            String[] splitPitches = pitchPatternForWord.split(" ");
            for (int j = 0; j < splitRhythms.length; j++) {
                float pitch = Note.getFrequency(KEY, Integer.valueOf(splitPitches[j]));
                float duration = Float.valueOf(splitRhythms[j]);
                SongUnit unit = new SongUnit(pitch, duration, tempo);
                songUnits.add(unit);
            }

        }
        return songUnits;


//
//        // Loop every word
//        for (int i = 0; i < words.getLength(); i++){
//            Element word = (Element) words.item(i);
//            System.out.println("Current word: " + word.getTextContent());
//            NodeList syllables = word.getElementsByTagName("syllable");
//            if (syllables.getLength() == 0){
//                break;
//            }
//
//            // Loop every syllable in the word
//            for (int j = 0; j < syllables.getLength(); j++) {
//                Node syllable = syllables.item(j);
//                //TODO Use syllable attributes (formants etc) to determine pitch pattern
//            }
//            System.out.println("Word length: " + syllables.getLength());
//            boolean end = (i == words.getLength() - 1);
//
//            String rhythmPatternForWord = findPatternForWord(rhythmPatterns, emotion, syllables.getLength(), end);
//            System.out.println("Rhythm pattern: " + rhythmPatternForWord);
//
//            String pitchPatternForWord = findPatternForWord(pitchPatterns, emotion, syllables.getLength(), end);
//            System.out.println("Pitch patterns: " + pitchPatternForWord);
//
//            String[] splitRhythms = rhythmPatternForWord.split(" ");
//            String[] splitPitches = pitchPatternForWord.split(" ");
//            for (int j = 0; j < splitRhythms.length; j++) {
//                float pitch = Note.getFrequency(KEY, Integer.valueOf(splitPitches[j]));
//                float duration = Float.valueOf(splitRhythms[j]);
//                SongUnit unit = new SongUnit(pitch, duration, 200);
//                songUnits.add(unit);
//            }
//            System.out.println();
//        }
//
//        return songUnits;
    }

    private static String findPatternForWord(Document patterns, Emotion emotion, int length, boolean end)
            throws XPathExpressionException {
        String emotionString = emotion.toString().toLowerCase();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr = null;
        System.out.println(String.valueOf(end));
        if (end) {
            expr = xPath.compile(String.format("//emotion[@type='%s']/patterns[@length='%d']/p[@type='end']", emotionString, length));
        } else {
            expr = xPath.compile(String.format("//emotion[@type='%s']/patterns[@length='%d']/p", emotionString, length));
        }
        NodeList nl = (NodeList) expr.evaluate(patterns, XPathConstants.NODESET);
        Random rand = new Random();
        String randomPattern = nl.item(rand.nextInt(nl.getLength())).getTextContent();
        return randomPattern;
    }
}
