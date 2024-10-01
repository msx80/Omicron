package com.github.msx80.omicron.fantasyconsole;


import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

public class ProjectCreatorWindow {

	private JFrame frmOmicronCartridgeCreator;
	private JTextField txtMyAwesomeGame;
	private JTextField txtCommyawesomegame;
	private JTextField txtAwesomegame;
	private JTextField textFolder;
	private JButton btnCreateCartridge;
	private JButton btnNewButton;
	private JLabel lblSorryForThe;
	private JLabel lblHopefullyItWill;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(
				            UIManager.getSystemLookAndFeelClassName());
					ProjectCreatorWindow window = new ProjectCreatorWindow();
					window.frmOmicronCartridgeCreator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public ProjectCreatorWindow() {
		initialize();
	}

	private void initialize() {
		frmOmicronCartridgeCreator = new JFrame();
		frmOmicronCartridgeCreator.setTitle("Omicron Cartridge Creator");
		frmOmicronCartridgeCreator.setBounds(100, 100, 450, 300);
		frmOmicronCartridgeCreator.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmOmicronCartridgeCreator.getContentPane().setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]"));
		
		JLabel lblNewLabel = new JLabel("Cartridge Name:");
		frmOmicronCartridgeCreator.getContentPane().add(lblNewLabel, "cell 0 0,alignx trailing");
		
		txtMyAwesomeGame = new JTextField();
		txtMyAwesomeGame.setText("My Awesome Game!");
		txtMyAwesomeGame.setToolTipText("This is your game full name, mostly all characters allowed.");
		frmOmicronCartridgeCreator.getContentPane().add(txtMyAwesomeGame, "cell 1 0,growx");
		txtMyAwesomeGame.setColumns(10);
		
		JLabel lblPackage = new JLabel("Package:");
		frmOmicronCartridgeCreator.getContentPane().add(lblPackage, "cell 0 1,alignx trailing");
		
		txtCommyawesomegame = new JTextField();
		txtCommyawesomegame.setToolTipText("This is the java package name your game will live in. In android this is also the app code.");
		txtCommyawesomegame.setText("com.my.awesome.game");
		frmOmicronCartridgeCreator.getContentPane().add(txtCommyawesomegame, "cell 1 1,growx");
		txtCommyawesomegame.setColumns(10);
		
		JLabel lblFolder = new JLabel("Code and Main:");
		frmOmicronCartridgeCreator.getContentPane().add(lblFolder, "cell 0 2,alignx trailing");
		
		txtAwesomegame = new JTextField();
		txtAwesomegame.setToolTipText("This is an alphanum only code that is both your main class name, project folder and your cartridge name.");
		txtAwesomegame.setText("AwesomeGame");
		frmOmicronCartridgeCreator.getContentPane().add(txtAwesomegame, "cell 1 2,growx");
		txtAwesomegame.setColumns(10);
		
		JLabel lblFolder_1 = new JLabel("Folder:");
		frmOmicronCartridgeCreator.getContentPane().add(lblFolder_1, "cell 0 3,alignx trailing");
		
		textFolder = new JTextField();
		textFolder.setToolTipText("Parent folder where this cartridge folder will be created.");
		frmOmicronCartridgeCreator.getContentPane().add(textFolder, "flowx,cell 1 3,growx");
		textFolder.setColumns(10);
		
		btnCreateCartridge = new JButton("Create Cartridge!");
		btnCreateCartridge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Path p = ProjectCreator.createProject(Paths.get(textFolder.getText()), txtAwesomegame.getText(), txtCommyawesomegame.getText(), txtAwesomegame.getText());
					JOptionPane.showMessageDialog(frmOmicronCartridgeCreator, "Your cartridge have been created! Click ok to open folder.", "Success!", JOptionPane.INFORMATION_MESSAGE);
					Desktop. getDesktop(). open(p.toFile());
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frmOmicronCartridgeCreator, "Something went wrong: "+e1.getMessage(), "Something went wrong!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		frmOmicronCartridgeCreator.getContentPane().add(btnCreateCartridge, "cell 1 4");
		
		btnNewButton = new JButton("Select..");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            int option = fileChooser.showOpenDialog(frmOmicronCartridgeCreator);
	            if(option == JFileChooser.APPROVE_OPTION){
	               File file = fileChooser.getSelectedFile();
	               textFolder.setText(file.getAbsolutePath());
	            }else{
	            	// textFolder.setText("Open command canceled");
	            }
			}
		});
		frmOmicronCartridgeCreator.getContentPane().add(btnNewButton, "cell 1 3");
		
		lblSorryForThe = new JLabel("Sorry for the crudeness of this dialog :)");
		frmOmicronCartridgeCreator.getContentPane().add(lblSorryForThe, "cell 1 6");
		
		lblHopefullyItWill = new JLabel("Hopefully it will be properly included in the Player in future.");
		frmOmicronCartridgeCreator.getContentPane().add(lblHopefullyItWill, "cell 1 7");
	}

}
