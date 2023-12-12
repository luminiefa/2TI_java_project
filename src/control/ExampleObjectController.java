package control;

import control.menu.ExampleObjectMenu;
import data.DataSource;
import model.ExampleObject;
import ui.input.ExampleObjectInput;
import ui.output.ExampleObjectOutput;

/**
 * Controller for the object menu
 */
public class ExampleObjectController extends MenuController<ExampleObjectMenu.ExampleObjectMenuAction> {

	// Uses an ExampleObjectMenu
	public ExampleObjectController() {
		super(new ExampleObjectMenu());
	}

	public void processAction() {

		// Execute some action
		switch (this.getMenuAction()) {
		case ADD:
			this.addObject();
			break;
		case DISPLAY:
			this.printObjects();
			break;
		case EXIT:
			this.exitRequest();
			break;
		}

	}

	// Action ADD
	private void addObject() {
		DataSource.DATA.add(ExampleObjectInput.readExampleObject());
	}

	// Action DISPLAY
	private void printObjects() {
		for (ExampleObject eo : DataSource.DATA)
			ExampleObjectOutput.printExampleObject(eo);
	}

}
