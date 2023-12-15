package control;

import control.menu.ServiceMenu;
import data.DataSource;
import model.Service;
import model.Enumeration.State;
import ui.input.Input;
import ui.output.Output;

public class ServiceController extends MenuController<ServiceMenu.ServiceMenuAction> {

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

			break;
		case TEST:

			break;
		case EXIT:
			this.exitRequest();
			break;
		}
	}
	

	private void listServices() {
		Output.message("ID	STATE	PORT");
		for (Service service : DataSource.selectedServer.getInstalledServices()) {
			Output.message(service.toString());
		}
	}
	
	
	private void addService() {
		String name = Input.readString("Enter the service name");
		String portString = Input.readString("Enter the service port");
		try {
			int port = Integer.parseInt(portString); // Transforme le port en integer
			
			for (Service service : DataSource.selectedServer.getInstalledServices()) {
	            if (service.getId() == name) {
	            	Output.message("Error: Service with this name already exists.");
	                return; // Sortir de la méthode si le nom n'est pas unique
	            }
	        }
			DataSource.selectedServer.installService(new Service(name, port)); //Ajoute le service
			Output.message("Service " + name + " has been installed on port " + port);
			
		} catch (NumberFormatException e) { // Si arrive pas à tranformer le port en integer renvoie erreur
	    	Output.message("Error: Invalid port format. Please enter a valid integer.");
	    }
	}
	
	
	private void switchService() {
		String name = Input.readString("Enter the service name");
		
		Service selectedService = null;
		boolean serviceFound = false;
        for (Service service : DataSource.selectedServer.getInstalledServices()) {
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
}