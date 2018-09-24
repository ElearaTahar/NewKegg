package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.*;

import xml.*;

/**
 * Uses the information extracted while parsing to construct a graph of a metabolic pathway, using the 
 * GraphStream library.
 * Each reaction is depicted by a rectangular node between nodes representing the substrates and products of 
 * the reaction.
 * @author Severine Liegeois
 *
 */
public class KgmlToGraph {
	/**
	 * Graph of a metabolic pathway;
	 */
	private Graph graph = new MultiGraph("Graph");
	/**
	 * Hash table with the ID (key) of a reaction and the list of bacteria possessing this reaction (value).
	 */
	private Hashtable<String, List<String>> countReactions;
	/**
	 * CSS reference for the different nodes styles.
	 */
	private String styleSheet =
	        "node.reaction {" +
	        "     shape: rounded-box; " +
	        "     padding: 2px; " +
	        "     fill-mode: dyn-plain;" +	
	        "     fill-color: white, blue; " + 
	        "     stroke-mode: plain; " +
	        "     size-mode: fit; " +
	        "     text-size: 10;}" + 
	        "node.compound {text-alignment: under;" +
	        "     text-size:10;" + 
	        "     size-mode:fit;" + 
	        "     size: 5px;}" +
	        "node.title {shape: box; " +
	        "     stroke-mode: dashes;" +
	        "     stroke-color: red;" +
	        "     stroke-width: 2px; " +
	        "     padding: 6px;" +
	        "     fill-mode: plain;" +
	        "     fill-color: white;" + 
	        "     size-mode: fit;}";
	/**
	 * Legend of the graph (when nodes are colored according to species).
	 */
	private JPanel legend = new JPanel();
	/**
	 * JPanel containing the graph.
	 */
	private JPanel panG = new JPanel();
	/**
	 * If checked, labels the reaction nodes with the enzyme name.
	 * If not, labels the reaction nodes with the reaction ID.
	 */
	private JCheckBox enzCheck = new JCheckBox("Enzyme name");
	
	// --- Constructor ---
	/**
	 * Creates a graph displaying a metabolic pathway.
	 * @param pathNb the ID of the pathway
	 * @param paths list of Pathways coming from the kgml files parsing
	 * @param compounds hash table with the ID and name of each compound
	 * @param reactions hash table with the ID and name of each reaction
	 * @param container JPanel which will contain the graph and its legend
	 */
	public KgmlToGraph(String pathNb, List<Pathway> paths, Hashtable<String, String> compounds,
			Hashtable<String, String> reactions, JPanel container){
		
		graph.setAttribute("ui.stylesheet", styleSheet);
		this.init(pathNb, paths, compounds, reactions, container);
	}
	
	// --- Methods --- 
	/**
	 * Creates a view of the graph and embeds it in a JPanel.
	 * Adds the check box under the graph.
	 * @param pathNb the ID of the pathway
	 * @param paths list of Pathways coming from the kgml files parsing
	 * @param compounds hash table with the ID and name of each compound
	 * @param reactions hash table with the ID and name of each reaction
	 * @param container JPanel which will contain the graph and its legend
	 */
	public void init(String pathNb, List<Pathway> paths, Hashtable<String, String> compounds,
			Hashtable<String, String> reactions, JPanel container){
		
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		View view = viewer.addDefaultView(false);
		viewer.disableAutoLayout();
		
		enzCheck.addActionListener(new CheckListener(reactions));
		legend.add(enzCheck);
		
		panG.removeAll();
		panG.setLayout(new BorderLayout());
		// plot the graph
		this.kgmlToGraph(pathNb, paths, compounds, reactions);
		// add graph in its JPanel
		panG.add((Component) view, BorderLayout.CENTER);
		container.add(panG, BorderLayout.CENTER);
		container.add(legend, BorderLayout.SOUTH);
	}
	
	// ----------- Listener of the JCheckBox ------------------
	/**
	 * Listener of the check box.
	 * @author Séverine Liegeois
	 *
	 */
	class CheckListener implements ActionListener{
		/**
		 * Hash table with the ID and name of each reaction.
		 */
		private Hashtable<String, String> reactions;
		
		/**
		 * Creates a new CheckListener.
		 * @param reactions hash table with the ID and name of each reaction
		 */
		public CheckListener(Hashtable<String, String> reactions){
			this.reactions = reactions;
		}
		
