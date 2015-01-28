package bussines;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;


public class WorkloadTest {
	
	private String id;
	private String usersNumber;
	private String serializerClass;
	private List<Node> initialNavigation;
	private List<NavigationTransition> navigationTransition;
	private List<Node> nodes;
	private mxGraph graph;
	private mxGraphComponent graphComponent;
	private int width = 50;
	private int height = 50;
	private Hashtable<String,Object> vertices;
	private Hashtable<String,Object> edges;
	//Constructor
	public WorkloadTest(String id, String usersNumber,String serializerClass){

		this.id = id;
		this.usersNumber = usersNumber;
		this.serializerClass = serializerClass;
		initialNavigation = new ArrayList<Node>();;
		navigationTransition = new ArrayList<NavigationTransition>();;
		nodes = new ArrayList<Node>();
		vertices = new Hashtable<String,Object>(30);
		edges = new Hashtable<String,Object>(30);
		setUpGraph();
	}
	
	
	//gets
	public String getId(){return id;}
	public String getUsersNumber(){return usersNumber;}
	public String getSerializerClass(){return serializerClass;}
	public List<Node> getInitialNavigation(){return initialNavigation;}
	public List<NavigationTransition> getNavigationTransition(){return navigationTransition;}
	public List<Node> getNodes(){return nodes;}
	
	/*
	 * Searches the node with the given id and returns it. Returns null if the node doesn't exists
	 */
	public Node getNodeById(String id)
	{
			
		for(int i=0; i<nodes.size(); i++)
		{
			if(nodes.get(i).getId().equals(id)) return nodes.get(i);
		}
			
		return null;
	}
	
	public Node getInitialNodeById(String id)
	{
		for(int i=0; i<initialNavigation.size(); i++)
		{
			if(initialNavigation.get(i).getId().equals(id)) return initialNavigation.get(i);
		}
		
		return null;
	}
	/*Searches a node by Id looking without considering whether it is an initial node or not. Returns null if the node doesn't exists*/
	public Node getVertexById(String id)
	{
		Node n;
		n= getNodeById(id);
		if(n==null) n= getInitialNodeById(id);
		
	return n;
	}
	
	
	//sets
	public void setId(String id){this.id=id; if(id == "") System.out.println("id changed to empty");}
	public void setUsersNumber(String num){usersNumber=num;}
	public void setSerializerClass(String sc){serializerClass=sc;}
	
	
	/*
	 * Set up the graph configuration.
	 */
	public void setUpGraph()
	{
		graph = new mxGraph();
		graph.setCellsBendable(false);
		graph.setCellsCloneable(false);
		graph.setCellsDeletable(true);
		graph.setCellsDisconnectable(false);
		graph.setCellsResizable(false);
		graph.setLabelsClipped(true);
		graph.setCellsEditable(false);
		graph.setCellsEditable(false);

		graphComponent = new mxGraphComponent(graph);

	}
	
	/*
	 * If the node does not already exist, it is added to the list and returns true
	 */
	
	public boolean addNode(Node n)
	{
		boolean ex;
		
		ex = exists(n);
		if (!ex)
		{
			if(n.isInitial())
			{
				System.out.println("Initial node added correctly");
				initialNavigation.add(n);
			}else
			{
				System.out.println("node added correctly");
				nodes.add(n);
			}
		}

		
	return !ex;
	}
	/*
	 * Updates a node, setting its new values on both the workload and the graph representation.
	 */
	
