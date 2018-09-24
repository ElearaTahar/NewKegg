package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import parsers.ThroughDirectories;
import xml.Pathway;

/**
 * <p>
 * Graphic interface of the application.
 * Displays several components:
 * <ul>
 * <li>a text area and a button allowing to browse through all the directories to select the one containing the KGML files</li>
 * <li>two panels to display the pathways and species lists</li>
 * <li>a panel occupying the most space to display the graph</li>
 * <li>a menu comprising a help page and the possibility to save the graph as a PNG file</li>
 * </ul>
 * </p>
 * @author Severine Liegeois
 *
 */
public class Fenetre extends JFrame implements ListSelectionListener{
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Menu bar.
	 */
	private JMenuBar menuBar = new JMenuBar();
	/**
	 * Menus.
	 */
	private JMenu map = new JMenu("Map"),
			help = new JMenu("Help");
	/**
	 * Menus items.
	 */
	private JMenuItem save = new JMenuItem("Save As PNG"),
			helpItem = new JMenuItem("Usage"),
			welcome = new JMenuItem("Welcome");
	/**
	 * Content pane of the JFrame.
	 */
	private JPanel container = new JPanel();
	/**
	 * Panels of the container.
	 */
	private JPanel panGraph, panLists, pPan, sPan, panDir;
	private JLabel dir, processStatus;
	/**
	 * Lists of pathways and species.
	 */
	private JList<String> pList, sList;
	/**
	 * Scroll bars for the pathways and species lists.
	 */
	private JScrollPane sScroll, pScroll;
	/**
	 * Allow to select a directory and save the graph as an image.
	 */
	private JFileChooser chooser, saver;
	/**
	 * Button allowing to select a directory.
	 */
	private JButton chooseButton;
	/**
	 * Displays the absolute path of the chosen directory.
	 */
	private JTextArea chosenDir;
	/**
	 * Lists of bacteria names and pathways.
	 */
	private List<String> bacteria, pathways;
	/**
	 * List of species selected by the user in the JList.
	 */
	private List<String> species;
	/**
	 * Parses the different files. 
	 */
	private ThroughDirectories d;
	/**
	 * Directory containing the kgml files, chosen by the user. 
	 */
	private File selectedDir;
	/**
	 * Creates the graph of a pathway.
	 */
	private KgmlToGraph k;
	
// ----- Constructor -----
	/**
	 * Creates a new graphic interface and displays its components.
	 */
	public Fenetre(){
		this.setTitle("NewKegg");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null); // middle of the screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		container.setLayout(new BorderLayout());
		
		// set the different components of the window
		this.initComponent();
		map.setEnabled(false);	// the map menu is disabled until a graph is displayed
		this.initMenu();		// initialize the menus bar
		
		this.setContentPane(container);
		this.setVisible(true);
	}
	
