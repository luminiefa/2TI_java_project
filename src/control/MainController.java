package control;

import java.util.ArrayList;
import java.util.List;

import control.menu.MainMenu;
import data.DataSource;
import model.BareMetal;
import model.Container;
import model.Server;
import model.VM;
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
			new AddServerController().start();
			break;
		case DELETE:
			this.deleteServer();
			break;
		case MOVE:
			this.moveServer();
			break;
		case EXIT_PROGRAM:
			this.exitRequest();
			Output.message("Goodbye...");
			break;
		}
	}

	// Action Lister tous les serveurs
	private void listServer() {
		Output.message("ID	STATE	TYPE	HOST_ID");
		for (Server server : DataSource.serverList) {
			Output.message(server.toString());
		}
	}
	
	
	// Action infos sur un serveur
	private void infoServer() {
	    try {
	        int id = Integer.parseInt(Input.readString("Enter the server's ID")); // Transforme l'id en integer

	        boolean serverFound = false;
	        for (Server server : DataSource.serverList) {
	            if (server.getId() == id) {
	            	DataSource.selectedServer = server; // Sélectionne le serveur en fonction de l'id
	                serverFound = true;
	                break; // Arrête la boucle for si le serveur est trouvé
	            }
	        }

	        if (serverFound) {
	            Output.message("ID	STATE    TYPE	HOST_ID");
	            Output.message(DataSource.selectedServer.toString());
	            
	            if (DataSource.selectedServer.getHostedServers() != null) { //Si on sélectionne un serveur qui host des serveurs
	            	List<Integer> hostedIdList = new ArrayList<>();
	            	for (Server server : DataSource.selectedServer.getHostedServers()) {
	            		hostedIdList.add(server.getId());
	            	}
		            Output.message("Hosted servers : ");
		            Output.message(hostedIdList.toString());
	            }
	            
	            new InfoServerController().start();
	        } else {
	            Output.message("Error: Server with this ID does not exist.");
	        }

	    } catch (NumberFormatException e) {
	        Output.message("Error: Invalid ID format. Please enter a valid integer.");
	    }
	}

	
	// Action Supprimer un serveur
	private void deleteServer() {
		try {
			int id = Integer.parseInt(Input.readString("Enter the ID of the server to be deleted"));	//tranforme l'id en integer
			
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
		} catch (NumberFormatException e) {
	        Output.message("Error: Invalid ID format. Please enter a valid integer.");
	    }
    }
	
	
	// Action Move
	private void moveServer() {
		try {
			 int movedId = Integer.parseInt(Input.readString("Enter the ID of the server you want to move"));
		     int destinationId = Integer.parseInt(Input.readString("Enter the ID of the destination server"));

		     Server movedServer = null;
		     Server destinationServer = null;

		     boolean serverFound = false;

		       // Recherche des serveurs
		     for (Server server : DataSource.serverList) {
	         if (server.getId() == movedId) {
	             movedServer = server;
		         } else if (server.getId() == destinationId) {
		             destinationServer = server;
		         }

	          // Si les deux serveurs sont trouvés, sortir de la boucle
	          if (movedServer != null && destinationServer != null) {
	        	  serverFound = true;
		             break;
		         }
		     }

	        if (serverFound) {
	        	if ((destinationServer instanceof VM && !(movedServer instanceof Container))) {
	        		Output.message("A virtual machine can only host Container servers.");
	        	}
	        	else if (movedServer instanceof BareMetal) {
	        		Output.message("A BareMetal server cannot be hosted.");
	        	}
	        	else { // opération pour enlever le serveur de là où il est hébergé et l'installer dans le nouveau
	        		if (movedServer.getHost() != null) {
	        		movedServer.getHost().deleteHostedServer(movedServer);
	        		}
	        		destinationServer.addHostedServer(movedServer);
	        		Output.message("Server " + movedId + " is now hosted on server " + destinationId + ".");
	        	}
	        } else {
	            Output.message("Error: Server with this ID does not exist.");
	        }
		} catch (NumberFormatException e) {
	        Output.message("Error: Invalid ID format. Please enter a valid integer.");
	    }
		
		
	}

}
