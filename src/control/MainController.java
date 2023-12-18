package control;

import control.menu.MainMenu;
import data.DataSource;
import model.Server;
import control.menu.MainMenu.MainMenuAction;
import ui.input.Input;
import ui.output.Output;

/**
 * Controller for the main menu
 */
public class MainController extends MenuController<MainMenuAction> {

	// MainController uses a MainMenu
	public MainController() {
		super(new MainMenu());
	}

	public void processAction() {

		// Execute some action
		// Menu principal
		switch (this.getMenuAction()) {
		case LIST:
			this.listServer();
			break;
		case INFO:
			this.infoServer();
			break;
		case ADD:
			this.addServer();
			break;
		case DELETE:
			this.deleteServer();
			break;
		case EXIT_PROGRAM:
			this.exitRequest();
			Output.message("Goodbye...");
			break;
		case EXAMPLE_OBJECT:
			this.startObjectController();
			break;
		}
	}

	// Action Lister tous les serveurs
	private void listServer() {
		Output.message("ID	STATE	Firewall	Services");
		for (Server server : DataSource.serverList) {
			Output.message(server.toString());
		}
	}
	
	
	// Action infos sur un serveur
	private void infoServer() {
	    String idString = Input.readString("Enter the server's ID"); // Lire l'id entré par l'utilisateur

	    try {
	        int id = Integer.parseInt(idString); // Transforme l'id en integer

	        boolean serverFound = false;
	        for (Server server : DataSource.serverList) {
	            if (server.getId() == id) {
	            	DataSource.selectedServer = server; // Sélectionne le serveur en fonction de l'id
	                serverFound = true;
	                break; // Arrête la boucle for si le serveur est trouvé
	            }
	        }

	        if (serverFound) {
	            Output.message("ID    STATE    Firewall    Services");
	            Output.message(DataSource.selectedServer.toString());
	            new InfoServerController().start();
	        } else {
	            Output.message("Error: Server with this ID does not exist.");
	        }

	    } catch (NumberFormatException e) {
	        Output.message("Error: Invalid ID format. Please enter a valid integer.");
	    }
	}
	
	
	// Action Ajouter un serveur
	private void addServer() {
	    String idString = Input.readString("Enter the new server's ID"); // Lire l'id entré par l'utilisateur

	    try {
	        int id = Integer.parseInt(idString); // Transforme l'id en integer

	        for (Server server : DataSource.serverList) {
	            if (server.getId() == id) {
	            	Output.message("Error: Server with this ID already exists.");
	                return; // Sortir de la méthode si l'ID n'est pas unique
	            }
	        }
	        DataSource.serverList.add(new Server(id)); // Ajoute le serveur
	        Output.message("Server added successfully.");
	        
	    } catch (NumberFormatException e) { // Si arrive pas à tranformer l'id en integer renvoie erreur
	    	Output.message("Error: Invalid ID format. Please enter a valid integer.");
	    }
	}

	
	// Action Supprimer un serveur
	private void deleteServer() {
		String idString = Input.readString("Enter the ID of the server to be deleted"); //Lire l'id entré par l'user
		int id = Integer.parseInt(idString);	//tranforme l'id en integer
		
		boolean removed = false;
        for (Server server : DataSource.serverList) {
            if (server.getId() == id) {
                DataSource.serverList.remove(server);
                Output.message("Server with ID " + id + " removed successfully.");
                removed = true; //indique qu'il a supprimé quelque chose
                break; // arrête la boucle for si serveur trouvé
            }
        }
        if (!removed) { //Si rien de supprimé après la boucle : erreur
            Output.message("Server with ID " + id + " not found.");
        }
    }
	
	
	// Action EXAMPLE_OBJECT
	private void startObjectController() {
		new ExampleObjectController().start();
	}

}
