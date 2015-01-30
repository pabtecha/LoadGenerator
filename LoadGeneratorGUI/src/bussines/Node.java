package bussines;


public class Node {
	private String id;
	private boolean isInitial;
	private String probability;
	
	public Node(String id){
		
		this.id = id;
		isInitial=false;

		
	}
	
	public Node(String id, String probability)
	{
		this.id = id;
		isInitial=true;
		this.probability = probability;

	}
	
	
	
	public String getId(){return id;}
	public boolean isInitial(){return isInitial;}
	public String getProbability(){return probability;}
	
	public void setId(String id){this.id = id;}
	public void setProbability(String prob){probability=prob;}
	public void setInitial(boolean b){isInitial = b;}

	public String toString(){return id;}
}
