package xml;

import java.util.ArrayList;
import java.util.List;

/**
 * Reaction belonging to a Pathway.
 * Defined by:
 * <ul>
 * <li> list of substrates involved
 * <li> list of products involved
 * <li> identifier of the reaction
 * </ul>
 * Specialises the Entry class.
 * @author Severine Liegeois
 *
 */
public class Reaction extends Entry{
	/**
	 * List of the substrates involved in the reaction.
	 */
	private List<Substrate> listSubstrate = new ArrayList<Substrate>();
	/**
	 * List of the products involved in the reaction.
	 */
	private List<Product> listProduct = new ArrayList<Product>();
	/**
	 * Identifier of the reaction.
	 */
	private String reaction = null;

	/**
	 * Adds a substrate to the list of substrates involved in the reaction.
	 * @param s Substrate instance
	 */
	public void addSubstrate(Substrate s){
		this.listSubstrate.add(s);
	}
	
	/**
	 * Adds a product to the list of products involved in the reaction.
	 * @param p Product instance
	 */
	public void addProduct(Product p){
		this.listProduct.add(p);
	}
	
	/**
	 * Sets the reaction identifier.
	 * @param s identifier
	 */
	public void setReaction(String s){
		this.reaction = s;
	}
	
	/**
	 * Returns all the substrates involved in the reaction.
	 * @return the list of Substrate instances
	 */
	public List<Substrate> getSubstrates(){
		return this.listSubstrate;
	}
	
	/**
	 * Returns all the products involved in the reaction.
	 * @return the list of Product instances
	 */
	public List<Product> getProducts(){
		return this.listProduct;
	}
}

