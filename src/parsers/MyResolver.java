package parsers;
import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Allows to ignore the DTD validation in kgml files.
 * @author Severine Liegeois
 *
 */
public class MyResolver implements EntityResolver {
	/**
	 * Replaces the system identifier with nothing, to avoid DTD validation.
	 */
	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		// TODO Auto-generated method stub
		return new InputSource(new StringReader(""));
	}
	 
}
