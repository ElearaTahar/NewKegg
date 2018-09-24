package xml;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the information about a metabolic pathway belonging to an organism.
 * The pathway is defined by its ID, name and organism.
 * It possesses different entries and reactions.
 * One pathway can belong to different orgnaisms.
 * @author Severine Liegeois
 *
 */
public class Pathway {
	/**
	 * Number identifying a metabolic pathway.
	 */
	private String number = null;
	/**
	 * Title of the metabolic pathway.
	 */
	private String title = null;
	/**
	 * The organism the pathway belongs to.
	 */
	private String org = null;
	/**
	 * List of kgml entries associated with the pathway.
	 */
	private List<Entry> listEntry = new ArrayList<>();
	/**
	 * List of reactions associated with the pathwawy;
	 */
	private List<Reaction> listReaction = new ArrayList<>();
	/**
	 * Bacteria species associated with the pathway.
	 */
	private Bacteria bacteria;
	
	/**
	 * Adds an Entry to the list associated with the pathway.
	 * @param e Entry instance
	 */
	public void addEntry(Entry e){
		this.listEntry.add(e);
	}
	
	/**
	 * Adds a Reaction to the list associated with the pathway.
	 * @param r reaction instance
	 */
	public void addReaction(Reaction r){
		this.listReaction.add(r);
	}
	
	/**
	 * Sets the Bacteria associated with the pathway.
	 * @param b Bacteria instance
	 */
	public void setBacteria(Bacteria b){
		this.bacteria = b;
	}
	
	/**
	 * Sets the number identifying the pathway.
	 * @param n number identifying the pathway
	 */
	public void setNumber(String n) {
		this.number = n;
	}
	
	/**
	 * Sets the title of the metabolic pathway.
	 * @param t title of the pathway
	 */
	public void setTitle(String t) {
		this.title = t;
	}
	
	/**
	 * Sets the 3 letters code identifying the organism the pathway belongs to.
	 * @param s 3 letters code of the organism
	 */
	public void setOrg(String s){
		this.org = s;
	}
	
	/**
	 * Returns all the Reaction instances of the Pathway.
	 * @return the list of the Pathway's Reaction instances
	 */
	public List<Reaction> getReactions(){
		return this.listReaction;
	}
	
	/**
	 * Returns all the Entry instances of the Pathway.
	 * @return the list of the Pathway's Entry instances
	 */
	public List<Entry> getEntry(){
		return this.listEntry;
	}
	
	/**
	 * Returns the Bacteria associated with the pathway.
	 * @return the Bacteria associated with the pathway
	 */
	public Bacteria getBacteria(){
		return this.bacteria;
	}
	
	/**
	 * Returns the title of the pathway.
	 * @return the title of the pathway
	 */
	public String getTitle(){
		return this.title;
	}
	
	/**
	 * Returns the organism the pathway belongs to.
	 * @return the organism's 3 letters identifier
	 */
	public String getOrg(){
		return this.org;
	}
	
	/**
	 * Returns the number identifying the pathway.
	 * @return the number identifying the pathway
	 */
	public String getNumber(){
		return this.number;
	}
}

