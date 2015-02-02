package bussines;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class Navigation {

	public String id;
	public InputData inputData;
	public ExecutionCode exCode;
	public List<StatisticAttribute> statAttr;
	
	//Constructor
	public Navigation(String id, InputData inData, ExecutionCode ec){

		this.id = id;
		this.inputData = inData;
		this.exCode = ec;
		statAttr = new ArrayList<StatisticAttribute>();
	}
	
	
	//gets
	public String getId(){return id;}
	public InputData getInputData(){return inputData;}
	public ExecutionCode getExecutionCode(){return exCode;}
	public List<StatisticAttribute> getStatisticAttributes(){return statAttr;}
	
	//sets
	public void setId(String id){this.id=id;}
	public void setInputData(InputData inData){inputData = inData;}
	public void setExecutionCode(ExecutionCode ec){exCode = ec;}
	public void setStatisticAttributes(List<StatisticAttribute> sa){statAttr = sa;}
	
	public void addStatisticAttribute(StatisticAttribute stat)
	{
		statAttr.add(stat);
		System.out.println("Adding "+stat.getName()+" to navigation");
	}	
	
	/*
	 * Create a XML file from the navigation information and stores it in the given path.
	 */
	public void createXML(String path)
	{
		try {
			Element nav = new Element("Navigation");
			nav.setAttribute("id", getId());
			Document doc = new Document(nav);



			
			Element inData = new Element("InputData");
			Element param = new Element("Param");
			
			param.setAttribute("name", inputData.getName());
			param.setAttribute("value", inputData.getValue());
			
			inData.addContent(param);
			
			doc.getRootElement().addContent(inData);
			
			Element exec = new Element("ExecutionCode");
			Element pca = new Element("PCA-Plugin");
			pca.setAttribute("name", exCode.getName());
			
			exec.addContent(pca);
			
			doc.getRootElement().addContent(exec);
			
			Element statConfig = new Element("StatisticsConfiguration");
			
			for(int i=0; i<statAttr.size(); i++)
			{
				Element stat = new Element("StatisticAttribute");
				stat.setAttribute("name", statAttr.get(i).getName());
				System.out.println(statAttr.get(i).getName());
				if(statAttr.get(i).getName().equals("HttpRoute"))
				{	List<StatisticAttribute> httpStats = statAttr.get(i).getAttributes();

					for(int j=0; j<httpStats.size();j++)
					{
					Element statHttp = new Element("StatisticAttribute");
					System.out.println("adding "+httpStats.get(j).getName());
					statHttp.setAttribute("name", httpStats.get(j).getName());
					statHttp.detach();
					stat.addContent(statHttp);
					}
				}
				statConfig.addContent(stat);
				
			}
			
			doc.getRootElement().addContent(statConfig);
			
			
			// new XMLOutputter().output(doc, System.out);
			XMLOutputter xmlOutput = new XMLOutputter();
	 
			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter(path));
	 
			System.out.println("File Saved!");
	  } catch (IOException io) {
		System.out.println(io.getMessage());
	  }
	}
	
	/*
	 * Read the XML file given and parses it to generate a workload.
	 */
	
	public void readXML(String path)
	{
		  SAXBuilder builder = new SAXBuilder();
		  File xmlFile = new File(path);
		  

		  try {
	 
			
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			
			id = rootNode.getAttributeValue("id");
						
			Element inputD = rootNode.getChild("InputData");	
			
			inputData.setName(inputD.getChild("Param").getAttributeValue("name"));
			inputData.setValue(inputD.getChild("Param").getAttributeValue("value"));
			
			Element execution = rootNode.getChild("ExecutionCode");
			
			exCode.setName(execution.getChild("PCA-Plugin").getAttributeValue("name"));
			
			Element statConfig = rootNode.getChild("StatisticsConfiguration");
			List<Element> statAttributes = statConfig.getChildren("StatisticAttribute");
			
			for(int i=0; i<statAttributes.size();i++)
			{
				Element stat = statAttributes.get(i);
				StatisticAttribute attr = new StatisticAttribute(stat.getAttributeValue("name"));
				
				if(attr.getName().equals("HttpRoute"))
				{
					List<Element> httpAttr = stat.getChildren();
					for(int j=0; j<httpAttr.size();j++)
					{
						Element http = httpAttr.get(j);
						attr.addStatisticAttribute(new StatisticAttribute(http.getAttributeValue("name")));
					}
				}
				addStatisticAttribute(attr);
					
			}
			
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }
		}
	
	public boolean isEmpty()
	{
		if(id.equals(""))
			return true;
		if(inputData.getName().equals("") || inputData.getValue().equals(""))
			return true;
		if(exCode.getName().equals(""))
			return true;

	return false;
	}
}
