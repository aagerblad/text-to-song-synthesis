import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dexter on 2014-03-26.
 */
public class SongUnitGenerator {

    public static final List<SongUnit> generate(Emotion emotion, String input, Document params, Document pitchPatterns, Document rhythmPatterns) {
        List<SongUnit> songUnits = new ArrayList<SongUnit>();
        NodeList words = params.getElementsByTagName("t");
        for (int i = 0; i < words.getLength(); i++){
            Node word = words.item(i);
            NodeList syllables = word.getChildNodes();
            for (int j = 0; j < syllables.getLength(); j++) {
                Node syllable = syllables.item(j);
                //TODO Use syllable attributes (formants etc) to determine pitch pattern
            }

            NodeList rhythmPatternsForEmotion = findPatternsForEmotion(rhythmPatterns, emotion);
            NodeList pitchPatternsForEmotion = findPatternsForEmotion(pitchPatterns, emotion);

        }

        return null;
    }

    private static NodeList findPatternsForEmotion(Document rythmPatterns, Emotion emotion) {
        //TODO Actually use the emotion here
        NodeList emotions = rythmPatterns.getElementsByTagName("emotion");
        NodeList patternsForEmotion = null;
        for (int j = 0; j < emotions.getLength(); j++) {
            Node emotionNode = emotions.item(j);
            if (emotionNode.getAttributes().getNamedItem("type").equals("happy")){
                patternsForEmotion = emotionNode.getChildNodes();
            }
        }
        return patternsForEmotion;
    }
}
