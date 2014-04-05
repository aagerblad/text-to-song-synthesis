import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.MaryAudioUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongSyntheziser {

    /**
     * @param args
     * @throws MaryConfigurationException
     * @throws SynthesisException
     * @throws IOException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     * @throws LineUnavailableException
     * @throws SAXException
     * @throws ParserConfigurationException
     */

    //public static final String INPUT_STRING = "I am a computer that really likes to sing";
    public static final String INPUT_STRING = "This was a triumph making a note here huge success";
//    public static final String INPUT_STRING = "I am I am I am I am I am I am I am";
    public static final String VOWELS = "A{6QE@3IO29&U}VY=~";
    private static final boolean FORCE_NOTE_LENGTH = true;
    private final List<String> CLOSED_PHONEMES = new ArrayList<String>(Arrays.asList("u", "@U", "y", "i", "I"));
    private final List<String> NEAR_CLOSED_PHONEMES = new ArrayList<String>(Arrays.asList("U", "Y"));
    private final List<String> CLOSED_MID_PHONEMES = new ArrayList<String>(Arrays.asList("o", "2", "e"));
    private final List<String> MID_PHONEMES = new ArrayList<String>(Arrays.asList("@"));
    private final List<String> OPEN_MID_PHONEMES = new ArrayList<String>(Arrays.asList("V", "O", "3", "9", "E"));
    private final List<String> NEAR_OPEN_PHONEMES = new ArrayList<String>(Arrays.asList("6", "{"));
    private final List<String> OPEN_PHONEMES = new ArrayList<String>(Arrays.asList("Q", "A", "&"));

    public static void main(String[] args) throws MaryConfigurationException,
            SynthesisException, IOException,
            TransformerFactoryConfigurationError, TransformerException,
            LineUnavailableException, SAXException,
            ParserConfigurationException, XPathExpressionException {
        System.out.println("Initializing...");
        MaryInterface marytts = new LocalMaryInterface();
        marytts.setOutputType("ACOUSTPARAMS");
        Document params = marytts
                .generateXML(INPUT_STRING);
        NodeList syllables = params.getElementsByTagName("syllable");

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document pitchPatterns = docBuilder.parse("xml/pitch-patterns.xml");
        Document rhythmPatterns = docBuilder.parse("xml/rhythm-patterns.xml");


        List<SongUnit> songUnits = SongUnitGenerator.generate(Emotion.SAD, params, pitchPatterns, rhythmPatterns);
        System.out.println("Songunits: " + songUnits.size() + "\nInput syllables: " + syllables.getLength() + "\n");
        setPhonemeAttributes(syllables, songUnits);

        writeOutputXML(params);

        marytts.setInputType("ACOUSTPARAMS");
        marytts.setOutputType("AUDIO");
        AudioInputStream audio = marytts.generateAudio(params);

        System.out.println("done!");
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();
        while (clip.getMicrosecondLength() != clip.getMicrosecondPosition()) {
            //Let the clip finish playing before closing
        }
        clip.stop();
        audio.close();
        System.exit(0);
//        createWav(audio);
    }


    private static void writeOutputXML(Document params) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
        Result output = new StreamResult(new File("xml/output.xml"));
        Source input = new DOMSource(params);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(input, output);
    }

    private static void createWav(AudioInputStream audio) throws IOException {
        MaryAudioUtils.writeWavFile(
                MaryAudioUtils.getSamplesAsDoubleArray(audio),
                "thisIsMyText.wav", audio.getFormat());
    }

    private static void setPhonemeAttributes(NodeList syllables, List<SongUnit> songUnits) {
        // Loop all syllables
        for (int i = 0; i < syllables.getLength(); i++) {
            Node syllable = syllables.item(i);
            NodeList phonemes = syllable.getChildNodes();

            System.out.println("Syllable: " + syllable.getAttributes().getNamedItem("ph").getTextContent());
            // Loop all phonemes in syllable
            if (FORCE_NOTE_LENGTH) {
                setNodesWithTempo(songUnits, i, phonemes);
            }else {
                setNodes(songUnits, i, phonemes);
            }
            System.out.println();
        }
    }

    private static void setNodes(List<SongUnit> songUnits, int i, NodeList phonemes) {
        for (int j = 0; j < phonemes.getLength(); j++) {
            Node phoneme = phonemes.item(j);
            for (char foo : phoneme.getAttributes().getNamedItem("p").getNodeValue().toCharArray()) {
                if (VOWELS.toLowerCase().contains(String.valueOf(foo).toLowerCase())) {
                    Node d = phoneme.getOwnerDocument().createAttribute("d");
                    d.setNodeValue(String.valueOf(songUnits.get(i).getDuration()));
                    phoneme.getAttributes().setNamedItem(d);
                    break;
                }
            }

            Node f0 = phoneme.getOwnerDocument().createAttribute("f0");
            f0.setNodeValue(String.format("(1,%s)(100,%s)", songUnits.get(i).getPitch(),
                    songUnits.get(i).getPitch()));
            phoneme.getAttributes().setNamedItem(f0);

            System.out.println("Phoneme: " + phoneme.getAttributes().getNamedItem("p").getTextContent()
                    + "     Duration: " + phoneme.getAttributes().getNamedItem("d").getTextContent());
        }
    }

    private static void setNodesWithTempo(List<SongUnit> songUnits, int i, NodeList phonemes) {
        List<Integer> durations = new ArrayList<Integer>();
        int totalDuration = 0;
        for (int j = 0; j < phonemes.getLength(); j++) {
            Node phoneme = phonemes.item(j);
            int phonemeDuration = Integer.valueOf(phoneme.getAttributes().getNamedItem("d").getTextContent());
            for (char ch : phoneme.getAttributes().getNamedItem("p").getNodeValue().toCharArray()) {
                if (VOWELS.toLowerCase().contains(String.valueOf(ch).toLowerCase())) {
                    phonemeDuration = phonemeDuration * 2;
                    break;
                }
            }
            durations.add(phonemeDuration);
            totalDuration += phonemeDuration;
        }

        for (int j = 0; j < durations.size(); j++) {
            float percentage = ((float) durations.get(j)) / totalDuration;
            int duration = Math.round(percentage * songUnits.get(i).getDuration());
            durations.set(j, duration);
        }

        for (int j = 0; j < phonemes.getLength(); j++) {
            Node phoneme = phonemes.item(j);
            Node d = phoneme.getOwnerDocument().createAttribute("d");
            d.setNodeValue(String.valueOf(durations.get(j)));
            phoneme.getAttributes().setNamedItem(d);

            Node f0 = phoneme.getOwnerDocument().createAttribute("f0");
            f0.setNodeValue(String.format("(1,%s)(100,%s)", songUnits.get(i).getPitch(),
                    songUnits.get(i).getPitch()));
            phoneme.getAttributes().setNamedItem(f0);

            System.out.println("Phoneme: " + phoneme.getAttributes().getNamedItem("p").getTextContent()
                    + "     Duration: " + phoneme.getAttributes().getNamedItem("d").getTextContent());
        }
        System.out.println("Songunit duration: " + songUnits.get(i).getDuration());
    }
}
