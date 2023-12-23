package control.menu;

public class MainMenu extends Menu<MainMenu.MainMenuAction> {

	public MainMenu() {
		super();
		// Add all the menu items
		this.addItem("1", "Show server list", MainMenuAction.LIST);
		this.addItem("2", "Select a server", MainMenuAction.INFO);
		this.addItem("3", "Add a server", MainMenuAction.ADD);
		this.addItem("4", "Delete a server", MainMenuAction.DELETE);
		this.addItem("5", "Move a server", MainMenuAction.MOVE);
		this.addItem("6", "Exit program", MainMenuAction.EXIT_PROGRAM);
	}

	/*
	 * The actions possible in this menu
	 */
	public enum MainMenuAction implements MenuAction {

		LIST, INFO, ADD, DELETE, MOVE, EXIT_PROGRAM

	}

}