		/**
		 * If the box is checked, the reaction nodes are labeled with the complete enzyme name,
		 * if not they are labeled with the reaction ID.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(enzCheck.isSelected()){
				for(Node n : graph){
					if(n.getAttribute("ui.class").equals("reaction")){
						String enzName = this.reactions.get(n.getAttribute("ui.label"));
						n.setAttribute("ui.label", n.getAttribute("ui.label")+ " : " +enzName);
					}
				}
			} else {
				for(Node n : graph){
					if(n.getAttribute("ui.class").equals("reaction")){
						n.setAttribute("ui.label", ((String) n.getAttribute("ui.label")).split(" : ")[0]);
					}
				}
			}
		}
	}
	//-------------------------------------------------------------
	
	/**
	 * Places and labels the nodes on the graph, and connects them with edges.
	 * A reaction node is connected to its substrates and products nodes.
	 * The edges are oriented.
	 * @param pathNb the ID of the pathway
	 * @param paths list of Pathways coming from the kgml files parsing
	 * @param compounds hash table with the ID and name of each compound
	 * @param reactions hash table with the ID and name of each reaction
	 */
	public void kgmlToGraph(String pathNb, List<Pathway> paths, Hashtable<String, String> compounds,
			Hashtable<String, String> reactions){
		
		graph.setStrict(false);
		graph.setAutoCreate(true);
		
		countReactions = new Hashtable<>();			// list of species having a reaction
		List<String> reaNames = new ArrayList<>();	// list of the identifiers of the reactions
		List<Reaction> rea = new ArrayList<>();		// list of the Reaction class instances present in a given pathway
		List<KgmlGraphics> gr = new ArrayList<>();	// list of KgmlGraphics class instances (coordinates)
		
		String pathName = null;	// variable which will contain the name of the metabolic pathway
		
		for (Pathway path : paths){								// For each Pathway instance
			if (path.getNumber().equals(pathNb)){				// if the number identifying the pathway corresponds to the one given as parameter
				pathName = path.getTitle();						// the name of the pathway is extracted.
				for(Reaction r: path.getReactions()){			// For each reaction of the Pathway
					String rName = r.getName().split(" ")[0];	// we extract its identifier
					if(!reaNames.contains(rName)){				// if it is not in the list
						reaNames.add(rName);					// we add it
						rea.add(r);								// and we add the Reaction in the 'rea' list.
					}
					if(!countReactions.containsKey(rName)){		// If the id of the reaction is not in the hash
						List<String> liste = new ArrayList<>();	// create a list
						liste.add(path.getOrg());				// and add the organism to this list
						countReactions.put(rName, liste);		// and associate the list and the id.
					}
					else if(countReactions.containsKey(rName)){	// If the id is already in the hash
						countReactions.get(rName).add(path.getOrg());	// add the organism in the associated list.
						}
				}
				for(Entry ent: path.getEntry()){	// For each Entry of the Pathway
					gr.add(ent.getGraphics());		// add its KgmlGraphics to the list.
				}
			}
		}
		
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		
		List<String> listNodes = new ArrayList<>();	// list of the graph nodes
		List<String> listEdges = new ArrayList<>();	// list of the graph edges
		
		int xt = 0, yt = 0;
		
		for(Reaction r : rea){							// For each reaction in the list
			String rName = r.getName().split(" ")[0];	// extract its identifier
			Node react = graph.addNode(rName);			// add the corresponding node
			react.addAttribute("ui.class", "reaction");	// give the node a type (for the style).
			
			int xs = 0, ys = 0, xp = 0, yp = 0;
			
			for(Substrate sub : r.getSubstrates()){		// For each substrate of the reaction
				xs = -1; ys = -1;
				Node sn;
				String s = sub.getName();				
				for(KgmlGraphics g: gr){
					if (g.getName().equals(s)){
						xs = g.getX();
						ys = g.getY();
						if(xs > xt) xt = xs;
						if(ys > yt) yt = ys;
					}
				}
				if (!listNodes.contains(s)){			// if there is no corresponding node
					s = compounds.get(s);				// get the compound name
					sn = graph.addNode(s);				// create a node
					sn.addAttribute("ui.label", s);		// label is the name of the compound
					sn.addAttribute("ui.class", "compound");
					if(xs != -1 && ys != -1)
						sn.setAttribute("xy", xs, ys);
					listNodes.add(s);					// add the node to the nodes list.
				}else{									// If it already exists
					sn = graph.getNode(s);				// get its reference.
				}
				String edgeName = s + r;				// ID of the edge between substrate and reaction.
				if (!listEdges.contains(edgeName)){		// If the edge does not exist, create it.
					if (r.getType().equals("reversible")){	// Reversible reaction, arrow at both ends of the edge.
						edgeName = edgeName + "rev";
						graph.addEdge(edgeName, react, sn, true);
					}else{									// Non reversible reaction
						graph.addEdge(edgeName, sn, react);
					}
					listEdges.add(edgeName);			// Add edge to edges list.
				}
			}
			
			for(Product pr : r.getProducts()){			// idem for products of the reaction
				xp = -1; yp = -1;
				Node pn;
				String p = pr.getName();
				for(KgmlGraphics g: gr){
					if (g.getName().equals(p)){
						xp = g.getX();
						yp = g.getY();
						if(xp > xt) xt = xp;
						if(yp > yt) yt = yp;
					}
				}
				if (!listNodes.contains(p)){
					p = compounds.get(p);
					pn = graph.addNode(p);
					pn.addAttribute("ui.label", p);
					pn.addAttribute("ui.class", "compound");
					if(xp != -1 && yp != -1)
						pn.setAttribute("xy", xp, yp);
					listNodes.add(p);
				}else{
					pn = graph.getNode(p);
				}
				String edgeName = p + r;
				if (!listEdges.contains(edgeName)){
					graph.addEdge(edgeName, react, pn, true);
					listEdges.add(edgeName);
				}
			}
			
			react.addAttribute("ui.label",  rName);	// add label to the node corresponding to the reaction
			react.addAttribute("xy", (xs+xp)/2, (ys+yp)/2);
		}
		// add name of the pathway, as a node
		Node titre = graph.addNode(pathName);
		titre.addAttribute("ui.label", pathName);
		titre.addAttribute("ui.class", "title");
		titre.setAttribute("xy", xt-30, yt+50);
	}
	
