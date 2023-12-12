package control.menu;

/**
 * An item of a menu
 * 
 * @param <T> The type of action this item allows
 */
public class MenuItem<T extends MenuAction> {

	// The String used to chose this item in a menu
	private String key;

	// The display text of this menu item
	private String text;

	// The action associated with this menu item
	private T action;

	public MenuItem(String key, String text, T action) {
		super();
		this.key = key;
		this.text = text;
		this.action = action;
	}

	public T getAction() {
		return this.action;
	}

	public String getKey() {
		return key;
	}

	public String getText() {
		return text;
	}

}
