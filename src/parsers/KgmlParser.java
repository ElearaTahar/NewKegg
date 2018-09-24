package parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.DOMException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import xml.Pathway;

/**
 * Reads a XML file and extracts the relevant data.
 * @author Severine Liegeois
 */
public class KgmlParser {
	/**
	 * Creates a new kgml parser.
	 */
	public KgmlParser(){}
	
	/**
	 * Extracts relevant info from a kgml file.
	 * @param f kgml file
	 * @param p list of the Pathway instances, contains the relevant information needed
	 */
	public void kgmlParser(File f, ArrayList<Pathway> p ){
		try {
		     XMLReader reader = XMLReaderFactory.createXMLReader();
		     reader.setEntityResolver(new MyResolver());
		     MyXMLHandler handler = new MyXMLHandler();
		     reader.setContentHandler(handler);
		     
		     InputSource inputSource;
		     try {
				inputSource = new InputSource(new FileInputStream(f));
				reader.parse(inputSource);
		     } catch (FileNotFoundException e) {
				e.printStackTrace();
		     } catch (IOException e) {
				e.printStackTrace();
		     }
		     p.add(handler.getPathway());
		} catch (DOMException e) {
	     e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
	     e.printStackTrace();
		} catch (SAXException e) {
	     e.printStackTrace();
		}
	}
}
