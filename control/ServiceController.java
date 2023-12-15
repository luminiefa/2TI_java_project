package control;

import control.menu.ServiceMenu;
import model.Service;
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

			break;
		case SWITCH:

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
		for (Service service : MainController.selectedServer.getInstalledServices()) {
			Output.message(service.toString());
		}
	}
}
