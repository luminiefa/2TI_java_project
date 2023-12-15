
import control.MainController;
import model.Server;

/**
 * Main class
 * 
 * Entry point of the program
 */
public class Main {

	/**
	 * Instantiate a new MainController and starts it
	 * 
	 * @param args program arguments
	 */
	public static Server selectedServer;
	
	public static void main(String[] args) {
		new MainController().start();
	}

}
