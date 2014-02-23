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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import java.io.IOException;

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
	public static void main(String[] args) throws MaryConfigurationException,
			SynthesisException, IOException,
			TransformerFactoryConfigurationError, TransformerException,
			LineUnavailableException, SAXException,
			ParserConfigurationException {
        System.out.println("Initializing...");
        MaryInterface marytts = new LocalMaryInterface();
		marytts.setOutputType("ACOUSTPARAMS");
		Document params = marytts
				.generateXML("I am a computer. Pleased to meet you.");
		NodeList nl = params.getElementsByTagName("syllable");

        // Loop all syllables
        for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			NodeList phonemes = node.getChildNodes();

			// Loop all phonemes in syllable
			for (int j = 0; j < phonemes.getLength(); j++) {
				Node phoneme = phonemes.item(j);
				Node f0 = phoneme.getOwnerDocument().createAttribute("f0");
				f0.setNodeValue("(100,220)");
				phoneme.getAttributes().setNamedItem(f0);
			}
		}

		// Optional code for reading from xml file
		// Transformer transformer = TransformerFactory.newInstance()
		// .newTransformer();
		// Result output = new StreamResult(new File("xml/output.xml"));
		// Source input = new DOMSource(params);
		// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// transformer.setOutputProperty(
		// "{http://xml.apache.org/xslt}indent-amount", "2");
		// transformer.transform(input, output);

		marytts.setInputType("ACOUSTPARAMS");
		marytts.setOutputType("AUDIO");
		// DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		// DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		// Document params2 = docBuilder.parse("xml/output.xml");
		AudioInputStream audio = marytts.generateAudio(params);

        System.out.println("done!");
        Clip clip = AudioSystem.getClip();
		clip.open(audio);
		clip.start();
//      Optional code for writing to wav file.
//		MaryAudioUtils.writeWavFile(
//				MaryAudioUtils.getSamplesAsDoubleArray(audio),
//				"thisIsMyText.wav", audio.getFormat());
	}
}