	/**
	 * Colors the nodes if only one or two species are selected: one color for each species, and if two are selected,
	 * a third color which shows the reaction present in both species.
	 * Adds a legend under the graph.
	 * @param species list of species selected in the JList
	 * @param legend JPanel containing the legend
	 */
	public void colorReactions(List<String> species){
		legend.removeAll();
		// Each rectangle of the legend is a colored JPanel, with the bacteria's name as a JLabel 
		JLabel l1, l2, l3;
		Dimension ld = new Dimension(35,25);
		Dimension ld2 = new Dimension(60,25);
		JPanel legend1 = new JPanel();
		legend1.setPreferredSize(ld);
		JPanel legend2 = new JPanel();
		legend2.setPreferredSize(ld);
		JPanel legend3 = new JPanel();
		legend3.setPreferredSize(ld2);
		
		for(Node n: graph){									// For each node of the graph
			String reaction = n.getAttribute("ui.label");	// get its label
			if(countReactions.containsKey(reaction)){		// if it is a reaction
				n.removeAttribute("ui.color");				// background is white.
				if(species.size() == 1){					// A single species selected
					// if the selected species has this reaction, color the node in cyan
					if(countReactions.get(reaction).contains(species.get(0))) n.setAttribute("ui.color", Color.CYAN);
					legend1.setBackground(Color.CYAN);		// color legend
					l1 = new JLabel(species.get(0));
					legend1.add(l1);
					legend.add(enzCheck);
					legend.add(legend1);
				}else{	// 2 selected species
					if(countReactions.get(reaction).contains(species.get(0)) &&
						!countReactions.get(reaction).contains(species.get(1))){
						n.setAttribute("ui.color", Color.CYAN);
					} else if(countReactions.get(reaction).contains(species.get(1)) &&
						!countReactions.get(reaction).contains(species.get(0))){
						n.setAttribute("ui.color", Color.YELLOW);
					} else if(countReactions.get(reaction).contains(species.get(0)) &&
							countReactions.get(reaction).contains(species.get(1))){
						n.setAttribute("ui.color", Color.GREEN);
					}
					legend1.setBackground(Color.CYAN);
					l1 = new JLabel(species.get(0));
					legend1.add(l1);
					legend2.setBackground(Color.YELLOW);
					l2 = new JLabel(species.get(1));
					legend2.add(l2);
					legend3.setBackground(Color.GREEN);
					l3 = new JLabel(species.get(0) + " + " + species.get(1));
					legend3.add(l3);
					legend.add(enzCheck);
					legend.add(legend1);
					legend.add(legend2);
					legend.add(legend3);
					legend.revalidate();
					legend.repaint();
				}
			}
		}
	}
	
	/**
	 * Colors the nodes if more than two species are selected. The nodes are colored with a gradient, from white (none of 
	 * the selected species possess this reaction) to blue (many or all of the selected species possess it).
	 * Adds a legend under the graph.
	 * @param species list of species selected in the JList
	 * @param legend JPanel containing the legend
	 */
	public void colorReactionsLot(List<String> species){
		legend.removeAll();
		for(Node n: graph){
			String reaction = n.getAttribute("ui.label");
			if(countReactions.containsKey(reaction)){
				int nb = 0;
				for(String sp: countReactions.get(reaction)){
					if(species.contains(sp)){
						nb++;		// increases each time a selected species has this reaction
					}
				}
				n.removeAttribute("ui.color");
				// Color with a gradient
				float col = (float)nb/(float)species.size();
				n.setAttribute("ui.color", col);
			}
		}
		// add legend as a colorbar with gradient
		ColorBar cb = new ColorBar();
		cb.setPreferredSize(new Dimension(130, 25));
		JLabel minLab = new JLabel("   -");
		JLabel maxLab = new JLabel("+");
		legend.add(enzCheck);
		legend.add(minLab);
		legend.add(cb);
		legend.add(maxLab);
		legend.revalidate();
		legend.repaint();
	}
}