	public boolean updateNode(Node n, Object cell)
	{
		mxCell c = (mxCell) cell;
		Node old = getVertexById(c.getId());
		boolean wasInitial = false;
		int pos = -1;
		boolean exists = false;
		boolean sameNode = false;
		
		if(n.getId().equals(old.getId()))
			sameNode = true;
		//search the position where the old node was, and check if the new id is already taken
		for(int i=0; i < initialNavigation.size(); i++)
		{
			if(initialNavigation.get(i).getId().equals(old.getId()))
			{
				pos=i;
				wasInitial = true;
			}
			if(initialNavigation.get(i).getId().equals(n.getId()))
				exists = true;
		}
		
		for(int i =0; i < nodes.size(); i++)
		{
			if(nodes.get(i).getId().equals(old.getId()))
				pos = i;
			if(nodes.get(i).getId().equals(n.getId()))
				exists = true;			
		}
		
		if(exists && !sameNode) //node already exists
			return false;
		
		
		//update predecessor and destination lists
		if(!sameNode)
		{
			for(int i=0; i< old.getDestinations().size(); i++)
			{
				Node dest = getVertexById(old.getDestinations().get(i).getTo());
				dest.editPredecessor(old.getId(), n.getId());					
			}
			
			for(int i=0; i<old.getPredecessors().size(); i++)
			{
				Node pred = getVertexById(old.getPredecessors().get(i).getFrom());
				pred.editDestination(old.getId(), n.getId());
			}
		}

		if(wasInitial)
		{
			if(n.isInitial())
			{
				initialNavigation.get(pos).setId(n.getId());
				initialNavigation.get(pos).setProbability(n.getProbability());
			}else
			{   //remove the old node, update it and add to nodes
				initialNavigation.remove(pos);
				old.setId(n.getId());
				old.setInitial(false);
				nodes.add(old);
			}

				
				
		}else
		{
			if(n.isInitial())
			{
				nodes.remove(pos);
				old.setId(n.getId());
				old.setInitial(true);
				old.setProbability(n.getProbability());
				initialNavigation.add(old);
			}else
				nodes.get(pos).setId(n.getId());
		}
		
		
		mxCell vertex = (mxCell) cell;	
		vertex = (mxCell) vertices.get(vertex.getId());
		
		if(vertex != null)
		{
			for(int i=0; i < graph.getChildEdges(cell).length; i++)
			{
				mxCell ed = (mxCell) graph.getChildEdges(cell)[i];
				NavigationTransition nav = (NavigationTransition) ed.getValue();
				if(ed.getSource().getId().equals(vertex.getId()))
				{
					
					updateEdge(n.getId(),nav.getTo(),nav.getProbability(),ed);
				}
				if(ed.getTarget().getId().equals(vertex.getId()))
				{
					updateEdge(nav.getFrom(), n.getId(),nav.getProbability(),ed);
				}
			}
			
			vertex.setId(n.getId());
			graph.cellLabelChanged(vertex, n.getId(), false);	
		}
		
		if(n.isInitial())
		{
			vertex.setStyle("shape=ellipse;fillColor=yellow");
		}else
		{
			vertex.setStyle("shape=ellipse");
		}

	

		
		
	
	return true;
	}
	/*
	 * Deletes the node from the graph and all the transitions going from and to the node.
	 */	
	public void deleteNode(Node n)
	{
		System.out.println("Received node "+n.getId()+" for deletion");
		if(n.getProbability()!=null) System.out.println(n.getProbability());
		if(n.getDestinations()!=null)	System.out.println(n.getDestinations().toString());
		if(n.getPredecessors()!=null)System.out.println(n.getPredecessors().toString());

		if(n.isInitial())
		{
						
			for(int i=0; i < n.getDestinations().size(); i++)
			{
				
				for(int j=0; j < initialNavigation.size(); j++)
				{
					if(n.getDestinations().get(i).getTo().equals(initialNavigation.get(j).getId()))
					{
						nodes.get(j).removePredecessor(n.getId());
					}
						
				}
				for(int j=0; j < nodes.size(); j++)
				{
					if(n.getDestinations().get(i).getTo().equals(nodes.get(j).getId()))
					{
						nodes.get(j).removePredecessor(n.getId());
					}
						
				}
								
			}
			initialNavigation.remove(n);
		}else
		{
			nodes.remove(n);
			System.out.println("node removed");
			for(int i=0; i < nodes.size(); i++)
			{
				System.out.println("nodes size:"+nodes.size());
				for(int j=0; j < n.getDestinations().size(); j++)
				{
					System.out.println(n.getId()+" destinations = "+n.getDestinations().size());
					if(n.getDestinations().get(j).getTo().equals(nodes.get(i).getId()))
					{
						nodes.get(i).removePredecessor(n.getId());
						System.out.println("removing "+n.getId()+" as a destination of "+nodes.get(i));
					}

				}
				
				for(int k=0; k < n.getPredecessors().size(); k++)
				{
					System.out.println(n.getId()+" has"+n.getPredecessors().size() +" predecessors");
					if(n.getPredecessors().get(k).getFrom().equals(nodes.get(i).getId()))
					{
						nodes.get(i).removeDestination(n.getId());
						System.out.println("removing "+n.getId()+" as a predecessor of "+nodes.get(i));
					}
						
				}
			}
			
		}
			graph.getModel().beginUpdate();
            try {
            	Object vertex = vertices.get(n.getId());	
            	System.out.println("removing vertex:" +vertex.toString());
                Object[] edges = graph.getEdges(vertex);
                graph.getModel().remove(vertex);
                for( Object edge: edges) {
                	graph.getModel().remove(edge);
                }
            } finally {
                graph.getModel().endUpdate();
            }
            graphComponent.setGraph(graph);
			//delete transitions
			removeTransitions(n);
	}
	
	
	/*
	 * If the transition does not already exist, it is added to the list and returns true
	 */
	public boolean addTransition(NavigationTransition n)
	{
		boolean add = true;
		
		if(navigationTransition == null) navigationTransition = new ArrayList<NavigationTransition>();
		for(int i=0; i<navigationTransition.size();i++)
		{
	
			if(navigationTransition.get(i).getFrom().equals(n.getFrom()) &&
				navigationTransition.get(i).getTo().equals(n.getTo()) &&
				navigationTransition.get(i).getProbability().equals(n.getProbability())){

				add = false;
				break;
			}
		
		}
		//Add the transition to the nodes.
		for(int i=0; i<initialNavigation.size();i++)
		{
			if(initialNavigation.get(i).getId().equals(n.getFrom())){initialNavigation.get(i).addDestination(n); }
			if(initialNavigation.get(i).getId().equals(n.getTo())){initialNavigation.get(i).addPredecessor(n);}
		}
		for(int i=0; i < nodes.size();i++)
		{
			if(nodes.get(i).getId().equals(n.getFrom())) nodes.get(i).addDestination(n);
			if(nodes.get(i).getId().equals(n.getTo())) nodes.get(i).addPredecessor(n);
		}
		
		if(add)
		{
			navigationTransition.add(n);
			
		}

		
	return add;
	}
	/*
	 * Searches for a specific transition and returns it. If the transition doesn't exists returns null
	 */
	public NavigationTransition getNavigation(String id)
	{
		NavigationTransition nav = null;
		for(int i = 0; i < navigationTransition.size(); i++)
		{
			if(navigationTransition.get(i).toString().equals(id))
			{
				nav = navigationTransition.get(i);
				break;
			}	
		}
	return nav;
	}
	
