package parsers;

import xml.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handles the kgml files.
 * @author Severine Liegeois
 *
 */
public class MyXMLHandler extends DefaultHandler {
	/**
	 * Contains the information of the kgml file.
	 */
	private Pathway pathway;
	/**
	 * Contains the information about an "entry" element of the kgml file.
	 */
	private Entry entry;
	/**
	 * Contains the information about a "reaction" element of the kgml file.
	 */
	private Reaction reaction;
	/**
	 * Contains the information about a "graphics" element of the kgml file.
	 */
	private KgmlGraphics graphics;
	/**
	 * Contains the information about a "substrate" element of the kgml file.
	 */
	private Substrate substrate;
	/**
	 * Contains the information about a "product" element of the kgml file.
	 */
	private Product product;
   
	/**
	 * Stores the name of an encountered node of the kgml file.
	 */
   private String node = null;
   
   /**
    * Redefines how to handle and catch the event.
    * Creates an object for each sort of element, for an easier manipulation when creating the graph.
    */
   public void startElement(String namespaceURI, String lname,
         String qname, Attributes attrs) throws SAXException {
      
	  // store node's name to handle the place where the node's value will be affected
      this.node = qname;
         
      // when we meet an element, create the associated object
      if(qname.equals("pathway")){
    	  pathway = new Pathway();
    	  pathway.setNumber(attrs.getValue("number"));
    	  pathway.setTitle(attrs.getValue("title"));
    	  pathway.setOrg(attrs.getValue("org"));
      }
      else if(qname.equals("entry")){
    	  entry = new Entry();
    	  entry.setName(attrs.getValue("name"));
    	  entry.setType(attrs.getValue("type"));
    	 // entry.setReaction(attrs.getValue("reaction"));
      }
      else if(qname.equals("graphics")){
    	  graphics = new KgmlGraphics();
    	  graphics.setName(attrs.getValue("name"));
    	  graphics.setX(attrs.getValue("x"));
    	  graphics.setY(attrs.getValue("y"));
      }
      else if(qname.equals("reaction")){
    	  reaction = new Reaction();
    	  reaction.setName(attrs.getValue(1).split(":")[1]);
    	  reaction.setType(attrs.getValue(2));
    	  reaction.setReaction(attrs.getValue(3));
      }
      else if(qname.equals("substrate")){
    	  substrate = new Substrate();
    	  substrate.setName(attrs.getValue("name").split(":")[1]);
      }
      else if(qname.equals("product")){
    	  product = new Product();
    	  product.setName(attrs.getValue("name").split(":")[1]);
      }
   }   
   
   /**
    * Detects when the end of an element has been reached and adds it to its proper place in the model.
    */
   public void endElement(String uri, String localName, String qName)
         throws SAXException{
	   // when the end of an element is detected, add it to its parent object
	   // add internals elements
	   if(qName.equals("entry")){
		   pathway.addEntry(entry);
		   entry.setGraphics(graphics);
	   }
	   else if(qName.equals("reaction")){
		   pathway.addReaction(reaction);
		   reaction.addSubstrate(substrate);
		   reaction.addProduct(product);
	   }
   }
   
   /**
    * Returns the Pathway instance.
    * @return instance of the Pathway class
    */
   public Pathway getPathway(){
	   return pathway;
   }
}