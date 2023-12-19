package control;

import control.menu.InfoServerMenu;

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
}
