package bussines;

import java.util.ArrayList;
import java.util.List;

public class StatisticAttribute {
	private String name;
	private List<StatisticAttribute> attrList;


	
	//Constructor
	public StatisticAttribute(String name){

		this.name = name;
		attrList = new ArrayList<StatisticAttribute>();
	}
	
	//gets
	public String getName(){return name;}
	public List<StatisticAttribute> getAttributes(){return attrList;}
	
	//sets
	public void setName(String n){name = n;}
	public void addStatisticAttribute(StatisticAttribute stat)
	{
		attrList.add(stat);
		System.out.println("Adding "+stat.getName()+" to HttpRoute");
	}
}