// ----- Methods -----
	/**
	 * Returns the JList of the metabolic pathways.
	 * @return the JList of the metabolic pathways
	 */
	public JList<String> getpList(){
		return this.pList;
	}
	/**
	 * Returns the JLabel displaying the status of the time-consuming task.
	 * @return the JLabel displaying the status of the files parsing.
	 */
	public JLabel getProcessStatus(){
		return this.processStatus;
	}
	/**
	 * Returns the button that permits to choose a directory.
	 * @return the button that permits to choose a directory
	 */
	public JButton getChooseButton(){
		return this.chooseButton;
	}
	
	/**
	 * Places the panels and the button for directory selection in the content pane.
	 */
	public void initComponent(){
		// --------------------------
		// Graph zone
		// --------------------------
		panGraph = new JPanel();
		panGraph.setBackground(Color.WHITE);
		panGraph.setLayout(new BorderLayout());
		panGraph.setPreferredSize(new Dimension(520, 550));
		panGraph.setMaximumSize(panGraph.getPreferredSize());
		panGraph.setMinimumSize(panGraph.getPreferredSize());
		panGraph.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		container.add(panGraph, BorderLayout.CENTER);
		
		// ---------------------------------------------
		// Pathway and species selection zone
		// ---------------------------------------------
		panLists = new JPanel();
		panLists.setPreferredSize(new Dimension(160, 550));
		panLists.setLayout(new FlowLayout());
		// create JPanels
		pPan = new JPanel();
		sPan = new JPanel();
		pPan.setBorder(BorderFactory.createTitledBorder("Pathways"));
		sPan.setBorder(BorderFactory.createTitledBorder("Species"));
		Dimension dimPan = new Dimension(150, 150);
		pPan.setPreferredSize(dimPan);
		sPan.setPreferredSize(dimPan);
		// add to the previous JPanel
		panLists.add(pPan);
		panLists.add(sPan);
		// add JLabel to indicate that work in progress is long
		processStatus = new JLabel(" ");
		processStatus.setHorizontalAlignment(SwingConstants.CENTER);
		panLists.add(processStatus);
		// add to ContentPane
		container.add(panLists, BorderLayout.WEST);
		
		// ---------------------------------
		// Folder selection bar
		// ---------------------------------
		panDir = new JPanel();
		panDir.setLayout(new FlowLayout());
		panDir.setPreferredSize(new Dimension(500, 40));
		dir = new JLabel("Select a directory");
		chooseButton = new JButton("Browse");
		chooseButton.addActionListener(new DirChooserListener());
		// text zone to print the chosen folder
		chosenDir = new JTextArea();
		chosenDir.setEditable(false);
		chosenDir.setPreferredSize(new Dimension(400, 25));
		chosenDir.setBackground(Color.WHITE);
		chosenDir.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// elements displayed in alphabetical order in the JPanel thanks to FLowLayout
		panDir.add(dir);
		panDir.add(chosenDir);
		panDir.add(chooseButton);
		// add to ContentPane
		container.add(panDir, BorderLayout.NORTH);
	}
	
	/**
	 * Places the menu bar in the content pane.
	 */
	public void initMenu(){
		// add an Actionlistener on the "Save" item of the Map menu
		save.addActionListener(new SaveMapListener());
		// add the accelerator CTRL + S on the Save sub-menu
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		// add Save item to Map menu
		map.add(save);
		
		// add an Actionlistener on the "Welcome" item in Help menu
		welcome.addActionListener(new ActionListener(){
			/**
			 * If the user clicks on the Welcome item in the Help menu, a frame pops up with a message explaining
			 * what the application does.
			 */
			@Override
			public void actionPerformed(ActionEvent e){
				// pop-up with message
				HelpMessages hm = new HelpMessages();
				JTextPane mess = new JTextPane();
				hm.initWelcomeMessage(mess);
				JOptionPane.showMessageDialog(null, mess, "Welcome", JOptionPane.PLAIN_MESSAGE, null);
			}
		});
		// add an Actionlistener on the "Usage" item in the Help menu
		helpItem.addActionListener(new ActionListener(){
			/**
			 * If the user clicks on the Welcome item in the Help menu, a frame pops up with a text explaining
			 * how to use the application.
			 */
			@Override
			public void actionPerformed(ActionEvent e){
				// idem
				HelpMessages hm = new HelpMessages();
				JTextPane mess = new JTextPane();
				hm.initUsageMessage(mess);
				JScrollPane scroll = new JScrollPane(mess);
				JOptionPane.showMessageDialog(null, scroll, "How to use NewKegg ?", JOptionPane.PLAIN_MESSAGE, null);
			}
		});
		// add items "Welcome" and "Usage" to Help menu
		help.add(welcome);
		help.add(helpItem);
		// add mnemonics
		map.setMnemonic('M');
		help.setMnemonic('H');
		// add menus in the menu bar
		menuBar.add(map);
		menuBar.add(help);
		// add menu bar to the window
		this.setJMenuBar(menuBar);
	}
	
	
// ----- Folder choice -----
	/**
	 * Selects a directory containing the kgml files.
	 */
	class DirChooserListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Choose a directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	// only directories can be chosen
			buttonActionPerformed(e);
		}
	}
	
	/**
	 * When the directory containing the kgml files is chosen by the user,
	 * its absolute path is displayed in the text area next to the button.
	 * The parsing of the files, which is a time-consuming task, is executed in another
	 * thread thanks to a SwingWorker.
	 * @param e ActionEvent, selection of a directory
	 */
	public void buttonActionPerformed(ActionEvent e){
		// when the directory is chosen
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			this.selectedDir = chooser.getSelectedFile();
			String dirName = selectedDir.getAbsolutePath();
			chosenDir.setText(dirName);		// print the absolute path of the chosen folder
			new DirWorker(this).execute();	// list of the metabolic pathways: in an other thread
		}
	}
// -----------------------------
	
	/**
	 * Saves the graph as a PNG file. The user the name and location of the saved image.
	 */
	class SaveMapListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// folder choice
			saver = new JFileChooser();
			saver.setCurrentDirectory(new java.io.File("."));
			saver.setDialogTitle("Save map");
			if(saver.showSaveDialog(save) == JFileChooser.APPROVE_OPTION){
				// convert the JPanel of the graph into an image
				BufferedImage image = new BufferedImage(panGraph.getWidth(), panGraph.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics g = image.getGraphics();
				panGraph.printAll(g);
				g.dispose();
				try{ // save image with name chosen by the user
					ImageIO.write(image, "png", new File(saver.getSelectedFile().getAbsolutePath()+".png"));
				} catch (IOException exp){
					exp.printStackTrace();
				}
			}
		}	
	}
	
