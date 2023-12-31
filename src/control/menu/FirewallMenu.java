package control.menu;

public class FirewallMenu extends Menu<FirewallMenu.FirewallMenuAction> {

	public FirewallMenu() {
		super();
		// Add all the menu items
		this.addItem("1", "List all firewall rules for this server", FirewallMenuAction.LIST);
		this.addItem("2", "Add a rule to this server's firewall", FirewallMenuAction.ADD);
		this.addItem("3", "Delete a rule to this server's firewall", FirewallMenuAction.DELETE);
		this.addItem("4", "Test if a port is open on this server", FirewallMenuAction.TEST);
		this.addItem("5", "Exit firewall menu", FirewallMenuAction.EXIT);
	}

	/*
	 * The actions possible in this menu
	 */
	public enum FirewallMenuAction implements MenuAction {
		LIST, ADD, DELETE, TEST, EXIT;
	}

}
