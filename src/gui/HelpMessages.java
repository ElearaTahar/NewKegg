package gui;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Creates the text messages that will be displayed when the user clicks on an item in the "Help" menu.
 * @author Severine Liegeois
 *
 */

public class HelpMessages {
	/**
	 * Creates a new HelpMessages instance.
	 */
	public HelpMessages(){}
	
	/**
	 * Sets a specified document style to a JTextPane which will be displayed when the user clicks
	 * on the "Welcome" item in the "Help" menu.
	 * @param welcomePane JTextPane that will be displayed
	 */
	public void initWelcomeMessage(JTextPane welcomePane){
		welcomePane.setEditable(false);
		welcomePane.setPreferredSize(new Dimension(400, 200));
		// text style
		SimpleAttributeSet normalStyle = new SimpleAttributeSet();
		StyleConstants.setFontFamily(normalStyle, "Calibri");
		StyleConstants.setFontSize(normalStyle, 14);
		
		// paragraph title style
		SimpleAttributeSet titleStyle = new SimpleAttributeSet();
		titleStyle.addAttributes(normalStyle);
		StyleConstants.setFontSize(titleStyle, 18);
		StyleConstants.setBold(titleStyle, true);
		StyleConstants.setUnderline(titleStyle, true);
		StyleConstants.setForeground(titleStyle, Color.BLUE);
		
		// text justification
		SimpleAttributeSet justif = new SimpleAttributeSet();
		StyleConstants.setAlignment(justif, StyleConstants.ALIGN_JUSTIFIED);
		
		try{
			// get document style
			StyledDocument docWelcome = welcomePane.getStyledDocument();
			// insert character string in the document
			docWelcome.insertString(docWelcome.getLength(), "Thanks for using NewKegg !\n\n", titleStyle);
			docWelcome.insertString(docWelcome.getLength(), "This application allows you to visualize metabolic pathways by using data from the KEGG PATHWAY database.\n\n"
			+ "NewKegg displays a \"generic\" map for each pathway. You can then choose to display the map for one or several specific species.", normalStyle);
			int endText = docWelcome.getLength();
			// justification
			docWelcome.setParagraphAttributes(0, endText, justif, false);
		} catch (BadLocationException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets a specified document style to a JTextPane which will be displayed when the user clicks
	 * on the "Usage" item in the "Help" menu.
	 * @param usagePane JTextPane that will be displayed 
	 */
	public void initUsageMessage(JTextPane usagePane){
		usagePane.setEditable(false);
		usagePane.setPreferredSize(new Dimension(480, 380));
		// text style
		SimpleAttributeSet normalStyle = new SimpleAttributeSet();
		StyleConstants.setFontFamily(normalStyle, "Calibri");
		StyleConstants.setFontSize(normalStyle, 14);
		
		// paragraph title style
		SimpleAttributeSet titleStyle = new SimpleAttributeSet();
		titleStyle.addAttributes(normalStyle);
		StyleConstants.setFontSize(titleStyle, 18);
		StyleConstants.setBold(titleStyle, true);
		StyleConstants.setUnderline(titleStyle, true);
		StyleConstants.setForeground(titleStyle, Color.BLUE);
		
		// text justification
		SimpleAttributeSet justif = new SimpleAttributeSet();
		StyleConstants.setAlignment(justif, StyleConstants.ALIGN_JUSTIFIED);
		
		try{
			// get document's style
			StyledDocument doc = usagePane.getStyledDocument();
			// insert character string in the document
			doc.insertString(doc.getLength(), "How to get a generic map of a pathway ?\n", titleStyle);
			doc.insertString(doc.getLength(), "Click on the \"Browse\" button to browse your directories and select the one containing the kgml files from the KEGG PATHWAY database."
					+ "These files are used to generate maps displaying, for a metabolic pathway, all the reactions present in all the species."
					+ "Once the parsing of the kgml files done, the list of metabolic pathways is displayed in the \"Pathways\" area. Click on one pathway to display its map.\n\n", normalStyle);
			doc.insertString(doc.getLength(), "How to get a specific map of a pathway ?\n", titleStyle);
			doc.insertString(doc.getLength(), "When you select a pathway in the \"Pathways\" list, the list of species which possess this pathway is displayed in the \"Species\" area."
					+ "If you click on one species, the reactions that occur in this species will be colored on the map.\n"
					+ "If you select two species, each species will have a specific color, and a third color will indicate the reactions present in both species.\n"
					+ "You can select more than two species. In this case, reactions will be colored according to a gradient: the more species possess this reaction, the more the reaction is blue.\n\n", normalStyle);
			doc.insertString(doc.getLength(), "How to display the enzymes' names ?\n", titleStyle);
			doc.insertString(doc.getLength(), "The maps displayed label reactions with IDs (e.g. R00001). If you want to see the names of the enzymes performing the reactions,"
					+ "check the \"Enzyme name\" box under the map.\n\n", normalStyle);
			doc.insertString(doc.getLength(), "How to zoom and navigate on the graph ?\n", titleStyle);
			doc.insertString(doc.getLength(), "Left-click on the graph. To zoom in, press the Page Up key; to zoom out, press the Page Down key.\n"
					+ "You can navigate on the graph by using the four arrow keys (left, up, down and right).\n\n", normalStyle);
			doc.insertString(doc.getLength(), "How to export a map as an image ?\n", titleStyle);
			doc.insertString(doc.getLength(), "In the \"Map\" menu, which is accessible as soon as a map is displayed, click on the \"Save As PNG\" item "
					+ "to save the map and its legend as an image.", normalStyle);
			int endText = doc.getLength();
			// justification
			doc.setParagraphAttributes(0, endText, justif, false);
		} catch (BadLocationException e){
			e.printStackTrace();
		}
	}
	
}



