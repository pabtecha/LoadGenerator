/*
 * @author: Nicolás Terevinto
 * @version: 1.0
 */

package bussines;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
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
	private double width = 50;
	private double height = 50;
	private Hashtable<String,Object> vertices;
	private Hashtable<String,Object> edges;
	

	/*
	 * WorkloadTest constructor
	 * @param id 				workload identifier.
	 * @param usersNumber		string with the number of users.
	 * @param serializerClass	string with the serializer.
	 */
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
	/*
	 * Returns the workload id.
	 * @return id
	 */
	public String getId(){return id;}
	/*
	 * Returns the number of users.
	 * @return usersNumber
	 */
	public String getUsersNumber(){return usersNumber;}
	/*
	 * Returns the serializer.
	 * @return serializerClass
	 */
	public String getSerializerClass(){return serializerClass;}
	/*
	 * Returns the list of initial nodes.
	 * @return initialNavigation
	 */
	public List<Node> getInitialNavigation(){return initialNavigation;}
	/*
	 * Returns the list of transitions
	 * @return navigationTransition
	 */
	public List<NavigationTransition> getNavigationTransition(){return navigationTransition;}
	/*
	 * Returns the list of non-initial nodes.
	 * @return nodes
	 */
	public List<Node> getNodes(){return nodes;}
	
	/*
	 * Searches the non-initial node with the given id and returns it. Returns null if the node doesn't exists
	 * @return Node
	 */
	public Node getNodeById(String id)
	{
			
		for(int i=0; i<nodes.size(); i++)
		{
			if(nodes.get(i).getId().equals(id)) return nodes.get(i);
		}
			
		return null;
	}
	
	/*
	 * Searches the initial node with the given id and returns it. Returns null if the node doesn't exists
	 * @return Node
	 */
	public Node getInitialNodeById(String id)
	{
		for(int i=0; i<initialNavigation.size(); i++)
		{
			if(initialNavigation.get(i).getId().equals(id)) return initialNavigation.get(i);
		}
		
		return null;
	}
	
	/*
	 * Searches a node by Id looking without considering whether it is an initial node or not. Returns null if the node doesn't exists
	 * @return Node
	 */
	public Node getVertexById(String id)
	{
		Node n;
		n= getNodeById(id);
		if(n==null) n= getInitialNodeById(id);
		
	return n;
	}
	
	
	//sets
	
	/*
	 * Sets the workload id.
	 */
	public void setId(String id){this.id=id;}
	/*
	 * Sets the number of users
	 */
	public void setUsersNumber(String num){usersNumber=num;}
	/*
	 * Set the serializer.
	 */
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
	 * If the node does not already exist, it is added to the list and returns true.
	 * @return ex
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

		printGraph();
		printJGraph();
	return !ex;
	}
	/*
	 * Updates a node, setting its new values on both the workload and the graph representation.
	 * @return boolean
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
		{
			sameNode = true;
			System.out.println("The node keeps the same id");
		}

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
		
		if(exists && !sameNode)
		{
			System.out.println("The new id already exists");
			//node already exists
			return false;
		}
			

		if(wasInitial)
		{
			if(n.isInitial())
			{
				System.out.println("The old node was at: "+ pos+" with id: "+initialNavigation.get(pos).getId()+ " and the new id is: "+ n.getId());
				initialNavigation.get(pos).setId(n.getId());
				initialNavigation.get(pos).setProbability(n.getProbability());
			}else
			{   //remove the old node, update it and add to nodes
				System.out.println("The old node was at: "+ pos+" with id: "+initialNavigation.get(pos).getId()+ " and the new id is: "+ n.getId());
				initialNavigation.remove(pos);
				old.setId(n.getId());
				old.setInitial(false);
				nodes.add(old);
			}

				
				
		}else
		{
			if(n.isInitial())
			{	System.out.println("The old node was at: "+ pos+" with id: "+nodes.get(pos).getId()+ " and the new id is: "+ n.getId());
				nodes.remove(pos);
				old.setId(n.getId());
				old.setInitial(true);
				old.setProbability(n.getProbability());
				initialNavigation.add(old);
			}else
			{
				System.out.println("The old node was at: "+ pos+" with id: "+nodes.get(pos).getId()+ " and the new id is: "+ n.getId());
				nodes.get(pos).setId(n.getId());
			}
				
		}
		
		
		mxCell vertex = (mxCell) cell;	
		vertex = (mxCell) vertices.get(vertex.getId());
		vertices.remove(vertex.getId());
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
			vertices.put(vertex.getId(), vertex);
			graph.cellLabelChanged(vertex, n.getId(), false);	
		}
		
		if(n.isInitial())
		{
			vertex.setStyle("shape=ellipse;fillColor=yellow");
		}else
		{
			vertex.setStyle("shape=ellipse");
		}

	

		printGraph();
		printJGraph();
	
	return true;
	}
	
	/*
	 * Deletes the node from the graph and all the transitions going from and to the node.
	 */	
	public void deleteNode(Node n)
	{
		System.out.println("Received node "+n.getId()+" for deletion");
		if(n.getProbability()!=null) System.out.println(n.getProbability());
		if(n.isInitial())
		{
			initialNavigation.remove(n);
		}else
		{
			nodes.remove(n);
			System.out.println("node removed");
			
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
			
			printGraph();
			printJGraph();
	}
	
	
	/*
	 * Adds the given transition to the workload only if the transition doesn't exist and both nodes exist.
	 * @return 0 if node is added
	 * @return 1 if at least one of the nodes doesn't exist
	 * @return 2 if the transition already exists
	 */
	public int addTransition(NavigationTransition n)
	{
		int add = 0;
		
		if(navigationTransition == null) navigationTransition = new ArrayList<NavigationTransition>();
		for(int i=0; i<navigationTransition.size();i++)
		{
	
			if(navigationTransition.get(i).getFrom().equals(n.getFrom()) &&
				navigationTransition.get(i).getTo().equals(n.getTo()) &&
				navigationTransition.get(i).getProbability().equals(n.getProbability())){

				add = 2;
				break;
			}
		
		}
		//Add the transition to the nodes		
		if(add==0)
		{
			Node from = getVertexById(n.getFrom());
			Node to = getVertexById(n.getTo());
			if(from != null && to != null)
			{
				navigationTransition.add(n);
			}
			else
			{
				add = 1;
			}

			
		}

		printGraph();
		printJGraph();
	return add;
	}
	
	
	/*
	 * Searches for a specific transition and returns it. If the transition doesn't exists returns null
	 * @return navigation
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
	 * Updates the probability to a specific transition and updates the edge label.
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
        printGraph();
        printJGraph();
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
        printGraph();
        printJGraph();
	}	
	/*
	 * Delete transitions that contains a specific node
	 */
	public void removeTransitions(Node n)
	{
		Iterator<NavigationTransition> i = navigationTransition.iterator();
		while (i.hasNext()) 
		{
			   NavigationTransition nav = i.next();
			   if(nav.getFrom().equals(n.getId()) ||
				nav.getTo().equals(n.getId()))
				   i.remove();
			   
		}
	}

	/*
	 * Adds nodes to the graph from this workload and returns the updated graph.
	 * @return mxGraph
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
		printJGraph();
	return graph;
	}
	
	/*
	 * Adds a transition to the graph and returns the updated graph.
	 * @return mxGraph
	 */
	public mxGraph addTransitionToGraph(NavigationTransition nav)
	{
		System.out.println("transition from: "+nav.getFrom()+" to: "+nav.getTo()+" added to graph");
		System.out.println("the cell is:" +vertices.get(nav.getFrom()).toString());
		graph.getModel().beginUpdate();
		try
		{
			Object ed = graph.insertEdge(vertices.get(nav.getFrom()),nav.toString(), 
					nav,vertices.get(nav.getFrom()),vertices.get(nav.getTo()));
			graph.cellLabelChanged(ed, nav.getProbability(), false);
			//graph.getModel().getGeometry(ed).setOffset(new mxPoint(10.0,50.0));
			edges.put(nav.toString(), ed);

		}
		finally
		{
			//end transaction with endUpdate
			graph.getModel().endUpdate();
			
			graphComponent.setGraph(graph);

		}
		printJGraph();
	return graph;
	}
	/*
	 * Generates a graph from the workload specification.
	 * @return mxGraph
	 */
	public mxGraph generateGraph()
	{	
		Object v, ed;
		double posX = 25;
		double posY = 25;
		double space = 100;


		graph.getModel().beginUpdate();
		try
		{
			//add initialNavigations
			for(int i=0;i<initialNavigation.size();i++)
			{
				v = graph.insertVertex(graph.getDefaultParent(),initialNavigation.get(i).getId(), initialNavigation.get(i), posX, posY+i*(height+20), width,height,"shape=ellipse;fillColor=yellow" );
				vertices.put(initialNavigation.get(i).getId(), v);
			}
			//add nodes
			posX += space;
			for(int i=0; i<nodes.size();i++)
			{	
				if( i%5 == 0) posX += space;
				
				v = graph.insertVertex(graph.getDefaultParent(),nodes.get(i).getId(), nodes.get(i), posX, posY+i*(height+20), width,height,"shape=ellipse");
				vertices.put(nodes.get(i).getId(), v);
			}
			
			//add edges
			for(int i=0;i<navigationTransition.size();i++)
			{
				//Properly connect the nodes "From" and "To"
				ed = graph.insertEdge(graph.getDefaultParent(),navigationTransition.get(i).toString(), 
						navigationTransition.get(i),vertices.get(navigationTransition.get(i).getFrom()),
						vertices.get(navigationTransition.get(i).getTo()));
				graph.cellLabelChanged(ed, navigationTransition.get(i).getProbability(), false);
		//		graph.getModel().getGeometry(ed).setOffset(new mxPoint(10.0,50.0));
				edges.put(navigationTransition.get(i).toString(), ed);
			}
		}
		finally
		{
		//end transaction with endUpdate
		graph.getModel().endUpdate();
		}

		return graph;
	}
	
	/*
	 * If the node already exists the function returns true, otherwise returns false.
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
	 * Testing class, prints on console the workload.
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
	
	/*
	 * Testing class, prints on console the graph.
	 */
	public void printJGraph()
	{
		Object[] cells = graph.getChildCells(graph.getDefaultParent());
		
		for(Object c : cells)
		{
			mxCell cell = (mxCell) c;
			System.out.println("Cell id is:" +cell.getId());
		}
		
	}
	
	/*
	 * Create a XML file from the JGraph information and stores it in the given path.
	 */
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
				doc.getRootElement().addContent(vert);
			}
			
			Enumeration<String> eKeys = edges.keys();
			while(eKeys.hasMoreElements())
			{
				String key = eKeys.nextElement();
				Object edge = edges.get(key);
				mxCell e = (mxCell) edge;
				Element ed = new Element("Edge");
				ed.setAttribute("partent", e.getParent().getId());
				ed.setAttribute("id", e.getId());
				ed.setAttribute("source", e.getSource().getId());
				ed.setAttribute("target",e.getTarget().getId());
				doc.getRootElement().addContent(ed);
				System.out.println(e.getId());
			}
			
		
			
			// new XMLOutputter().output(doc, System.out);
			XMLOutputter xmlOutput = new XMLOutputter();
	 
			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter(path));
	 
			System.out.println("File Graph Saved!");
	  } catch (IOException io) {
		System.out.println(io.getMessage());
	  }
	
	
	}
	/*
	 * Read the XML file given and parses it to generate a JGraph.
	 */
	public mxGraph readGraphXML(String path)
	{
		  SAXBuilder builder = new SAXBuilder();
		  File xmlFile = new File(path);
		  
		  try {
	 
			
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			List<Element> verts = rootNode.getChildren("Vertex");
			List<Element> eds = rootNode.getChildren("Edge");
			
			System.out.println("Number of vertices: "+verts.size());
			
			Object v, ed;
			double pos_x;
			double pos_y;
			
			graph.getModel().beginUpdate();
			try
			{	
				for (int i = 0; i < verts.size(); i++)
				{
				   Element node = (Element) verts.get(i);
				   Node n = getVertexById(node.getAttributeValue("id"));
					
	
				   pos_x = Double.parseDouble(node.getAttributeValue("pos_x"));
				   pos_y = Double.parseDouble(node.getAttributeValue("pos_y"));
				   width = Double.parseDouble(node.getAttributeValue("width"));
				   height = Double.parseDouble(node.getAttributeValue("height"));
					
				   v = graph.insertVertex(node.getAttributeValue("parent"),n.getId(),n, pos_x, pos_y, width,height,node.getAttributeValue("style"));
	
				   vertices.put(n.getId(), v);
				}
				System.out.println("Number of edges: "+eds.size());
				
				for(int i =0; i<eds.size();i++)
				{
					Element edge = (Element) eds.get(i);
					System.out.println("getting nav: "+edge.getAttributeValue("id"));
					NavigationTransition trans = getNavigation(edge.getAttributeValue("id"));
					System.out.println("got nav: "+ trans.toString());
					
					ed = graph.insertEdge(edge.getAttributeValue("parent"), edge.getAttributeValue("id"), trans, vertices.get(edge.getAttributeValue("source")),
							vertices.get(edge.getAttributeValue("target")));
					System.out.println("new label: "+trans.getProbability());
					graph.cellLabelChanged(ed,trans.getProbability() , false);
				//	graph.getModel().getGeometry(ed).setOffset(new mxPoint(10.0,50.0));
					edges.put(edge.getAttributeValue("id"), ed);
				}

 
			}
			
			finally
			{
				//end transaction with endUpdate
				graph.getModel().endUpdate();

			}

			printJGraph();
			
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }
		  
		  return graph;
		}
	
}