	/*
	 * Updates the probability to a specific transition and updates the edge label
	 */
	public void updateEdge(String from, String to, String prob, mxCell cell)
	{

		for(int i = 0; i < navigationTransition.size(); i++)
		{
			if(navigationTransition.get(i).toString().equals(cell.getId()))
			{
				navigationTransition.get(i).setProbability(prob);
				break;
			}	
		}
		
		graph.getModel().beginUpdate();
        try {

        	Object edge = edges.get(cell.getId());
        	if(edge != null)
        	{	
        		graph.getModel().remove(edge);
        		edges.remove(cell.getId());     		
        		addTransitionToGraph(new NavigationTransition(from,to, prob));
        	}

        	
        } finally {
            graph.getModel().endUpdate();
        }
        graphComponent.setGraph(graph);
		
	}
	
	/*
	 * Deletes a transition from the workload and the graph.
	 */
	
	public void deleteTransition(NavigationTransition nav)
	{
		for(int i = 0; i < navigationTransition.size(); i++)
		{
			if(navigationTransition.get(i).toString().equals(nav.toString()))
				navigationTransition.remove(i);
		}
		for(int i=0; i<initialNavigation.size();i++)
		{
			if(initialNavigation.get(i).getId().equals(nav.getFrom())){initialNavigation.get(i).removeDestination(nav.getFrom()); }
			if(initialNavigation.get(i).getId().equals(nav.getTo())){initialNavigation.get(i).removePredecessor(nav.getTo());}
		}
		for(int i=0; i < nodes.size();i++)
		{
			if(nodes.get(i).getId().equals(nav.getFrom())) nodes.get(i).removeDestination(nav.getFrom());
			if(nodes.get(i).getId().equals(nav.getTo())) nodes.get(i).removePredecessor(nav.getTo());
		}
		
		graph.getModel().beginUpdate();
        try {
        	Object from = vertices.get(nav.getFrom());
        	Object to = vertices.get(nav.getTo());
      
            Object[] edges = graph.getEdgesBetween(from, to);

            for( Object edge: edges) {
            	mxCell ed = (mxCell) edge;
            	if(ed.getId().equals(nav.toString()))
            		graph.getModel().remove(edge);
            }
        } finally {
            graph.getModel().endUpdate();
        }
        graphComponent.setGraph(graph);
		
	}

		

	
	/*
	 * Delete transitions that contains a specific node
	 */
	public void removeTransitions(Node n)
	{
		for(int i = 0; i < navigationTransition.size(); i++)
		{
			if(navigationTransition.get(i).getFrom().equals(n.getId()) ||
				navigationTransition.get(i).getTo().equals(n.getId()))
				navigationTransition.remove(i);
		}
	}

