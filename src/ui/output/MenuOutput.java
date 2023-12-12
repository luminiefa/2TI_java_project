package ui.output;

import java.util.Map;

import control.menu.MenuAction;
import control.menu.MenuItem;

/**
 * Managing Menu output
 */
public class MenuOutput {

	// Displays the menu items
	public static <T extends MenuAction> void print(Map<String, MenuItem<T>> items) {
		for (MenuItem<T> item : items.values())
			System.out.println(item.getKey() + ") " + item.getText());
	}

}
