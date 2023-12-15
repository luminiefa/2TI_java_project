package control;

import control.menu.FirewallMenu;
import data.DataSource;
import model.Rule;
import ui.output.Output;

public class FirewallController extends MenuController<FirewallMenu.FirewallMenuAction> {

	// MainController uses a MainMenu
	public FirewallController() {
		super(new FirewallMenu());
	}

	public void processAction() {

		// Execute some action
		// Menu principal
		switch (this.getMenuAction()) {
		case LIST:
			this.listFirewallRules();
			break;
		case ADD:

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
	
	private void listFirewallRules() {
		Output.message("ACTION	PORT");
		for (Rule rule : DataSource.selectedServer.getFirewall().getRules()) {
			Output.message(rule.toString());
		}
	}

}
	
