package parsers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parses a compound file.
 * @author Séverine Liegeois
 */
public class CompoundParser {
	/**
	 * Creates a new parser.
	 */
	public CompoundParser(){}
	
	/**
	 * Extracts the compound name from the text file and returns it.
	 * @param f file in the compound directory
	 * @return name of the compound
	 */
	public String compoudParser(File f){
		FileReader input = null;
		try {
			input = new FileReader(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(input);
		String strLine;
		// reads file line after line
		String name = null;
		try {
			while((strLine = br.readLine()) != null){
				if (strLine.startsWith("NAME")){
					name = strLine.split("NAME")[1];
					name = name.trim();
					name = name.replaceAll(";", "");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}
}
