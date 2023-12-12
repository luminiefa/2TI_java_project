package ui.input;

import java.util.Set;

import ui.output.Output;

/**
 * Manages the input at the menu level
 */
public class MenuInput {

	// Reads a valid string for a menu choice
	public static String readMenuChoice(Set<String> keySet) {
		
		String s = Input.readString("Choose an option");

		while (!keySet.contains(s))
		{
			Output.error("Invalid choice, try again");
			s = Input.readString("Choose an option");
		}

		return s;
	}

}
