package xml;

/**
 * Creates an Entry object with its name, type and graphic characteristics.
 * @author Severine Liegeois
 *
 */
public class Entry {
	/**
	 * Name of the kgml entry.
	 */
	protected String name = null;
	/**
	 * Type of the kgml entry.
	 */
	protected String type = null;
	/**
	 * Contains the graphic characteristics of the entry.
	 */
	private KgmlGraphics graphics;
	
	/**
	 * Sets the graphic characteristics of the entry.
	 * @param g KgmlGraphics instance
	 */
	public void setGraphics(KgmlGraphics g){
		this.graphics = g;
	}
	
	/**
	 * Sets the name of the entry.
	 * @param n name of the entry
	 */
	public void setName(String n){
		this.name = n;
	}
	
	/**
	 * Sets the type of the entry.
	 * @param t type of the entry
	 */
	public void setType(String t){
		this.type = t;
	}
	
	/**
	 * Returns the graphic characteristics associated to the entry. 
	 * @return the graphic characteristics of the entry
	 */
	public KgmlGraphics getGraphics(){
		return this.graphics;
	}
	
	/**
	 * Returns the name of the entry.
	 * @return the name of the entry
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Returns the type of the entry.
	 * @return the type of the entry
	 */
	public String getType(){
		return this.type;
	}
}

