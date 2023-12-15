package ui.output;

/**
 * General class to manage the output to the user
 */
public class Output {
	
	// Prints message to error stream
	public static void error(String error)
	{
		System.err.println(error);
	}
	
	// Prints message to output stream
	public static void message(String message)
	{
		System.out.println(message);
	}
	
	// Prints prompt to output stream (without new line)
	public static void prompt(String prompt)
	{
		System.out.print(prompt);
	}

}
