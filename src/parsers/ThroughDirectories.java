package parsers;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import xml.Bacteria;
import xml.Pathway;

/**
 * Browses through the subfolders and files of the directory selected by the user and containing 
 * the kgml and text files.
 * When a kgml file is encountered, a new instance of the Pathway class is created and the file is parsed.
 * All the Pathway instances are stored into an ArrayList.
 * The text files containing information about reactions and compounds are parsed when encountered, and the information
 * are stored into hash tables.
 * @author Severine Liegeois
 *
 */
public class ThroughDirectories {
	// -------------------------------------------
	// attributes for parsing kgml files
	// -------------------------------------------
	/**
	 * List of Pathway instances, coming from the kgml files parsing.
	 */
	private ArrayList<Pathway> parsedPathways = new ArrayList<>();
	/**
	 * Kgml file parser.
	 */
	private KgmlParser parser = new KgmlParser();
	
	// ---------------------------------------------------
	// attributes for parsing reaction files
	// ---------------------------------------------------
	/**
	 * Hash table containing the ID (key) and name (value) of each enzyme performing a reaction.
	 */
	private Hashtable<String, String> reactions = new  Hashtable<String, String>();
	/**
	 * Text file with information about reactions parser.
	 */
	private ReactionParser reactionParser = new ReactionParser();
	
	// ----------------------------------------------------
	// attributes for parsing compounds files
	// ----------------------------------------------------
	/**
	 * Hash table containing ID (key) and name (value) of each compound.
	 */
	private Hashtable<String, String> compounds = new Hashtable<String, String>();
	/**
	 * Text file with information about compounds parser.
	 */
	private CompoundParser compoundParser = new CompoundParser();
	
	// ----- Constructor -----
	/**
	 * Creates a new ThroughDirectories.
	 */
	public ThroughDirectories(){}
	
	// ----- Methods -----
	/**
	 * Returns the list of Pathway instances from the kgml parsing.
	 * @return the list of Pathway instances from the kgml parsing
	 */
	public List<Pathway> getParsedPathways(){
		return this.parsedPathways;
	}
	
	/**
	 * Returns the list of Bacteria possessing a specified metabolic pathway.
	 * @param path ID of the pathway of interest
	 * @return the list of Bacteria possessing a specified metabolic pathway
	 */
	public List<String> getBacteria(String path){
		List<String> listBacteria = new ArrayList<>();
		for(Pathway p : parsedPathways){
			if(p.getNumber().equals(path)){
				// for each pathway, extract the 3-letters code and the bacteria name
				String species = p.getBacteria().getCode() + " - " + p.getBacteria().getName();
				if(!listBacteria.contains(species)) 
					// if the bacteria is not in the list, add it
					listBacteria.add(species);
			}
		}
		// oder in alphabetical order
		Collections.sort(listBacteria);
		// return list of bacterias having the metabolic pathway
		return listBacteria;
	}
	
	/**
	 * Returns the list of metabolic pathways present in the directory including the kgml files.
	 * @return the list of pathways
	 */
	public List<String> getPathways(){
		List<String> pList = new ArrayList<>();
		for(Pathway p : parsedPathways){
			// read all the parsed files and list all the pathways
			if(!pList.contains(p.getNumber() + " - " + p.getTitle())){
				pList.add(p.getNumber() + " - " + p.getTitle());
			}	
		}
		Collections.sort(pList); // order
		return pList; // list of the metabolic pathways
	}
	
	/**
	 * Returns the ID and name of each compound.
	 * @return hash table with ID (key) and name (value) of each compound.
	 */
	public Hashtable<String, String> getParsedCompounds(){
		return this.compounds;
	}
	
	/**
	 * Returns the ID and name of each reaction.
	 * @return hash table with ID (key) and name (value) of each reaction
	 */
	public Hashtable<String, String> getParsedReactions(){
		return this.reactions;
	}
	
	/**
	 * Recursively browses through the sub-folders and files of the directory chosen by the user, and parses the files.
	 * @param files the directory including the kgml files and the text files containing information about reactions and compounds.
	 */
	public void listPathwaysAndBact(File[] files){
		for (File f : files){
			// directory: function is called recursively
			if (f.isDirectory()){
				listPathwaysAndBact(f.listFiles());
			}
			// kgml file
			else if(f.isFile() && f.getName().contains(".kgml")){
				// parsing
				parser.kgmlParser(f, parsedPathways);
				
				// get complete name of the bacteria
				String[] bact = f.getParentFile().getName().split("_");
				String code = bact[0];					// 3-letters code
				String name = bact[1] + " " + bact[2];	// complete name
				Bacteria bacteria = new Bacteria(name, code);	// create new instance of the Bacteria class
				parsedPathways.get(parsedPathways.size()-1).setBacteria(bacteria); // update bacteria in the Pathway resulting from parsing
			}
			// reaction file
			else if(f.isFile() && (f.getName().startsWith("R") && f.getName().endsWith(".txt"))){
				String h = reactionParser.reactionParser(f);
				String reactionId = f.getName().split("\\.txt")[0];
				// add the hash table to the one which will contain all the parsed reactions
				if (h != null)
					reactions.put(reactionId, h);				
			}
			// compound file
			else if(f.isFile() && (f.getName().startsWith("C") && f.getName().endsWith(".txt"))){
				String compoundName = compoundParser.compoudParser(f);	// extract compound name
				String compoundId = f.getName().split("\\.txt")[0];
				compounds.put(compoundId, compoundName);				
			}
		}
	}
}