package xml;

/**
 * Creates a Bacteria object with its name and ID (3 letters code).
 * @author Severine Liegeois
 *
 */
public class Bacteria {
	/**
	 * Scientific name of the bacteria.
	 */
	private String name;
	/**
	 * Short name (3 letters) of the bacteria.
	 */
	private String code;
	
	/**
	 * Creates a new Bacteria.
	 * @param name scientific name of the bacteria
	 * @param code short name identifying the bacteria
	 */
	public Bacteria(String name, String code){
		this.name = name;
		this.code = code;
	}
	
	/**
	 * Returns the scientific name of the bacteria. 
	 * @return scientific name of the bacteria
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Returns the short name identifying the bacteria.
	 * @return short name identifying the bacteria
	 */
	public String getCode(){
		return this.code;
	}
}
