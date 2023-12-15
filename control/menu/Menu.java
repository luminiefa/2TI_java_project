package control.menu;

import java.util.HashMap;
import java.util.Map;

import ui.input.MenuInput;

/**
 * Abstract class of a Menu
 * @param <T> The type of actions this menu allows
 */
public abstract class Menu<T extends MenuAction> {

	// The items of this menu
	// key is the key inside the menu item
	private Map<String, MenuItem<T>> items = new HashMap<>();

	// Add a new item
	public void addItem(String key, String text, T action) {
		this.items.put(key, new MenuItem<T>(key, text, action));
	}

	// Get the action chosen by the user
	public T getMenuAction() {
		String choice = MenuInput.readMenuChoice(items.keySet());
		return this.items.get(choice).getAction(); 
	}

	public Map<String, MenuItem<T>> getItems() {
		return this.items;
	}
}
