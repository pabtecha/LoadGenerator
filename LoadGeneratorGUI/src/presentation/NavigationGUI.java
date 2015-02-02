package presentation;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bussines.ExecutionCode;
import bussines.InputData;
import bussines.Navigation;
import bussines.StatisticAttribute;

import javax.swing.JToolBar;

public class NavigationGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8378224363526667292L;
	private JPanel contentPane;
	private JTextField txtNavId;
	private JTextField txtParamValue;
	private JTextField txtInputName;
	private JTextField txtPCA;
	private JCheckBox chckNavigationTime;
	private JCheckBox chckExecutionTime;
	private JCheckBox chckHttpRoute;
	private JCheckBox chckURL;
	private JCheckBox chckbxHttpmethod;
	private JCheckBox chckbxStablished;
	private JCheckBox chckbxStablishmenttime;
	private JCheckBox chckbxTransfertime;
	private JCheckBox chckbxThinkUserTime;
	private JCheckBox chckbxContentSize;
	private JCheckBox chckbxTimestamp;
	private Navigation nav;
	private JToolBar toolBar;
	private JButton btnSave_1;
	private JButton btnOpen;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NavigationGUI frame = new NavigationGUI();
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
	public NavigationGUI() {
		
		nav = new Navigation("",new InputData("",""),new ExecutionCode(""));
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 396, 429);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{220, 156, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 106, 222, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		toolBar = new JToolBar();
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 5);
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		contentPane.add(toolBar, gbc_toolBar);
		
		btnSave_1 = new JButton("Save");
		btnSave_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveNavigation();
			}
		});
		toolBar.add(btnSave_1);
		
		btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openNavigation();
			}
		});
		toolBar.add(btnOpen);
		
		JPanel panelData = new JPanel();
		GridBagConstraints gbc_panelData = new GridBagConstraints();
		gbc_panelData.gridwidth = 2;
		gbc_panelData.fill = GridBagConstraints.BOTH;
		gbc_panelData.insets = new Insets(0, 0, 5, 5);
		gbc_panelData.gridx = 0;
		gbc_panelData.gridy = 1;
		contentPane.add(panelData, gbc_panelData);
		panelData.setLayout(new GridLayout(4, 2, 0, 0));
		
		JLabel lblNavigationId = new JLabel("Navigation id:");
		panelData.add(lblNavigationId);
		
		txtNavId = new JTextField();
		panelData.add(txtNavId);
		txtNavId.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("parameter name:");
		panelData.add(lblNewLabel);
		
		txtParamValue = new JTextField();
		txtParamValue.setText("");
		panelData.add(txtParamValue);
		txtParamValue.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("value:");
		panelData.add(lblNewLabel_1);
		
		txtInputName = new JTextField();
		txtInputName.setText("");
		panelData.add(txtInputName);
		txtInputName.setColumns(10);
		
		JLabel lblPcaplugin = new JLabel("PCA-Plugin:");
		panelData.add(lblPcaplugin);
		
		txtPCA = new JTextField();
		panelData.add(txtPCA);
		txtPCA.setColumns(10);
		
		JPanel panelStatistics = new JPanel();
		GridBagConstraints gbc_panelStatistics = new GridBagConstraints();
		gbc_panelStatistics.gridwidth = 2;
		gbc_panelStatistics.insets = new Insets(0, 0, 5, 5);
		gbc_panelStatistics.fill = GridBagConstraints.BOTH;
		gbc_panelStatistics.gridx = 0;
		gbc_panelStatistics.gridy = 2;
		contentPane.add(panelStatistics, gbc_panelStatistics);
		panelStatistics.setLayout(new GridLayout(11, 1, 0, 0));
		
		chckNavigationTime = new JCheckBox("NavigationTime");
		panelStatistics.add(chckNavigationTime);
		chckNavigationTime.setSelected(true);
		
		chckExecutionTime = new JCheckBox("ExecutionTime");
		chckExecutionTime.setSelected(true);
		panelStatistics.add(chckExecutionTime);
		
		chckHttpRoute = new JCheckBox("HttpRoute");
		chckHttpRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disableOptions();
			}
		});
		chckHttpRoute.setSelected(true);
		panelStatistics.add(chckHttpRoute);
		
		chckURL = new JCheckBox("URL");
		chckURL.setSelected(true);
		panelStatistics.add(chckURL);
		
		chckbxHttpmethod = new JCheckBox("HttpMethod");
		chckbxHttpmethod.setSelected(true);
		panelStatistics.add(chckbxHttpmethod);
		
		chckbxStablished = new JCheckBox("Stablished");
		chckbxStablished.setSelected(true);
		panelStatistics.add(chckbxStablished);
		
		chckbxStablishmenttime = new JCheckBox("StablishmentTime");
		chckbxStablishmenttime.setSelected(true);
		panelStatistics.add(chckbxStablishmenttime);
		
		chckbxTransfertime = new JCheckBox("TransferTime");
		chckbxTransfertime.setSelected(true);
		panelStatistics.add(chckbxTransfertime);
		
		chckbxThinkUserTime = new JCheckBox("ThinkUserTime");
		chckbxThinkUserTime.setSelected(true);
		panelStatistics.add(chckbxThinkUserTime);
		
		chckbxContentSize = new JCheckBox("ContentSize");
		chckbxContentSize.setSelected(true);
		panelStatistics.add(chckbxContentSize);
		
		chckbxTimestamp = new JCheckBox("TimeStamp");
		chckbxTimestamp.setSelected(true);
		panelStatistics.add(chckbxTimestamp);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveNavigation();
			}
		});
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 0, 5);
		gbc_btnSave.gridx = 0;
		gbc_btnSave.gridy = 3;
		contentPane.add(btnSave, gbc_btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancel.gridx = 1;
		gbc_btnCancel.gridy = 3;
		contentPane.add(btnCancel, gbc_btnCancel);
	}
	
	
	/*
	 * Save current navigation
	 */
	public void saveNavigation()
	{
		
		nav.setId(txtNavId.getText());
		nav.setInputData(new InputData(txtInputName.getText(),txtParamValue.getText()));
		nav.setExecutionCode(new ExecutionCode(txtPCA.getText()));
		
		if(nav.isEmpty())
		{ 
			JOptionPane.showMessageDialog(this,"Some parameters are empty","ERROR",JOptionPane.ERROR_MESSAGE);
		}
		else
		{

			
			if(chckNavigationTime.isSelected())	nav.addStatisticAttribute(new StatisticAttribute(chckNavigationTime.getText()));
			if(chckExecutionTime.isSelected()) nav.addStatisticAttribute(new StatisticAttribute(chckExecutionTime.getText()));
			if(chckHttpRoute.isSelected())
			{
				StatisticAttribute	httpStat = new StatisticAttribute(chckHttpRoute.getText());
				if(chckURL.isSelected()) httpStat.addStatisticAttribute(new StatisticAttribute(chckURL.getText()));
				if(chckbxHttpmethod.isSelected()) httpStat.addStatisticAttribute(new StatisticAttribute(chckbxHttpmethod.getText()));
				if(chckbxStablished.isSelected()) httpStat.addStatisticAttribute(new StatisticAttribute(chckbxStablished.getText()));
				if(chckbxStablishmenttime.isSelected()) httpStat.addStatisticAttribute(new StatisticAttribute(chckbxStablishmenttime.getText()));
				if(chckbxTransfertime.isSelected()) httpStat.addStatisticAttribute(new StatisticAttribute(chckbxTransfertime.getText()));
				if(chckbxThinkUserTime.isSelected()) httpStat.addStatisticAttribute(new StatisticAttribute(chckbxThinkUserTime.getText()));
				if(chckbxContentSize.isSelected()) httpStat.addStatisticAttribute(new StatisticAttribute(chckbxContentSize.getText()));
				
				nav.addStatisticAttribute(httpStat);
			}
					
			if(chckbxTimestamp.isSelected()) nav.addStatisticAttribute(new StatisticAttribute(chckbxTimestamp.getText()));
			
			
			
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			  File file = fileChooser.getSelectedFile();
			  // save to file
			 nav.createXML(file.getAbsolutePath());
			}

			
		}

	}
	
	public void openNavigation()
	{
		//construct the navigation object with the information from the XML file given.
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
		  File file = fileChooser.getSelectedFile();
		  // load from file
		  nav.readXML(file.getAbsolutePath());
		}
		
		//set the interface with the attributes from the navigation object
		txtNavId.setText(nav.getId());
		txtInputName.setText(nav.getInputData().getName());
		txtParamValue.setText(nav.getInputData().getValue());
		txtPCA.setText(nav.getExecutionCode().getName());
		
		for(int i=0; i<nav.getStatisticAttributes().size(); i++)
		{
			setCheckbox(nav.getStatisticAttributes().get(i));
		}
		
	}
	
	/*
	 * Set checkbox of an specific option as selected
	 */
	public void setCheckbox(StatisticAttribute option)
	{
		switch(option.getName())
		{
	        case "NavigationTime":  chckNavigationTime.setSelected(true);
	        break;
			case "ExecutionTime":  chckExecutionTime.setSelected(true);
			break;
			case "HttpRoute":  chckHttpRoute.setSelected(true);
								for(int i=0; i< option.getAttributes().size(); i++)
								{
									setCheckbox(option.getAttributes().get(i));
								}
								disableOptions();
			break;
			case "TimeStamp":  chckbxTimestamp.setSelected(true);
			break;
			case "URL":  chckURL.setSelected(true);
			break;
			case "HttpMethod":  chckbxHttpmethod.setSelected(true);
			break;
			case "Stablished":  chckbxStablished.setSelected(true);
			break;
			case "StablishmentTime":  chckbxStablishmenttime.setSelected(true);
			break;
			case "TransferTime":  chckbxTransfertime.setSelected(true);
			break;
			case "ThinkUserTime":  chckbxThinkUserTime.setSelected(true);
			break;
			case "ContentSize":  chckbxContentSize.setSelected(true);
			break;
			
			
			default:
	        break;
		}
		
	}
	
	public void disableOptions()
	{
		if(chckHttpRoute.isSelected())
		{
			chckURL.setEnabled(true);
			chckbxHttpmethod.setEnabled(true);
			chckbxStablished.setEnabled(true);
			chckbxStablishmenttime.setEnabled(true);
			chckbxTransfertime.setEnabled(true);
			chckbxThinkUserTime.setEnabled(true);
			chckbxContentSize.setEnabled(true);
		}
		else
		{
			chckURL.setEnabled(false);
			chckbxHttpmethod.setEnabled(false);
			chckbxStablished.setEnabled(false);
			chckbxStablishmenttime.setEnabled(false);
			chckbxTransfertime.setEnabled(false);
			chckbxThinkUserTime.setEnabled(false);
			chckbxContentSize.setEnabled(false);
		}
	}
}
