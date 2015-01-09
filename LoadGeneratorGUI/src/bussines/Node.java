package bussines;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private String id;
	private List<NavigationTransition> to;
	private List<NavigationTransition> from;
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
	public List<NavigationTransition> getPredecessors(){return from;}
	public List<NavigationTransition> getDestinations(){return to;}
	public boolean isInitial(){return isInitial;}
	public String getProbability(){return probability;}
	
	public void setId(String id){this.id = id;}
	public void setProbability(String prob){probability=prob;}
	public void setInitial(boolean b){isInitial = b;}
	public void addPredecessor(NavigationTransition n)
	{
		if(from == null) from = new ArrayList<NavigationTransition>();
		from.add(n);
	//	System.out.println(n.toString());
	}
	
	public void addDestination(NavigationTransition n)
	{
		if(to == null) to = new ArrayList<NavigationTransition>();
		to.add(n);
	//	System.out.println(n.toString());
	}
	/*
	 * Removes the references coming from a given node. 
	 */
	public void removePredecessor(Node n)
	{
		for(int i=0; i < from.size(); i++)
		{
			if(from.get(i).getFrom().equals(n.getId())){ from.remove(i);}
		}
	}
	/*
	 * Removes all the transitions going to the given node-
	 */
	public void removeDestination(Node n)
	{
		for(int i=0; i < to.size(); i++)
		{
			if(from.get(i).getTo().equals(n.getId())){ from.remove(i);}
		}
	}
	public String toString(){return id;}
}
