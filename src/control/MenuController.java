package control;

import control.menu.Menu;
import control.menu.MenuAction;
import ui.output.MenuOutput;

/**
 * Abstract class
 * 
 * A controller to execute actions requested via a Menu
 * T is a MenuAction implementation, the class of action this controller must perform
 * 
 */
public abstract class MenuController<T extends MenuAction> extends Controller {

	// The menu this controller uses
	private Menu<T> menu;

	// If a request to exit this menu has been made
	private boolean exitFlag;
	
	// Constructor
	public MenuController(Menu<T> menu)
	{
		this.menu = menu;
	}

	// Displays the menu
	public void showMenu() {
		MenuOutput.print(this.menu.getItems());
	}

	// Request to exit the menu
	public void exitRequest() {
		this.exitFlag = true;
	}

	// Displays the menu and process the chosen action until an exit request is made
	public void start() {
		while (!this.exitFlag) {
			this.showMenu();
			this.processAction();
		}
	}
	
	// Returns the chosen action
	public T getMenuAction()
	{
		return this.menu.getMenuAction();
	}

	// Executes the chosen action
	public abstract void processAction();
}
