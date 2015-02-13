package bussines;

import java.util.ArrayList;
import java.util.List;

public class StatisticAttribute {
	private String name;
	private List<StatisticAttribute> attrList;


	
	//Constructor
	/**
	 * Constructor for the statistic attribute. Requires the name.
	 */
	public StatisticAttribute(String name){

		this.name = name;
		attrList = new ArrayList<StatisticAttribute>();
	}
	
	//gets
	/**
	 * Returns the name of the statistic attribute 
	 */
	public String getName(){return name;}
	/**
	 * Returns the list of statistic attributes 
	 */
	public List<StatisticAttribute> getAttributes(){return attrList;}
	
	//sets
	/**
	 * Sets the name of the statistic attribute
	 */
	public void setName(String n){name = n;}
	
	/**
	 * Adds a statistic Attribute to the list	 */
	public void addStatisticAttribute(StatisticAttribute stat)
	{
		attrList.add(stat);
		System.out.println("Adding "+stat.getName()+" to HttpRoute");
	}
}
