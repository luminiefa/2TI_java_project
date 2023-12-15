package control.menu;

public class InfoServerMenu extends Menu<InfoServerMenu.InfoServerMenuAction> {

	public InfoServerMenu() {
		super();
		// Add all the menu items
		this.addItem("1", "Open the services menu", InfoServerMenuAction.SERVICE);
		this.addItem("2", "Open the firewall menu", InfoServerMenuAction.FIREWALL);
		this.addItem("3", "Exit server info", InfoServerMenuAction.EXIT);
	}

	/*
	 * The actions possible in this menu
	 */
	public enum InfoServerMenuAction implements MenuAction {
		SERVICE, FIREWALL, EXIT;
	}

}
