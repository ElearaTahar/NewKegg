package gui;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

/**
 * Implements the abstract SwingWorker<T,V> class to perform lengthy GUI-interaction tasks
 * in a background thread: the parsing of all the kgml and text files.
 * Avoids the GUI to freeze during the parsing.
 * This SwingWorker's doInBackground and get methods return a String message: " " if the time-consuming 
 * task proceeded without any problem, "Error" if an error occurred during the parsing.
 * @author Severine Liegeois
 * 
 */
public class DirWorker extends SwingWorker<String, Void>{
	/**
	 * The graphic interface.
	 */
	private Fenetre fenetre;
	
	
	// -- Constructor --
	/**
	 * Creates a new SwingWorker by specifying the graphic interface containing the Browse button 
	 * (to chose the directory containing the kgml files) and the different JPanels
	 * which will display the pathways and species lists.
	 * @param f the graphic interface
	 */
	public DirWorker(Fenetre f){
		super();
		this.fenetre = f;
	}

	/**
	 * De-activates the Browse button while the different files are being browsed and parsed.
	 * MeanWhile shows a message: "Processing...".
	 */
	@Override
	public String doInBackground(){
		fenetre.getChooseButton().setEnabled(false);			// the button is disabled
		fenetre.getProcessStatus().setText("Processing...");	// informs the user that work is in progress
		fenetre.initPaths();
		return " ";
	}
	
	/**
	 * Re-activates the Browse button when the long task is done.
	 * Shows the message "Error" if an error occurred during the parsing.
	 */
	@Override
	public void done(){
		String msg = "Error";
		try{
			msg = get();
		} catch (ExecutionException e){
			e.printStackTrace();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		fenetre.getProcessStatus().setText(msg);
		fenetre.getChooseButton().setEnabled(true);		// the button is re-activated
	}
	
}