	/*
	 * Adds nodes to the graph from this workload and returns it.
	 */
	
	public mxGraph addNodeToGraph(Node n, boolean isIni)
	{
		Object v;
		graph.getModel().beginUpdate();
		try
		{	
			if(isIni) v = graph.insertVertex(graph.getDefaultParent(),n.getId(),n, 250, 100, width,height,"shape=ellipse;fillColor=yellow");
			else v = graph.insertVertex(graph.getDefaultParent(),n.getId(),n, 250, 100, width,height,"shape=ellipse");
			vertices.put(n.getId(),v);
		}
		finally
		{
			//end transaction with endUpdate
			graph.getModel().endUpdate();

		}
		graphComponent.setGraph(graph);
	return graph;
	}
	
	
	public mxGraph addTransitionToGraph(NavigationTransition nav)
	{
		graph.getModel().beginUpdate();
		try
		{
			Object ed = graph.insertEdge(vertices.get(nav.getFrom()),nav.toString(), 
					nav,vertices.get(nav.getFrom()),vertices.get(nav.getTo()));
			graph.cellLabelChanged(ed, nav.getProbability(), false);
			edges.put(nav.toString(), ed);

		}
		finally
		{
			//end transaction with endUpdate
			graph.getModel().endUpdate();
			
			graphComponent.setGraph(graph);

		}
	return graph;
	}
	/*
	 * Generates a graph from the workload specification.
	 */
	public mxGraphComponent generateGraph()
	{	
		List<Object> vertices = new ArrayList<Object>();
		Object v;
		int posX = 10;
		int posY = 10;


		graph.getModel().beginUpdate();
		try
		{
			//add initialNavigations
			for(int i=0;i<initialNavigation.size();i++)
			{
				v = graph.insertVertex(graph.getDefaultParent(),initialNavigation.get(i).getId(), initialNavigation.get(i), posX, posY+i*height, width,height);
				vertices.add(v);
			}
			//add nodes
			for(int i=0; i<nodes.size();i++)
			{
				v = graph.insertVertex(graph.getDefaultParent(),nodes.get(i).getId(), nodes.get(i), posX, posY+i*height, width,height);
				vertices.add(v);
			}
			
			//add edges
			for(int i=0;i<navigationTransition.size();i++)
			{
				//Properly connect the nodes "From" and "To"
				graph.insertEdge(graph.getDefaultParent(),navigationTransition.get(i).getFrom()+"-"+navigationTransition.get(i).getTo(), 
						navigationTransition.get(i),vertices.get(0),vertices.get(1));
			}
		}
		finally
		{
		//end transaction with endUpdate
		graph.getModel().endUpdate();
		}
		graphComponent.setGraph(graph);
		return graphComponent;
	}
	
	/*
	 * If the node already exists the function updates its value and returns true, otherwise returns false
	 */
	private boolean exists(Node n)
	{
		boolean exists = false;
		if(nodes == null) nodes = new ArrayList<Node>();

			for(int i=0; i<initialNavigation.size();i++)
			{
				System.out.println("searching as initial");
				if(initialNavigation.get(i).getId().equals(n.getId())){
					exists = true;
					break;
				}
			
			}
		if(!exists)
		{
			for(int i=0; i<nodes.size();i++)
			{
				System.out.println("searching as node");
				if(nodes.get(i).getId().equals(n.getId())){
					exists = true;
					break;
				}	
			}
		}
		return exists;
	}
	
	/*
	 * Checks if some of the parameters on this Workload are empty. Return true if there is an empty parameter.
	 */

