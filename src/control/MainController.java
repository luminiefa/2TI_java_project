package control;

import control.menu.MainMenu;
import control.menu.MainMenu.MainMenuAction;
import ui.output.Output;

/**
 * Controller for the main menu
 */
public class MainController extends MenuController<MainMenuAction> {

	// MainController uses a MainMenu
	public MainController() {
		super(new MainMenu());
	}

	public void processAction() {

		// Execute some action
		switch (this.getMenuAction()) {
		case HELLO:
			this.sayHello();
			break;
		case EXAMPLE_OBJECT:
			this.startObjectController();
			break;
		case EXIT_PROGRAM:
			this.exitRequest();
			Output.message("Goodbye...");
			break;
		}
		
	}

	// Action HELLO
	private void sayHello() {
		Output.message("hello");
	}

	// Action EXAMPLE_OBJECT
	private void startObjectController() {
		new ExampleObjectController().start();
	}

}
