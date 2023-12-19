package model;

/**
 * A type of data our program wants to manipulate
 */
public class ExampleObject {
	
	private String value;

	public ExampleObject(String value) {
		super();
		this.value = value;
	}

	@Override
	public String toString() {
		return "ExampleObject [value=" + value + "]";
	}

}
