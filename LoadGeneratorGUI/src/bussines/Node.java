package bussines;


public class Node {
	private String id;
	private boolean isInitial;
	private String probability;
	
	/**
	 * Constructor for non-initial nodes. Only requires an ID
	 */
	public Node(String id){
		
		this.id = id;
		isInitial=false;

		
	}
	/**
	 * Constructor for initial nodes. Requires ID and probability
	 */
	public Node(String id, String probability)
	{
		this.id = id;
		isInitial=true;
		this.probability = probability;

	}
	
	
	/**
	 * Returns the ID of the node
	 * @return id
	 */
	public String getId(){return id;}
	/**
	 * Returns true if the node is initial. Otherwise returns false
	 */
	public boolean isInitial(){return isInitial;}
	/**
	 * Returns the probability of an initial node
	 */
	public String getProbability(){return probability;}
	
	/**
	 * Sets the node ID
	 */
	public void setId(String id){this.id = id;}
	/**
	 * Sets the node probability
	 */
	public void setProbability(String prob){probability=prob;}
	/**
	 * True will set the node as initial. False will set the node as non-initial
	 */
	public void setInitial(boolean b){isInitial = b;}

	public String toString(){return id;}
}
