package bussines;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XMLParser {

	WorkloadTest wl;
	Navigation navi;
	public static void main(String[] args) {
		
		XMLParser demo = new XMLParser();
		 demo.readXMLNavigation("/home/caresiz/GuernicaGUI/foo2.xml");
		 demo.createXMLNavigation("/home/caresiz/GuernicaGUI/foo3.xml");
	}
	
	public void createXMLNavigation(String path)
	{
		try {
			Element nav = new Element("Navigation");
			nav.setAttribute("id", navi.getId());
			Document doc = new Document(nav);



			
			Element inData = new Element("InputData");
			Element param = new Element("Param");
			
			param.setAttribute("name", navi.getInputData().getName());
			param.setAttribute("value", navi.getInputData().getValue());
			
			inData.addContent(param);
			
			doc.getRootElement().addContent(inData);
			
			Element exec = new Element("ExecutionCode");
			Element pca = new Element("PCA-Plugin");
			pca.setAttribute("name", navi.getExecutionCode().getName());
			
			exec.addContent(pca);
			
			doc.getRootElement().addContent(exec);
			
			Element statConfig = new Element("StatisticsConfiguration");
			
			for(int i=0; i<navi.getStatisticAttributes().size(); i++)
			{
				Element stat = new Element("StatisticAttribute");
				stat.setAttribute("name", navi.getStatisticAttributes().get(i).getName());
				System.out.println(navi.getStatisticAttributes().get(i).getName());
				if(navi.getStatisticAttributes().get(i).getName().equals("HttpRoute"))
				{	List<StatisticAttribute> httpStats = navi.getStatisticAttributes().get(i).getAttributes();

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
	
	public void readXMLNavigation(String path)
	{
		  SAXBuilder builder = new SAXBuilder();
		  File xmlFile = new File(path);
		  
		  navi = new Navigation("",new InputData("",""),new ExecutionCode(""));
		  
		  try {
	 
			
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			
			navi.setId(rootNode.getAttributeValue("id"));
						
			Element inputD = rootNode.getChild("InputData");	
			
			InputData inputData = new InputData("","");
			inputData.setName(inputD.getAttributeValue("name"));
			inputData.setValue(inputD.getAttributeValue("value"));
			
			navi.setInputData(inputData);
			Element execution = rootNode.getChild("ExecutionCode");
			
			ExecutionCode exCode = new ExecutionCode("");
			exCode.setName(execution.getChild("PCA-Plugin").getAttributeValue("name"));
			navi.setExecutionCode(exCode);
			
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
				navi.addStatisticAttribute(attr);
					
			}
			
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }
		}
	
	public void createXML()
	{
		try {
			Element workload = new Element("WorkloadTest");
			workload.setAttribute("id", wl.getId());
			workload.setAttribute("serializerClass", wl.getSerializerClass());
			workload.detach();
			Document doc = new Document(workload);



			
			Element userNum = new Element("UsersNumber");
			userNum.setText(wl.getUsersNumber());
			
			doc.getRootElement().addContent(userNum);
			
			Element navigationGraph = new Element("NavigationGraph");
			
			//Add all the initial navigations.
			Element initialNavigations = new Element("InitialNavigations");
			
			List<Node> initialNodes = wl.getInitialNavigation();
			for(int i=0; i < initialNodes.size(); i++)
			{
				Element iniNav = new Element("InitialNavigation");
				iniNav.setAttribute("id", initialNodes.get(i).getId());
				iniNav.setAttribute("probability", initialNodes.get(i).getProbability());
				initialNavigations.addContent(iniNav);
			}
			
			navigationGraph.addContent(initialNavigations);
			
			//Add all the navigation transitions.
			Element navigationTransitions = new Element("NavigationTransitions");
			
			List<NavigationTransition> transitions = wl.getNavigationTransition();
			for(int i=0; i < transitions.size(); i++)
			{
				Element trans = new Element("NavigationTransition");
				trans.setAttribute("from", transitions.get(i).getFrom());
				trans.setAttribute("to", transitions.get(i).getTo());
				trans.setAttribute("probability", transitions.get(i).getProbability());
				navigationTransitions.addContent(trans);
			}
			
			navigationGraph.addContent(navigationTransitions);
			
			
			doc.getRootElement().addContent(navigationGraph);
	 
			
			// new XMLOutputter().output(doc, System.out);
			XMLOutputter xmlOutput = new XMLOutputter();
	 
			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter("/home/caresiz/GuernicaGUI/prueba.xml"));
	 
			System.out.println("File Saved!");
	  } catch (IOException io) {
		System.out.println(io.getMessage());
	  }
	}
	
	
	
	
	
	
	public void readXML()
	{
		  SAXBuilder builder = new SAXBuilder();
		  File xmlFile = new File("workload.xml");
	 
		  try {
	 
			
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			System.out.println("ID: "+rootNode.getAttributeValue("id"));
			System.out.println("Serializer: "+rootNode.getAttributeValue("serializerClass"));
			System.out.println("Users Number: "+rootNode.getChildText("UsersNumber"));
			
			wl = new WorkloadTest(rootNode.getAttributeValue("id"),rootNode.getChildText("UsersNumber"),rootNode.getAttributeValue("serializerClass"));
			
			Element graph = rootNode.getChild("NavigationGraph");	
			
			Element iniNavs = graph.getChild("InitialNavigations");
			List<Element> iniNav = iniNavs.getChildren("InitialNavigation");
			
			Element navTrans = graph.getChild("NavigationTransitions");
			List<Element> navTran = navTrans.getChildren();
			
			System.out.println("Number of initial navigations: "+iniNav.size());
				
			for (int i = 0; i < iniNav.size(); i++) {
			   Element node = (Element) iniNav.get(i);
			   wl.addNode(new Node(node.getAttributeValue("id"),node.getAttributeValue("probability")));
	//		   System.out.println("ID : " + node.getAttributeValue("id"));
	//		   System.out.println("Prob : " + node.getAttributeValue("probability"));	 
			}
			
			System.out.println("Number of navigation transitions: "+navTran.size());
			
			for(int i=0; i < navTran.size(); i++)
			{
				Element node = (Element) navTran.get(i);
				List<Node> nodes = wl.getNodes();
				Node newNode = new Node(node.getAttributeValue("to"));
				if(!nodes.contains(newNode)) wl.addNode(newNode);
					
				wl.addTransition(new NavigationTransition(node.getAttributeValue("from"),node.getAttributeValue("to"),node.getAttributeValue("probability")));
	//			System.out.println("From: "+node.getAttributeValue("from"));
	//			System.out.println("To: "+node.getAttributeValue("to"));
	//			System.out.println("Prob: "+node.getAttributeValue("probability"));
			
			}
	 
			wl.printGraph();
			
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }
		}
	}
	
	
	
	
	

