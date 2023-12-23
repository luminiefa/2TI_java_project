package control;

import control.menu.InfoServerMenu;
import data.DataSource;
import model.Enumeration.State;
import model.Server;
import ui.output.Output;

/**
 * Controller for the object menu
 */
public class InfoServerController extends MenuController<InfoServerMenu.InfoServerMenuAction> {

	// Uses an ExampleObjectMenu
	public InfoServerController() {
		super(new InfoServerMenu());
	}

	public void processAction() {

		// Execute some action
		switch (this.getMenuAction()) {
		case SWITCH:
			this.switchServerState();
			break;
		case SERVICE:
			new ServiceController().start();
			break;
		case FIREWALL:
			new FirewallController().start();
			break;
		case EXIT:
			this.exitRequest();
			break;
		}

	}

	public void switchServerState() {
		Server server = DataSource.selectedServer;
		if (server.getState() == State.DOWN) {
			server.setState(State.UP);
			Output.message("Server " + server.getId() + " status has been changed to UP.");
		} else {
			server.setState(State.DOWN);
			Output.message("Server " + server.getId() + " status has been changed to DOWN.");
		}
	}

}
