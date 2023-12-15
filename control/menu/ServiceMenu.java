package control.menu;

public class ServiceMenu extends Menu<ServiceMenu.ServiceMenuAction> {

	public ServiceMenu() {
		super();
		// Add all the menu items
		this.addItem("1", "List all services installed on this server", ServiceMenuAction.LIST);
		this.addItem("2", "Add a new service on this server", ServiceMenuAction.ADD);
		this.addItem("3", "Turn on/off a service on this server", ServiceMenuAction.SWITCH);
		this.addItem("4", "Delete a service on this server", ServiceMenuAction.DELETE);
		this.addItem("5", "Test if a service is available on this server", ServiceMenuAction.TEST);
		this.addItem("6", "Exit service menu", ServiceMenuAction.EXIT);
	}

	/*
	 * The actions possible in this menu
	 */
	public enum ServiceMenuAction implements MenuAction {
		LIST, ADD, SWITCH, DELETE, TEST, EXIT;
	}

}
