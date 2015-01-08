package bussines;

public class ExecutionCode {

	private String pcaPlugginName;


	
	//Constructor
	public ExecutionCode(String name){

		pcaPlugginName = name;

	}
	
	//gets
	public String getName(){return pcaPlugginName;}

	
	//sets
	public void setName(String n){pcaPlugginName = n;}

	
}
