package control.menu;

public class MainMenu extends Menu<MainMenu.MainMenuAction> {

	public MainMenu() {
		super();
		// Add all the menu items
		this.addItem("1", "Say hello", MainMenuAction.HELLO);
		this.addItem("2", "Object menu", MainMenuAction.EXAMPLE_OBJECT);
		this.addItem("3", "Exit program", MainMenuAction.EXIT_PROGRAM);
	}

	/*
	 * The actions possible in this menu
	 */
	public enum MainMenuAction implements MenuAction {

		HELLO, EXAMPLE_OBJECT, EXIT_PROGRAM

	}

}
