package bussines;

public class InputData {
	
	private String name;
	private String value;

	
	//Constructor
	/**
	 * Input Data constructor. Requires a name and a value.
	 */
	public InputData(String name, String value){

		this.name = name;
		this.value = value;
	}
	
	//gets
	/**
	 * Returns the input data name
	 * @return name
	 */
	public String getName(){return name;}
	/**
	 * Returns the input data value
	 * @return value
	 */
	public String getValue(){return value;}
	
	//sets
	/**
	 * Sets the input data name
	 */
	public void setName(String n){name = n; System.out.println("name set to: "+n);}
	/**
	 * Sets the input data value
	 */
	public void setValue(String v){value = v; System.out.println("value set to: "+v);}
}
