package ui.input;

import model.ExampleObject;

/**
 * Manages ExampleObjetInput
 */
public class ExampleObjectInput {
	
	// To read and create an object from the keyboard
	public static ExampleObject readExampleObject()
	{
		String value = Input.readString("Enter value");
		return new ExampleObject(value);
	}

}
