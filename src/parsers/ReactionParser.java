package parsers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parses a reaction file.
 * @author Severine Liegeois
 *
 */
public class ReactionParser {
	/**
	 * Creates a new reaction parser.
	 */
	public ReactionParser(){}
	
	/**
	 * Extracts the name of the enzyme performing the reaction from the text file
	 * and returns it.
	 * @param f file from the reaction directory
	 * @return name of the enzyme performing the reaction
	 */
	public String reactionParser(File f){
		FileReader input = null;
		try {
			input = new FileReader(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(input);
		String strLine;
		// read file line after line
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
