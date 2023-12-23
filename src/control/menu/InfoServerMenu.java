package control.menu;

public class InfoServerMenu extends Menu<InfoServerMenu.InfoServerMenuAction> {

	public InfoServerMenu() {
		super();
		// Add all the menu items
		this.addItem("1", "Turn On/Off the server", InfoServerMenuAction.SWITCH);
		this.addItem("2", "Open the services menu", InfoServerMenuAction.SERVICE);
		this.addItem("3", "Open the firewall menu", InfoServerMenuAction.FIREWALL);
		this.addItem("4", "Exit server info", InfoServerMenuAction.EXIT);
	}

	/*
	 * The actions possible in this menu
	 */
	public enum InfoServerMenuAction implements MenuAction {
		SWITCH, SERVICE, FIREWALL, EXIT;
	}

}
