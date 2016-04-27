package com.sniffer.server.views;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.sniffer.server.model.PacketCaptor;
import com.sniffer.server.stats.JDStatisticsTaker;

public class FrameView extends JFrame //implements ActionListener
{
	public PacketCaptor captor;
	
	public JLabel statusLabel;
	public JMenuItem openMenu,saveMenu,captureMenu,stopMenu, newWinMenu, exitMenu;
	public JMenu statMenu;
	public JButton openButton,saveButton,captureButton,stopButton;
	public JMenuItem overallmenu, netprotostat;
	
	public TablePaneView tablePane;

	public static FrameView openNewWindow(PacketCaptor captor){
		FrameView frame=new FrameView(captor);
		frame.setVisible(true);
		
		return frame;
	}

	public FrameView(PacketCaptor captor){
		this.captor=captor;
		tablePane=new TablePaneView(captor);
		captor.setJDFrame(this);
		
		setTitle("Packet Sniffer");

		// Create Menu
		JMenuBar menuBar=new JMenuBar();
		setJMenuBar(menuBar);
		
		//System Menu
		JMenu menu=new JMenu("System");
		menuBar.add(menu);
		newWinMenu = new JMenuItem("New Window");
		newWinMenu.setActionCommand("NewWin");
//		newWinMenu.addActionListener(this);
		menu.add(newWinMenu);
		
		exitMenu=new JMenuItem("Exit");
		exitMenu.setActionCommand("Exit");
//		exitMenu.addActionListener(this);
		menu.add(exitMenu);
		
		//File Menu
		menu=new JMenu("File");
		menuBar.add(menu);
		openMenu=new JMenuItem("Open");
		openMenu.setIcon(getImageIcon("/image/open.png"));
		openMenu.setActionCommand("Open");
//		openMenu.addActionListener(this);
		menu.add(openMenu);
		saveMenu=new JMenuItem("Save");
		saveMenu.setIcon(getImageIcon("/image/save.png"));
		saveMenu.setActionCommand("Save");
//		saveMenu.addActionListener(this);
		saveMenu.setEnabled(false);
		menu.add(saveMenu);

		//Capture Menu
		menu=new JMenu("Capture");
		menuBar.add(menu);
		captureMenu=new JMenuItem("Start");
		captureMenu.setIcon(getImageIcon("/image/start.png"));
		captureMenu.setActionCommand("Start");
//		captureMenu.addActionListener(this);
		menu.add(captureMenu);
		stopMenu=new JMenuItem("Stop");
		stopMenu.setIcon(getImageIcon("/image/stop.png"));
		stopMenu.setActionCommand("Stop");
//		stopMenu.addActionListener(this);
		stopMenu.setEnabled(false);
		menu.add(stopMenu);
		
		JMenuItem item;
		//Stat Menu
		statMenu=new JMenu("Statistics");
		menuBar.add(statMenu);
		
		overallmenu = new JMenuItem("Summary");
		overallmenu.setActionCommand("summary");
		overallmenu.setEnabled(true);
		statMenu.add(overallmenu);
		
		netprotostat = new JMenuItem("Network Protocols");
		netprotostat.setActionCommand("networkprotocolstat");
		netprotostat.setEnabled(true);
		statMenu.add(netprotostat);
		
//		menu=new JMenu("Cumulative");
//		statMenu.add(menu);
//		java.util.List<JDStatisticsTaker> stakers=StatisticsTakerLoader.getStatisticsTakers();
//		for(int i=0;i<stakers.size();i++){
//			item=new JMenuItem(stakers.get(i).getName());
//			item.setActionCommand("CUMSTAT"+i);
////			item.addActionListener(this);
//			menu.add(item);
//		}
//		menu=new JMenu("Continuous");
//		statMenu.add(menu);
//		for(int i=0;i<stakers.size();i++){
//			item=new JMenuItem(stakers.get(i).getName());
//			item.setActionCommand("CONSTAT"+i);
////			item.addActionListener(this);
//			menu.add(item);
//		}

		//View menu
//		menu=new JMenu("View");
//		menuBar.add(menu);
//		tablePane.setTableViewMenu(menu);

		//Create Toolbar
		JToolBar toolbar=new JToolBar();
		toolbar.setFloatable(false);
		openButton=new JButton(getImageIcon("/image/open.png"));
		openButton.setActionCommand("Open");
//		openButton.addActionListener(this);
		toolbar.add(openButton);
		saveButton=new JButton(getImageIcon("/image/save.png"));
		saveButton.setActionCommand("Save");
//		saveButton.addActionListener(this);
		saveButton.setEnabled(false);
		toolbar.add(saveButton);
		toolbar.addSeparator();
		captureButton=new JButton(getImageIcon("/image/start.png"));
		captureButton.setActionCommand("Start");
//		captureButton.addActionListener(this);
		toolbar.add(captureButton);
		stopButton=new JButton(getImageIcon("/image/stop.png"));
		stopButton.setActionCommand("Stop");
//		stopButton.addActionListener(this);
		stopButton.setEnabled(false);
		toolbar.add(stopButton);
		
		statusLabel=new JLabel("JpcapDumper started.");
		
		getContentPane().setLayout(new BorderLayout());
		//getContentPane().add(desktop,BorderLayout.CENTER);
		getContentPane().add(statusLabel,BorderLayout.SOUTH);
		getContentPane().add(tablePane,BorderLayout.CENTER);
		//getContentPane().add(toolbar,BorderLayout.NORTH);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt){
				saveProperty();
				JpcapDumper.closeWindow((FrameView)evt.getSource());
			}
		});
		
		loadProperty();
		//pack();
	}
	
	public void clear(){
		tablePane.clear();
	}

	public void startUpdating(){
		JDFrameUpdater.setRepeats(true);
		JDFrameUpdater.start();
	}
	
	public void stopUpdating(){
		JDFrameUpdater.stop();
		JDFrameUpdater.setRepeats(false);
		JDFrameUpdater.start();
	}

	javax.swing.Timer JDFrameUpdater=new javax.swing.Timer(500,new ActionListener(){
		public void actionPerformed(ActionEvent evt){
			tablePane.fireTableChanged();
			statusLabel.setText("Captured "+captor.getPackets().size()+" packets.");

			repaint();
		}
	});

	void loadProperty(){
		setSize(Integer.parseInt(JpcapDumper.preferences.get("WinWidth","640")),
		        Integer.parseInt(JpcapDumper.preferences.get("WinHeight","480")));
		setLocation(Integer.parseInt(JpcapDumper.preferences.get("WinX","0")),
			Integer.parseInt(JpcapDumper.preferences.get("WinY","0")));
	}
	
	void saveProperty(){
		//JpcapDumper.JDProperty.setProperty("WinWidth",String.valueOf(getBounds().width));
		//JpcapDumper.JDProperty.setProperty("WinHeight",String.valueOf(getBounds().height));
		JpcapDumper.preferences.put("WinWidth",String.valueOf(getBounds().width));
		JpcapDumper.preferences.put("WinHeight",String.valueOf(getBounds().height));
		JpcapDumper.preferences.put("WinX",String.valueOf(getBounds().x));
		JpcapDumper.preferences.put("WinY",String.valueOf(getBounds().y));
		
		tablePane.saveProperty();
		
		JpcapDumper.saveProperty();
	}
	
	public void enableCapture(){
		openMenu.setEnabled(true);
		openButton.setEnabled(true);
		saveMenu.setEnabled(true);
		saveButton.setEnabled(true);
		captureMenu.setEnabled(true);
		captureButton.setEnabled(true);
		stopMenu.setEnabled(false);
		stopButton.setEnabled(false);
	}
	
	public void disableCapture(){
		openMenu.setEnabled(false);
		openButton.setEnabled(false);
		captureMenu.setEnabled(false);
		captureButton.setEnabled(false);
		saveMenu.setEnabled(true);
		saveButton.setEnabled(true);
		stopMenu.setEnabled(true);
		stopButton.setEnabled(true);
	}
	
	private ImageIcon getImageIcon(String path){
		return new ImageIcon(this.getClass().getResource(path));
	}
}