	public boolean isEmpty()
	{
		if(id.equals("")) System.out.println("id is empty");
		if(usersNumber.equals("")) System.out.println("num is empty");
		if(serializerClass.equals("")) System.out.println("class is empty");
		
		if(id.equals("") || usersNumber.equals("") || serializerClass.equals("")){ System.out.println("I'm empty"); return true;}
		
		System.out.println("I'm not empty");
		return false;
	}
	

	
	/*
	 * Create a XML file from the workload information and stores it in the given path.
	 */
	public void createXML(String path)
	{
		try {
			Element workload = new Element("WorkloadTest");
			workload.setAttribute("id", getId());
			workload.setAttribute("serializerClass", "com.isoco.guernica.core.store.fs.serializer."+getSerializerClass());
			workload.detach();
			Document doc = new Document(workload);



			
			Element userNum = new Element("UsersNumber");
			userNum.setText(getUsersNumber());
			
			doc.getRootElement().addContent(userNum);
			
			Element navigationGraph = new Element("NavigationGraph");
			
			//Add all the initial navigations.
			Element initialNavigations = new Element("InitialNavigations");
			
			List<Node> initialNodes = getInitialNavigation();
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
			
			List<NavigationTransition> transitions = getNavigationTransition();
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
		  
		  int index;
		  try {
	 
			
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			System.out.println("ID: "+rootNode.getAttributeValue("id"));
			System.out.println("Serializer: "+rootNode.getAttributeValue("serializerClass"));
			System.out.println("Users Number: "+rootNode.getChildText("UsersNumber"));			
			
			id = rootNode.getAttributeValue("id");
			usersNumber = rootNode.getChildText("UsersNumber");
			index = rootNode.getAttributeValue("serializerClass").lastIndexOf(".");
			serializerClass = rootNode.getAttributeValue("serializerClass").substring(index+1);
			
			Element graph = rootNode.getChild("NavigationGraph");	
			
			Element iniNavs = graph.getChild("InitialNavigations");
			List<Element> iniNav = iniNavs.getChildren("InitialNavigation");
			
			Element navTrans = graph.getChild("NavigationTransitions");
			List<Element> navTran = navTrans.getChildren();
			
			System.out.println("Number of initial navigations: "+iniNav.size());
				
			for (int i = 0; i < iniNav.size(); i++) {
			   Element node = (Element) iniNav.get(i);
			   addNode(new Node(node.getAttributeValue("id"),node.getAttributeValue("probability")));
 
			}
			
			System.out.println("Number of navigation transitions: "+navTran.size());
			
			for(int i=0; i < navTran.size(); i++)
			{
				Element node = (Element) navTran.get(i);
				List<Node> nodes = getNodes();
				Node newNode = new Node(node.getAttributeValue("to"));
				if(!nodes.contains(newNode)) addNode(newNode);
					
				addTransition(new NavigationTransition(node.getAttributeValue("from"),node.getAttributeValue("to"),node.getAttributeValue("probability")));
			
			}
	 
			printGraph();
			
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }
		}
	
	/*
	 * Testing class, prints on console the graph.
	 */
	public void printGraph()
	{
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println("	Initial Nodes	  		   ");
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		
		for(int i = 0; i< initialNavigation.size(); i++)
		{
			System.out.println(initialNavigation.get(i).toString());
		}
		
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println("	 Nodes					   ");
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		
		for(int i = 0; i< nodes.size(); i++)
		{
			System.out.println(nodes.get(i).toString());
		}
		
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println("	Transitions				   ");
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		
		for(int i = 0; i< navigationTransition.size(); i++)
		{
			System.out.println( "From: "+navigationTransition.get(i).getFrom()+ "\t to: "+navigationTransition.get(i).getTo()+ "\t prob: "+navigationTransition.get(i).getProbability());
		}
		
	}
	
	public void createJGraphXML(String path)
	{
		try {
			Element graph = new Element("Graph");
			graph.detach();
			Document doc = new Document(graph);
			Enumeration<String> vKeys = vertices.keys();
			while(vKeys.hasMoreElements())
			{
				String key = vKeys.nextElement();
				Object vertex = vertices.get(key);
				mxCell v = (mxCell) vertex;
				Element vert = new Element("Vertex");
				vert.setAttribute("partent", v.getParent().getId());
				vert.setAttribute("id", v.getId());
				vert.setAttribute("pos_x",  Double.toString(v.getGeometry().getX()));
				vert.setAttribute("pos_y", Double.toString(v.getGeometry().getY()));
				vert.setAttribute("width", Double.toString(v.getGeometry().getWidth()));
				vert.setAttribute("height", Double.toString(v.getGeometry().getHeight()));
				vert.setAttribute("style", v.getStyle());
				graph.addContent(vert);
			}
			
			doc.getRootElement().addContent(navigationGraph);
	 
			
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
}