// -----------------------------
	/**
	 * Gets the list of pathways and displays it in a scrollable JList in the "Pathways" panel.
	 * A single pathway can be selected at once.
	 * When selected, the pathway is drawn as a graph.
	 * A check box allows to label the reactions nodes with the enzymes names rather than their ID.
	 */
	public void initPaths(){		
		File[] files = this.selectedDir.listFiles();	// list of files and sub-directories
		this.d = new ThroughDirectories();				
		
		d.listPathwaysAndBact(files);			
		this.pathways = d.getPathways();				// list of metabolic pathways
		
		// build a new model for the JList
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (String p : this.pathways){
			model.addElement(p);
		}
		
		// create JList
		pPan.removeAll(); // in case a JList is already printed (for example, if the directory changes)
		pList = new JList<String>(model);
		pList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pList.setVisibleRowCount(6);
		pList.addListSelectionListener(this);
		pList.setSelectedIndex(-1);
		// create scrolling bar
		pScroll = new JScrollPane(pList);
		Dimension dp = pScroll.getPreferredSize();
		dp.width = 138;
		pScroll.setPreferredSize(dp);
		pList.setAutoscrolls(true);
		// add to corresponding JPanel
		pPan.add(pScroll);
		pPan.revalidate();
		pPan.repaint();
	}
	
	/**
	 * Reacts to a change in values selected in the Pathways and Species Jlist.
	 * If a pathway is selected, its ID is retrieved and used to call the initBact method, and the graph
	 * of the pathway is drawn.
	 * If one or several species are selected, the list of species names is retrieved and used to color the 
	 * reactions nodes thanks to the initColors method.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(pList)){				// source = pathways list
			if (!e.getValueIsAdjusting()){
				if (pList.getSelectedIndex() != -1){	// select a pathway
					String path = (String)this.pList.getSelectedValue().split(" - ")[0];
					initBact(path);						// list of the species having this pathway
					drawGraph(path);					// show corresponding graph
				}
			}
		}
		if (e.getSource().equals(sList)){				// source = list of species
			if (!e.getValueIsAdjusting()){
				species = sList.getSelectedValuesList();// liste of species selected in the Jlist
				initColors(species);					// color nodes
			}
		}
	}
	
	/**
	 * Gets the list of species possessing a specified pathway and displays it in a scrollable JList in the "Species" panel.
	 * Several species can be selected at once.
	 * @param path ID of a metabolic pathway
	 */
	public void initBact(String path){
		this.bacteria = this.d.getBacteria(path);
		
		DefaultListModel<String> model2 = new DefaultListModel<String>();
		for (String p : this.bacteria){
			model2.addElement(p);
		}
		
		// create JList
		sPan.removeAll();
		sList = new JList<String>(model2);
		//System.out.println(model2.getSize());
		sList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		sList.setVisibleRowCount(6);
		sList.addListSelectionListener(this);
		sList.setSelectedIndex(-1);
		// create scrolling bar
		sScroll = new JScrollPane(sList);
		Dimension ds = sScroll.getPreferredSize();
		ds.width = 138;
		sScroll.setPreferredSize(ds);
		sList.setAutoscrolls(true);
		// add to corresponding JPanel
		sPan.add(sScroll);
		sPan.revalidate();
		sPan.repaint();
	}
	
	/**
	 * Creates and draws the graph of a metabolic pathway selected in the JList.
	 * @param pathNb ID of the selected pathway
	 */
	public void drawGraph(String pathNb){
		map.setEnabled(true);	// Map menu is accessible
		panGraph.removeAll();	// empty the JPanel
		List<Pathway> paths = this.d.getParsedPathways();
		Hashtable<String, String> compounds = this.d.getParsedCompounds();
		Hashtable<String, String> reactions = this.d.getParsedReactions();
		k = new KgmlToGraph(pathNb, paths, compounds, reactions, panGraph);
		panGraph.repaint();
		container.add(panGraph, BorderLayout.CENTER);
	}
	
	/**
	 * Colors the nodes representing reactions according to species that possess them.
	 * When species are selected, the reactions present in these species are colored and a legend is displayed.
	 * If one or two species are selected, calls the colorReactions method from the KgmlToGraph class.
	 * If more than two species are selected, calls the colorReactionsLot from the same class.
	 * @param sp list of the species selected in the JList
	 */
	public void initColors(List<String> sp){
		List<String> spCodes = new ArrayList<>();		// list of the 3-letters species codes
		for(int i = 0; i < sp.size(); i++){
			spCodes.add(sp.get(i).split(" - ")[0]);
		}
		if(spCodes.size() == 1 || spCodes.size() == 2){	// 1 or 2 selected species
			k.colorReactions(spCodes);
		}else if (spCodes.size() > 2){ 
			k.colorReactionsLot(spCodes);				// 2+ selected species
		}
		panGraph.revalidate();
		panGraph.repaint();
	}
	
	
	
	
// --------------------------------------------------------------------
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Fenetre();
            }
        });
	}

	
}
