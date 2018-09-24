package xml;

/**
 * Defines the coordinates of a kgml entry defined by its name.
 * @author Severine Liegeois
 *
 */
public class KgmlGraphics {
	/**
	 * Name of the kgml element.
	 */
	private String name = null;
	/**
	 * Coordinate on the x-axis.
	 */
	private String x = null;
	/**
	 * Coordinate on the y-axis.
	 */
	private String y = null;
	
	/**
	 * Sets the name of the element to which the coordinates belong
	 * @param n name of the element
	 */
	public void setName(String n){
		this.name = n;
	}
	
	/**
	 * Sets the x coordinate.
	 * @param x
	 */
	public void setX(String x){
		this.x = x;
	}
	
	/**
	 * Sets the y coordinate.
	 * @param y
	 */
	public void setY(String y){
		this.y = y;
	}
	
	/**
	 * Returns the name of the element.
	 * @return the name of the element
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Returns the x coordinate as an integer.
	 * @return the x coordinate
	 */
	@SuppressWarnings("null")
	public int getX(){
		if(this.x.equals(null)){
			return (Integer) null;
		}
		return Integer.parseInt(this.x);
	}
	
	/**
	 * Returns the y coordinate as an integer.
	 * @return the y coordinate
	 */
	@SuppressWarnings("null")
	public int getY(){
		if(this.y.equals(null)){
			return (Integer) null;
		}
		return Integer.parseInt(this.y);
	}
}

