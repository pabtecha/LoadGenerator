package bussines;

public class NavigationTransition {

	private String from;
	private String to;
	private String probability;
	

	//Constructor
	/**
	 * NavigationTransition constructor. Specifying the source, destination and probability
	 */
	public NavigationTransition(String from, String to, String probability){

		this.from = from;
		this.to = to;
		this.probability = probability;
	}
	
	//gets
	/**
	 * Returns the id of the source node
	 * @return from
	 */
	public String getFrom(){return from;}
	/**
	 * Returns the id of the destination node
	 * @return to
	 */
	public String getTo(){return to;}
	/**
	 * Returns the probability of transition
	 * @return probability
	 */
	public String getProbability(){return probability;}
	
	//sets
	/**
	 * Sets the source node id
	 */
	public void setFrom(String f){this.from = f;}
	/**
	 * Sets the destination node id
	 */
	public void setTo(String t){this.to = t;}
	/**
	 * Sets the probability of transition
	 */
	public void setProbability(String p){probability = p;}
	
	public String toString(){return from+"-"+to+"-"+probability;}
}
