package bussines;

public class InputData {
	
	private String name;
	private String value;

	
	//Constructor
	public InputData(String name, String value){

		this.name = name;
		this.value = value;
	}
	
	//gets
	public String getName(){return name;}
	public String getValue(){return value;}
	
	//sets
	public void setName(String n){name = n; System.out.println("name set to: "+n);}
	public void setValue(String v){value = v; System.out.println("value set to: "+v);}
}
