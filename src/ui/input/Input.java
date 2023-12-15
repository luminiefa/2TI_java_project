package ui.input;

import java.util.InputMismatchException;
import java.util.Scanner;

import ui.output.Output;

/**
 * General class to manage the inputs of the user
 */
public class Input {

	// The only Scanner used by the program
	public static final Scanner SCANNER = new Scanner(System.in);

	//  Get an int from the user, displays prompt
	public static int readInt(String prompt) {
		while (true)
			try {
				Output.prompt(prompt + " : ");
				return SCANNER.nextInt(); // Read next int
			} catch (InputMismatchException e) {
				Output.error("Invalid input, try again");
				SCANNER.nextLine(); // Empty the scanner
			}
	}
	
	// Get a String from the user, displays prompt
	public static String readString(String prompt)
	{
		Output.prompt(prompt + " : ");
		return SCANNER.next(); // Read next String
	}

}
