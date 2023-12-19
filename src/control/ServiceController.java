package control;

import java.util.HashSet;

import control.menu.ServiceMenu;
import data.DataSource;
import model.Service;
import model.Enumeration.State;
import ui.input.Input;
import ui.output.Output;

public class ServiceController extends MenuController<ServiceMenu.ServiceMenuAction> {

	private HashSet<Service> services = DataSource.selectedServer.getInstalledServices();
	
	// MainController uses a MainMenu
	public ServiceController() {
		super(new ServiceMenu());
	}

	public void processAction() {

		// Execute some action
		// Menu principal
		switch (this.getMenuAction()) {
		case LIST:
			this.listServices();
			break;
		case ADD:
			this.addService();
			break;
		case SWITCH:
			this.switchService();
			break;
		case DELETE:
			this.deleteService();
			break;
		case TEST:
			this.testService();
			break;
		case EXIT:
			this.exitRequest();
			break;
		}
	}
	

	private void listServices() {
		Output.message("ID	STATE	PORT");
		for (Service service : services) {
			Output.message(service.toString());
		}
	}
	
	
	private void addService() {
		String name = Input.readString("Enter the service name");
		String portString = Input.readString("Enter the service port");
		try {
			int port = Integer.parseInt(portString); // Transforme le port en integer
			
			if (port >= 1 && port <= 1024) {
				for (Service service : services) {
		            if (service.getId() == name) {
		            	Output.message("Error: Service with this name already exists.");
		                return; // Sortir de la méthode si le nom n'est pas unique
		            }
		        }
				DataSource.selectedServer.installService(new Service(name, port)); //Ajoute le service
				Output.message("Service " + name + " has been installed on port " + port);
				
			} else {Output.message("Error: Invalid port format. Please enter a number between 1 and 1024.");}
				
			
		} catch (NumberFormatException e) { // Si arrive pas à tranformer le port en integer renvoie erreur
	    	Output.message("Error: Invalid port format. Please enter a valid integer.");
	    }
	}
	
	
	private void switchService() {
		String name = Input.readString("Enter the service name");
		
		Service selectedService = null;
		boolean serviceFound = false;
        for (Service service : services) {
            if (service.getId().equals(name)) { //peut pas utiliser == car compare deux String
            	selectedService = service; //selectionne le service choisis par l'user
            	serviceFound = true;
                break; // Arrête la boucle for si le service est trouvé
            }
        }

        if (serviceFound) {
        	if (selectedService.getState() == State.DOWN) {
        		selectedService.setState(State.UP);
        		Output.message(selectedService.getId() + " service status has been changed to UP.");
        	} else {
        		selectedService.setState(State.DOWN);
        		Output.message(selectedService.getId() + " service status has been changed to DOWN.");
        	}
           
            
        } else {
            Output.message("Error: Service with this name does not exist.");
        }
		
	}
	
	
	private void deleteService() {
		String name = Input.readString("Enter the ID of the server to be deleted"); //Lire le nom entré par l'user
		
		boolean removed = false;
        for (Service service : services) {
            if (service.getId().equals(name)) {
            	DataSource.selectedServer.uninstallService(service);;
                Output.message("Service " + name + " removed successfully.");
                removed = true; //indique qu'il a supprimé quelque chose
                break; // arrête la boucle for si service trouvé
            }
        }
        if (!removed) { //Si rien de supprimé après la boucle : erreur
            Output.message("Service " + name + " not found.");
        }
    }
	
	
	private void testService() {
		String name = Input.readString("Enter the service name");
		
		Service selectedService = null;
		boolean serviceFound = false;
        for (Service service : services) {
            if (service.getId().equals(name)) { //peut pas utiliser == car compare deux String
            	selectedService = service; //selectionne le service choisis par l'user
            	serviceFound = true;
                break; // Arrête la boucle for si le service est trouvé
            }
        }
        
        if (serviceFound) {
        	if (DataSource.selectedServer.isServiceAvailable(selectedService)) {
        		Output.message("Service " + name + " IS available.");
        	} else {
        		Output.message("Service " + name + " IS NOT available.");
        	}
           
            
        } else {
            Output.message("Error: Service with this name does not exist.");
        }
	}
	
}
