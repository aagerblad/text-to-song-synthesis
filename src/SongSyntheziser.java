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

    public static final String INPUT_STRING = "Happy birthday to you!";

    public static void main(String[] args) throws MaryConfigurationException,
            SynthesisException, IOException,
            TransformerFactoryConfigurationError, TransformerException,
            LineUnavailableException, SAXException,
            ParserConfigurationException {
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


        List<String> vowels = Arrays.asList("{", "i", "r=", "EI", "u"); //TODO Put this in another class as a field, and add all relevant strings
        List<SongUnit> songUnits = SongUnitGenerator.generate(Emotion.HAPPY, INPUT_STRING, params, pitchPatterns, rhythmPatterns);
//        List<SongUnit> songUnits = new ArrayList<SongUnit>(Arrays.asList(new SongUnit[]{new SongUnit(261.63f, 210, 1),
//                new SongUnit(261.63f, 210, 1), new SongUnit(293.66f, 425, 1), new SongUnit(261.63f, 325, 1),
//                new SongUnit(349.23f, 380, 1), new SongUnit(329.63f, 640, 1)}));
        System.out.println(songUnits.size() + " " + syllables.getLength());
        setPhonemeAttributes(syllables, vowels, songUnits);

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

    private static void setPhonemeAttributes(NodeList syllables, List<String> vowels, List<SongUnit> songUnits) {
        // Loop all syllables
        for (int i = 0; i < syllables.getLength(); i++) {
            Node syllable = syllables.item(i);
            NodeList phonemes = syllable.getChildNodes();

            int duration = songUnits.get(i).getDuration();
            for (int j = 0; j < phonemes.getLength(); j++) {
                Node phoneme = phonemes.item(j);
                if (!vowels.contains(phoneme.getAttributes().getNamedItem("p").getNodeValue())){
                    System.out.println("consonant: " + phoneme.getAttributes().getNamedItem("d").getNodeValue());
                    duration -= Integer.parseInt(phoneme.getAttributes().getNamedItem("d").getNodeValue());
                }
            }

            // Loop all phonemes in syllable
            for (int j = 0; j < phonemes.getLength(); j++) {
                Node phoneme = phonemes.item(j);
                if (vowels.contains(phoneme.getAttributes().getNamedItem("p").getNodeValue())){
                    Node d = phoneme.getOwnerDocument().createAttribute("d");
                    d.setNodeValue(String.valueOf(songUnits.get(i).getDuration()));
                    System.out.println("vowel: " + String.valueOf(duration));
                    phoneme.getAttributes().setNamedItem(d);
                }
                Node f0 = phoneme.getOwnerDocument().createAttribute("f0");
                f0.setNodeValue("(100," + songUnits.get(i).getPitch() + ")");
                phoneme.getAttributes().setNamedItem(f0);
            }
            System.out.println();
        }
    }
}
