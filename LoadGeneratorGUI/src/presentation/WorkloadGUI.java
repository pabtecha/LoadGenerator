package presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import bussines.NavigationTransition;
import bussines.Node;
import bussines.WorkloadTest;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;


public class WorkloadGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1757537006758067046L;
	private JPanel contentPane;
	private JTextField txtFrom;
	private JTextField txtTo;
	private JTextField txtTProb;
	private JTextField txtNProb;
	private JTextField txtNID;
	private JSplitPane splitPane;
	private JPanel panelWorkLoad; 
	private JCheckBox chckbxIsInitial;
	private JButton btnAddTransition;
	private JButton btnAddNode;
	private JPanel panelTransition;
	private JPanel panelNode;
	private JComboBox<String> cbSerializer;
	private WorkloadTest wl;
	private mxGraph graph;
	private mxGraphComponent graphComponent;
	private String[] serializers = {"","XMLBaseWorkloadGeneratorSerializer","XMLBaseWorkloadPlannerConfigurationSerializer","XMLBaseWorkloadTestStatisticSerializer",
			"XMLGraphNavigationWorkloadTestSerializer","XMLOne2OneNavigationWorkloadTestSerializer","XMLSerializer", "XMLWSWorkloadGeneratorSerializer","XMLWSWorkloadPlannerSerializer"};
	private boolean isInitial = false;
	private JTextField txtUsersNumber;
	private JPanel graphPanel;
	private JTextField txtWLId;
	private JLabel lblWorkloadId;
	private JLabel lblNumberOfUsers;
	private JLabel lblSerializer;
	private Component rigidArea;
	private JButton btnEditNode;
	private JButton btnEditTransition;
	private JMenu mnNavigation;
	private JMenuItem mntmNewNav;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WorkloadGUI frame = new WorkloadGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WorkloadGUI() {
		
		if(wl==null) wl = new WorkloadTest("","","");
		graph = new mxGraph();
		graphComponent = new mxGraphComponent(graph);
		graphComponent.setConnectable(false);
		graphComponent.setDragEnabled(false);

		graphComponent.getGraphControl().addMouseListener(new MouseAdapter()
		{		
	
				public void mousePressed(final MouseEvent e) {
					// If Right Mouse Button
					if (SwingUtilities.isRightMouseButton(e)) {
						// Find Cell in Model Coordinates
						final mxCell cell = (mxCell)graphComponent.getCellAt(e.getX(), e.getY());
						System.out.println("Mouse pressed at cell:" +cell.getId());
						// Create PopupMenu for the Cell
						JPopupMenu menu =  new JPopupMenu();
						if(cell != null && cell.isVertex())
						{ 
						 ActionListener actionSource = new ActionListener() {
						      public void actionPerformed(ActionEvent e) {
						    	  System.out.println(cell.getId());
						    	 txtFrom.setText(wl.getVertexById(cell.getId()).getId());
						      }
						    };
						 ActionListener actionDest = new ActionListener() {
							  public void actionPerformed(ActionEvent e) {
								  txtTo.setText(wl.getVertexById(cell.getId()).getId());
							   }
						 };
						 ActionListener actionDelete = new ActionListener() {
							  public void actionPerformed(ActionEvent e) {
								  wl.deleteNode(wl.getVertexById(cell.getId()));
							   }
						 };
						 ActionListener actionEdit = new ActionListener() {
							  public void actionPerformed(ActionEvent e) {				  
								  Node n = wl.getVertexById(cell.getId());							  
								  txtNID.setText(n.getId());
								  if(n.isInitial()){
									  chckbxIsInitial.setSelected(true);
									  isInitial = true;
									  txtNProb.setText(n.getProbability());
								  }else
								  {
									  txtNProb.setText("");
									  chckbxIsInitial.setSelected(false);
									  isInitial = false;
								  }
								  
							   }
						 };
						    JMenuItem m = new JMenuItem("Edit");
						    m.addActionListener(actionEdit);
						    menu.add(m);
						    m = new JMenuItem("Delete");
						    m.addActionListener(actionDelete);
						    menu.add(m);
						    m = new JMenuItem("Set as source");
						    m.addActionListener(actionSource);
						    menu.add(m);
						    m = new JMenuItem("Set as destination");	   
						    m.addActionListener(actionDest);
						    menu.add(m);
	
						}
						
						if(cell != null && cell.isEdge())
						{
							//the split result is nav[0]=from; nav[1]=to; nav[2]=prob;
							final String[] nav = cell.getId().split("-");
							ActionListener actionEdit = new ActionListener() {
							      public void actionPerformed(ActionEvent e) {
							    	 System.out.println("Editing transition");
							    	 txtFrom.setText(nav[0]);
							    	 txtTo.setText(nav[1]);
							    	 txtTProb.setText(nav[2]);

							      }
							 };
							 ActionListener actionDelete = new ActionListener() {
								  public void actionPerformed(ActionEvent e) {
									 System.out.println("deleting transition");
									 System.out.println(cell.getId());
									
									 if(wl.getNavigation(cell.getId()) != null)
										 wl.deleteTransition(wl.getNavigation(cell.getId()));
								   }
							 };
							 JMenuItem m = new JMenuItem("Set probability");
							 m.addActionListener(actionEdit);
							 menu.add(m);
							 m = new JMenuItem("Delete");
							 m.addActionListener(actionDelete);
							 menu.add(m);
						}
						// Display PopupMenu
						menu.show(contentPane, e.getX(), e.getY());
						
						
	
					}
	
				}		
		});
	
	
		
		setMinimumSize(new Dimension(600, 500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnArchivo = new JMenu("File");
		menuBar.add(mnArchivo);
		
		JMenuItem mntmNuevo = new JMenuItem("New");
		mntmNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newWorkloadFile(e);
			}
		});
		mnArchivo.add(mntmNuevo);
		
		JMenuItem mntmAbrir = new JMenuItem("Open");
		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadWorkload();
			}
		});
		mnArchivo.add(mntmAbrir);
		
		JMenuItem mntmGuardar = new JMenuItem("Save");
		mntmGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveWorkload();
			}
		});
		mnArchivo.add(mntmGuardar);
		
		JSeparator separator = new JSeparator();
		mnArchivo.add(separator);
		
		JMenuItem mntmSalir = new JMenuItem("Exit");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnArchivo.add(mntmSalir);
		
		mnNavigation = new JMenu("Navigation");
		menuBar.add(mnNavigation);
		
		mntmNewNav = new JMenuItem("New");
		mntmNewNav.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NavigationGUI navGUI = new NavigationGUI();
				navGUI.setVisible(true);
			}
		});
		mnNavigation.add(mntmNewNav);
		contentPane = new JPanel();
		contentPane.setMinimumSize(new Dimension(600, 500));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		splitPane = new JSplitPane();
		splitPane.setMinimumSize(new Dimension(600, 500));
		splitPane.setResizeWeight(0.9);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane);
		splitPane.add(graphComponent,JSplitPane.TOP);

		
		
		panelWorkLoad = new JPanel();
		panelWorkLoad.setMinimumSize(new Dimension(250, 200));
		splitPane.setRightComponent(panelWorkLoad);
		GridBagLayout gbl_panelWorkLoad = new GridBagLayout();
		gbl_panelWorkLoad.columnWidths = new int[]{10, 522, 0};
		gbl_panelWorkLoad.rowHeights = new int[]{19, 20, 0, 0, 0};
		gbl_panelWorkLoad.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panelWorkLoad.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelWorkLoad.setLayout(gbl_panelWorkLoad);
		
		lblWorkloadId = new JLabel("Workload ID:");
		GridBagConstraints gbc_lblWorkloadId = new GridBagConstraints();
		gbc_lblWorkloadId.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkloadId.anchor = GridBagConstraints.WEST;
		gbc_lblWorkloadId.gridx = 0;
		gbc_lblWorkloadId.gridy = 0;
		panelWorkLoad.add(lblWorkloadId, gbc_lblWorkloadId);
		
		txtWLId = new JTextField();
		GridBagConstraints gbc_txtWLId = new GridBagConstraints();
		gbc_txtWLId.insets = new Insets(0, 0, 5, 0);
		gbc_txtWLId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWLId.gridx = 1;
		gbc_txtWLId.gridy = 0;
		panelWorkLoad.add(txtWLId, gbc_txtWLId);
		txtWLId.setColumns(10);
		
		lblNumberOfUsers = new JLabel("Number of users:");
		GridBagConstraints gbc_lblNumberOfUsers = new GridBagConstraints();
		gbc_lblNumberOfUsers.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfUsers.anchor = GridBagConstraints.WEST;
		gbc_lblNumberOfUsers.gridx = 0;
		gbc_lblNumberOfUsers.gridy = 1;
		panelWorkLoad.add(lblNumberOfUsers, gbc_lblNumberOfUsers);
		
		txtUsersNumber = new JTextField();
		txtUsersNumber.setMinimumSize(new Dimension(40, 19));
		txtUsersNumber.setPreferredSize(new Dimension(40, 19));
		GridBagConstraints gbc_txtUsersNumber = new GridBagConstraints();
		gbc_txtUsersNumber.anchor = GridBagConstraints.WEST;
		gbc_txtUsersNumber.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsersNumber.gridx = 1;
		gbc_txtUsersNumber.gridy = 1;
		panelWorkLoad.add(txtUsersNumber, gbc_txtUsersNumber);
		txtUsersNumber.setColumns(10);
	    
	    lblSerializer = new JLabel("Serializer");
	    GridBagConstraints gbc_lblSerializer = new GridBagConstraints();
	    gbc_lblSerializer.insets = new Insets(0, 0, 5, 5);
	    gbc_lblSerializer.anchor = GridBagConstraints.WEST;
	    gbc_lblSerializer.gridx = 0;
	    gbc_lblSerializer.gridy = 2;
	    panelWorkLoad.add(lblSerializer, gbc_lblSerializer);
		
		
	    cbSerializer = new JComboBox<String>(serializers);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 2;
		panelWorkLoad.add(cbSerializer, gbc_comboBox);
		
		graphPanel = new JPanel();
		GridBagConstraints gbc_graphPanel = new GridBagConstraints();
		gbc_graphPanel.anchor = GridBagConstraints.WEST;
		gbc_graphPanel.gridwidth = 2;
		gbc_graphPanel.fill = GridBagConstraints.VERTICAL;
		gbc_graphPanel.gridx = 0;
		gbc_graphPanel.gridy = 3;
		panelWorkLoad.add(graphPanel, gbc_graphPanel);
		GridBagLayout gbl_graphPanel = new GridBagLayout();
		gbl_graphPanel.columnWidths = new int[]{52, 60, 72, 0};
		gbl_graphPanel.rowHeights = new int[]{29, 0, 0};
		gbl_graphPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_graphPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		graphPanel.setLayout(gbl_graphPanel);
		
		JLabel lblNode = new JLabel("Node");
		GridBagConstraints gbc_lblNode = new GridBagConstraints();
		gbc_lblNode.anchor = GridBagConstraints.WEST;
		gbc_lblNode.insets = new Insets(0, 0, 5, 5);
		gbc_lblNode.gridx = 0;
		gbc_lblNode.gridy = 0;
		graphPanel.add(lblNode, gbc_lblNode);
		
		JLabel lblTransition = new JLabel("Transition");
		GridBagConstraints gbc_lblTransition = new GridBagConstraints();
		gbc_lblTransition.anchor = GridBagConstraints.WEST;
		gbc_lblTransition.insets = new Insets(0, 0, 5, 0);
		gbc_lblTransition.gridx = 2;
		gbc_lblTransition.gridy = 0;
		graphPanel.add(lblTransition, gbc_lblTransition);
		
		panelNode = new JPanel();
		GridBagConstraints gbc_panelNode = new GridBagConstraints();
		gbc_panelNode.fill = GridBagConstraints.VERTICAL;
		gbc_panelNode.anchor = GridBagConstraints.WEST;
		gbc_panelNode.insets = new Insets(0, 0, 0, 5);
		gbc_panelNode.gridx = 0;
		gbc_panelNode.gridy = 1;
		graphPanel.add(panelNode, gbc_panelNode);
		panelNode.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		GridBagLayout gbl_panelNode = new GridBagLayout();
		gbl_panelNode.columnWidths = new int[]{0, 86, 82, 22, 0};
		gbl_panelNode.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelNode.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelNode.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelNode.setLayout(gbl_panelNode);
		
		JLabel lblId = new JLabel("ID:");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.gridx = 1;
		gbc_lblId.gridy = 1;
		panelNode.add(lblId, gbc_lblId);
		
		txtNID = new JTextField();
		GridBagConstraints gbc_txtNID = new GridBagConstraints();
		gbc_txtNID.insets = new Insets(0, 0, 5, 5);
		gbc_txtNID.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNID.gridx = 2;
		gbc_txtNID.gridy = 1;
		panelNode.add(txtNID, gbc_txtNID);
		txtNID.setColumns(10);
		
		chckbxIsInitial = new JCheckBox("is Initial");
		chckbxIsInitial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtNProb.setText("");
				txtNProb.setEnabled(!isInitial);
				isInitial = !isInitial;
			}
		});
		GridBagConstraints gbc_chckbxIsInitial = new GridBagConstraints();
		gbc_chckbxIsInitial.anchor = GridBagConstraints.EAST;
		gbc_chckbxIsInitial.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxIsInitial.gridx = 1;
		gbc_chckbxIsInitial.gridy = 2;
		panelNode.add(chckbxIsInitial, gbc_chckbxIsInitial);
		
		JLabel lblProbability = new JLabel("Probability:");
		GridBagConstraints gbc_lblProbability = new GridBagConstraints();
		gbc_lblProbability.anchor = GridBagConstraints.EAST;
		gbc_lblProbability.insets = new Insets(0, 0, 5, 5);
		gbc_lblProbability.gridx = 1;
		gbc_lblProbability.gridy = 3;
		panelNode.add(lblProbability, gbc_lblProbability);
		
		txtNProb = new JTextField();
		txtNProb.setEnabled(false);
		GridBagConstraints gbc_txtNProb = new GridBagConstraints();
		gbc_txtNProb.insets = new Insets(0, 0, 5, 5);
		gbc_txtNProb.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNProb.gridx = 2;
		gbc_txtNProb.gridy = 3;
		panelNode.add(txtNProb, gbc_txtNProb);
		txtNProb.setColumns(10);
		
		btnAddNode = new JButton("Add Node");
		btnAddNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNode();
			}
		});
		
		btnEditNode = new JButton("Edit");
		btnEditNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editNode();
			}
		});
		GridBagConstraints gbc_btnEditNode = new GridBagConstraints();
		gbc_btnEditNode.insets = new Insets(0, 0, 5, 5);
		gbc_btnEditNode.gridx = 1;
		gbc_btnEditNode.gridy = 4;
		panelNode.add(btnEditNode, gbc_btnEditNode);
		
		GridBagConstraints gbc_btnAddNode = new GridBagConstraints();
		gbc_btnAddNode.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddNode.gridx = 2;
		gbc_btnAddNode.gridy = 4;
		panelNode.add(btnAddNode, gbc_btnAddNode);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea = new GridBagConstraints();
		gbc_rigidArea.fill = GridBagConstraints.HORIZONTAL;
		gbc_rigidArea.insets = new Insets(0, 0, 0, 5);
		gbc_rigidArea.gridx = 1;
		gbc_rigidArea.gridy = 1;
		graphPanel.add(rigidArea, gbc_rigidArea);
		
		panelTransition = new JPanel();
		GridBagConstraints gbc_panelTransition = new GridBagConstraints();
		gbc_panelTransition.fill = GridBagConstraints.VERTICAL;
		gbc_panelTransition.anchor = GridBagConstraints.WEST;
		gbc_panelTransition.gridx = 2;
		gbc_panelTransition.gridy = 1;
		graphPanel.add(panelTransition, gbc_panelTransition);
		panelTransition.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		GridBagLayout gbl_panelTransition = new GridBagLayout();
		gbl_panelTransition.columnWidths = new int[]{0, 86, 82, 22, 0};
		gbl_panelTransition.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panelTransition.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelTransition.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelTransition.setLayout(gbl_panelTransition);
		
		JLabel lblFrom = new JLabel("From:");
		GridBagConstraints gbc_lblFrom = new GridBagConstraints();
		gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
		gbc_lblFrom.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblFrom.gridx = 1;
		gbc_lblFrom.gridy = 1;
		panelTransition.add(lblFrom, gbc_lblFrom);
		
		txtFrom = new JTextField();
		GridBagConstraints gbc_txtFrom = new GridBagConstraints();
		gbc_txtFrom.insets = new Insets(0, 0, 5, 5);
		gbc_txtFrom.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFrom.gridx = 2;
		gbc_txtFrom.gridy = 1;
		panelTransition.add(txtFrom, gbc_txtFrom);
		txtFrom.setColumns(10);
		
		JLabel lblTo = new JLabel("To:");
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo.gridx = 1;
		gbc_lblTo.gridy = 2;
		panelTransition.add(lblTo, gbc_lblTo);
		
		txtTo = new JTextField();
		GridBagConstraints gbc_txtTo = new GridBagConstraints();
		gbc_txtTo.insets = new Insets(0, 0, 5, 5);
		gbc_txtTo.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTo.gridx = 2;
		gbc_txtTo.gridy = 2;
		panelTransition.add(txtTo, gbc_txtTo);
		txtTo.setColumns(10);
		
		JLabel lblProbability_1 = new JLabel("Probability:");
		GridBagConstraints gbc_lblProbability_1 = new GridBagConstraints();
		gbc_lblProbability_1.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblProbability_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblProbability_1.gridx = 1;
		gbc_lblProbability_1.gridy = 3;
		panelTransition.add(lblProbability_1, gbc_lblProbability_1);
		
		txtTProb = new JTextField();
		GridBagConstraints gbc_txtTProb = new GridBagConstraints();
		gbc_txtTProb.insets = new Insets(0, 0, 5, 5);
		gbc_txtTProb.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTProb.gridx = 2;
		gbc_txtTProb.gridy = 3;
		panelTransition.add(txtTProb, gbc_txtTProb);
		txtTProb.setColumns(10);
		
		btnAddTransition = new JButton("Add Transition");
		btnAddTransition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addTransition();
			}
		});
		
		btnEditTransition = new JButton("Edit");
		btnEditTransition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editTransition();				
			}
		});
		GridBagConstraints gbc_btnEditTransition = new GridBagConstraints();
		gbc_btnEditTransition.insets = new Insets(0, 0, 5, 5);
		gbc_btnEditTransition.gridx = 1;
		gbc_btnEditTransition.gridy = 4;
		panelTransition.add(btnEditTransition, gbc_btnEditTransition);
		GridBagConstraints gbc_btnAddTransition = new GridBagConstraints();
		gbc_btnAddTransition.anchor = GridBagConstraints.NORTH;
		gbc_btnAddTransition.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddTransition.gridx = 2;
		gbc_btnAddTransition.gridy = 4;
		panelTransition.add(btnAddTransition, gbc_btnAddTransition);
	}
	
	public void newWorkloadFile(ActionEvent e)
	{
		boolean saved = true;
		int reply = JOptionPane.showConfirmDialog(null, "Would you like to save first?", "Create new workload.", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
        	saved =	saveWorkload();
        }
        if(reply != JOptionPane.CLOSED_OPTION && saved)
        {
    		wl = new WorkloadTest("","","");
    		txtWLId.setText(null);
    		txtUsersNumber.setText(null);
    		cbSerializer.setSelectedIndex(0);
    		txtFrom.setText(null);
    		txtNID.setText(null);
    		txtNProb.setText(null);
    		txtTo.setText(null);
    		txtTProb.setText(null);
    		chckbxIsInitial.setSelected(false);
    		graph = new mxGraph();
    		graphComponent.setGraph(graph);
    		graphComponent.refresh();
       
        }
	}

	
	public void addNode()
	{	
		boolean added;
		Node n;
		
		if(chckbxIsInitial.isSelected())
		{
			if(txtNID.getText().equals("") || txtNProb.getText().equals(""))
			{
				JOptionPane.showMessageDialog(this,"Some parameter on node is empty","ERROR",JOptionPane.ERROR_MESSAGE);
				return;
			}
			else
			{	n = new Node(txtNID.getText(), txtNProb.getText());
				added = wl.addNode(n);
				if(added) graphComponent.setGraph( wl.addNodeToGraph(n, true));
			}
		}
		else 
		{
			if(txtNID.getText().equals(""))
			{
				JOptionPane.showMessageDialog(this,"Node ID is empty","ERROR",JOptionPane.ERROR_MESSAGE);
				return;
			}
			else
			{	n = new Node(txtNID.getText());
	
				added =	wl.addNode(n);
				if(added) graphComponent.setGraph(wl.addNodeToGraph(n, false));
			}
		}
		splitPane.add(graphComponent,JSplitPane.TOP);
		if(!added)
		{ 
			JOptionPane.showMessageDialog(this,"This node already exists","ERROR",JOptionPane.ERROR_MESSAGE);
	
		}	
	}
	
	public void editNode()
	{
		boolean updated;
		
		Object cell =	graphComponent.getGraph().getSelectionCell();
		if(cell == null)
		{
			JOptionPane.showMessageDialog(this,"Please, select a node","ERROR",JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			if(chckbxIsInitial.isSelected())
				updated = wl.updateNode(new Node(txtNID.getText(),txtNProb.getText()), cell);
			else
				updated = wl.updateNode(new Node(txtNID.getText()),cell);
				
			if(!updated)
				JOptionPane.showMessageDialog(this,"This ID already exists","ERROR",JOptionPane.ERROR_MESSAGE);
			graphComponent.refresh();
		}

			
	}
	public void addTransition()
	{
		int added;
		if(txtFrom.getText().equals("") || txtTo.getText().equals("") || txtTProb.getText().equals(""))
			JOptionPane.showMessageDialog(this,"The transition has empty parameters","ERROR",JOptionPane.ERROR_MESSAGE);
		else
		{
			
			added = wl.addTransition(new NavigationTransition(txtFrom.getText(),txtTo.getText(),txtTProb.getText()));
			if(added == 0)
			{
				graphComponent.setGraph( wl.addTransitionToGraph(new NavigationTransition(txtFrom.getText(),txtTo.getText(),txtTProb.getText())));
				graphComponent.refresh();
				mxParallelEdgeLayout parEd = new mxParallelEdgeLayout(graph);
				parEd.execute(graph.getDefaultParent());
			}
			
			if(added==1)
			{ 
				JOptionPane.showMessageDialog(this,"One of the nodes doesn't exist","ERROR",JOptionPane.ERROR_MESSAGE);
			}
			if(added==2)
			{ 
				JOptionPane.showMessageDialog(this,"This transition already exists","ERROR",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void editTransition()
	{
		mxCell edge = (mxCell) graphComponent.getGraph().getSelectionCell();
		if(edge == null)
			JOptionPane.showMessageDialog(this,"Please, select a transition","ERROR",JOptionPane.ERROR_MESSAGE);
		else
			wl.updateEdge(txtFrom.getText(),txtTo.getText(),txtTProb.getText(), edge);
	}
	public boolean saveWorkload()
	{
		wl.setId(txtWLId.getText());
		wl.setSerializerClass(cbSerializer.getSelectedItem().toString());
		wl.setUsersNumber(txtUsersNumber.getText());
		
		if(!wl.isEmpty())
		{
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			  File file = fileChooser.getSelectedFile();
			  // save to file
			 wl.createXML(file.getAbsolutePath());
			 wl.createJGraphXML("./graph/"+file.getName());
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this,"Some parameters are empty","ERROR",JOptionPane.ERROR_MESSAGE);
			return false;
		}

	return true;
	}
	
	public void loadWorkload()
	{
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
		  File file = fileChooser.getSelectedFile();
		  File fileGraph = new File("./graph/"+file.getName());
		  // load from file
		
		  wl.readXML(file.getAbsolutePath());
		  System.out.println(fileGraph.getPath());
		  if(fileGraph.exists() && fileGraph.isFile())
		  {
			  int reply = JOptionPane.showConfirmDialog(null, "Would you like to load the associated graph?", "Graph file found.", JOptionPane.YES_NO_OPTION);
		        if (reply == JOptionPane.YES_OPTION) {
					  graphComponent.setGraph(wl.readGraphXML(fileGraph.getPath()));
					  graphComponent.refresh();
		        }
		        else {
					  System.out.println("generate a graph from workload");
					  graphComponent.setGraph(wl.generateGraph());
					  graphComponent.refresh();
		        }

		  }else
		  {
			  System.out.println("generate a graph from workload");
			  graphComponent.setGraph(wl.generateGraph());
			  graphComponent.refresh();
		  }
			  
		}
		
		txtWLId.setText(wl.getId());
		cbSerializer.setSelectedItem(wl.getSerializerClass());
		txtUsersNumber.setText(wl.getUsersNumber());
	}
	
	
}
