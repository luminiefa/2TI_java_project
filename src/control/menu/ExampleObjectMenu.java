package control.menu;

public class ExampleObjectMenu extends Menu<ExampleObjectMenu.ExampleObjectMenuAction> {

	public ExampleObjectMenu() {
		super();
		// Add all the menu items
		this.addItem("1", "Add new object", ExampleObjectMenuAction.ADD);
		this.addItem("2", "Display all objects", ExampleObjectMenuAction.DISPLAY);
		this.addItem("3", "Exit object menu", ExampleObjectMenuAction.EXIT);
	}

	/*
	 * The actions possible in this menu
	 */
	public enum ExampleObjectMenuAction implements MenuAction {
		ADD, DISPLAY, EXIT;
	}

}
