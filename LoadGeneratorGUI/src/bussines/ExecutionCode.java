package bussines;

public class ExecutionCode {

	private String pcaPlugginName;


	
	//Constructor
	/**
	 * Execution Code constructor. Requires the PCA-Plugin Name
	 */
	public ExecutionCode(String name){

		pcaPlugginName = name;

	}
	
	//gets
	/**
	 * Returns the PCA-Pluggin Name
	 * @return pcaPlugginName
	 */
	public String getName(){return pcaPlugginName;}

	
	//sets
	/**
	 * Sets the the PCA-Pluggin Name
	 */
	public void setName(String n){pcaPlugginName = n;}

	
}
