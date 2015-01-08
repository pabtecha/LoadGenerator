package bussines;

public class NavigationTransition {

	private String from;
	private String to;
	private String probability;
	

	//Constructor
	public NavigationTransition(String from, String to, String probability){

		this.from = from;
		this.to = to;
		this.probability = probability;
	}
	
	//gets
	public String getFrom(){return from;}
	public String getTo(){return to;}
	public String getProbability(){return probability;}
	
	//sets
	public void setFrom(String f){this.from = f;}
	public void setTo(String t){this.to = t;}
	public void setProbability(String p){probability = p;}
	
	public String toString(){return probability;}
}
