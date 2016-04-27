package com.sniffer.server.views;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.util.List;

import com.sniffer.server.analyzer.PacketAnalyzer;
import com.sniffer.server.model.PacketCaptor;
import com.sniffer.server.model.PacketAnalyzerLoader;

import jpcap.packet.*;

public class TablePaneView extends JPanel implements ActionListener,ListSelectionListener
{
	JDTableView table;
	TableTreeView tree;
	TableTextAreaView text;
	PacketCaptor captor;
	List<PacketAnalyzer> analyzers;
	
	JMenu[] tableViewMenu=new JMenu[4];
	TablePaneView(PacketCaptor captor){
		this.captor=captor;
		table=new JDTableView(this,captor);
		tree=new TableTreeView();
		text=new TableTextAreaView();
		
		JSplitPane splitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JSplitPane splitPane2=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setTopComponent(table);
		splitPane2.setTopComponent(tree);
		splitPane2.setBottomComponent(new JScrollPane(text));
		splitPane.setBottomComponent(splitPane2);
		splitPane.setDividerLocation(200);
		splitPane2.setDividerLocation(200);
		
		tableViewMenu[0]=new JMenu("Datalink Layer");
		tableViewMenu[1]=new JMenu("Network Layer");
		tableViewMenu[2]=new JMenu("Transport Layer");
		tableViewMenu[3]=new JMenu("Application Layer");
		analyzers=PacketAnalyzerLoader.getAnalyzers();
		JMenuItem item,subitem;
		
		for(int i=0;i<analyzers.size();i++){
			PacketAnalyzer analyzer=analyzers.get(i);
			item=new JMenu(analyzer.getProtocolName());
			String[] valueNames=analyzer.getValueNames();
			if(valueNames==null) continue;
			for(int j=0;j<valueNames.length;j++){
				subitem=new JCheckBoxMenuItem(valueNames[j]);
				subitem.setActionCommand("TableView"+i);
				subitem.addActionListener(this);
				item.add(subitem);
			}
			tableViewMenu[analyzer.layer].add(item);
		}

		setLayout(new BorderLayout());
		add(splitPane,BorderLayout.CENTER);

		loadProperty();
		setSize(400,200);
	}
	
	public void fireTableChanged(){
		table.fireTableChanged();
	}
	
	void clear(){
		table.clear();
	}
	
	public void setTableViewMenu(JMenu menu){
		menu.add(tableViewMenu[0]);
		menu.add(tableViewMenu[1]);
		menu.add(tableViewMenu[2]);
		menu.add(tableViewMenu[3]);
	}
	
	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		
		if(cmd.startsWith("TableView")){
			int index=Integer.parseInt(cmd.substring(9));
			JCheckBoxMenuItem item=(JCheckBoxMenuItem)evt.getSource();
			table.setTableView(analyzers.get(index),item.getText(),item.isSelected());
		}
	}
	
	public void valueChanged(ListSelectionEvent evt){
		if(evt.getValueIsAdjusting()) return;
		
		int index=((ListSelectionModel)evt.getSource()).getMinSelectionIndex();
		if(index>=0){
			Packet p=(Packet)captor.getPackets().get(table.sorter.getOriginalIndex(index));
			tree.analyzePacket(p);
			text.showPacket(p);
		}
	}
	
	void loadProperty(){
		//get all menus
		Component[] menus=new Component[analyzers.size()];
		int k=0;
		for(int j=0;j<tableViewMenu[0].getMenuComponents().length;j++)
			menus[k++]=tableViewMenu[0].getMenuComponents()[j];
		for(int j=0;j<tableViewMenu[1].getMenuComponents().length;j++)
			menus[k++]=tableViewMenu[1].getMenuComponents()[j];
		for(int j=0;j<tableViewMenu[2].getMenuComponents().length;j++)
			menus[k++]=tableViewMenu[2].getMenuComponents()[j];
		for(int j=0;j<tableViewMenu[3].getMenuComponents().length;j++)
			menus[k++]=tableViewMenu[3].getMenuComponents()[j];
		
		//load ptoperty
		StringTokenizer status=new StringTokenizer(JpcapDumper.preferences.get("TableView",
				"Ethernet Frame:Source MAC,Ethernet Frame:Destination MAC,IPv4:Source IP,IPv4:Destination IP"),",");
		
		while(status.hasMoreTokens()){
			StringTokenizer s=new StringTokenizer(status.nextToken(),":");
			if(s.countTokens()==2){
				String name=s.nextToken(),valueName=s.nextToken();
				//for(int i=0;i<analyzers.length;i++)
					//if(analyzers[i].getProtocolName().equals(name)){
				for(int i=0;i<menus.length;i++){
					if(((JMenu)menus[i]).getText()==null || name==null) continue;
					if(((JMenu)menus[i]).getText().equals(name)){
						Component[] vn=((JMenu)menus[i]).getMenuComponents();
						//table.setTableView(analyzers[i],n,true);
						for(int j=0;j<vn.length;j++)
							if(valueName.equals(((JCheckBoxMenuItem)vn[j]).getText())){
								((JCheckBoxMenuItem)vn[j]).setState(true);
								break;
							}
						break;
					}
				}
				
				for(PacketAnalyzer analyzer:analyzers)
					if(analyzer.getProtocolName().equals(name)){
						table.setTableView(analyzer,valueName,true);
						break;
					}
			}
		}
	}
	
	public void saveProperty(){
		String[] viewStatus=table.getTableViewStatus();
		if(viewStatus.length>0){
			StringBuffer buf=new StringBuffer(viewStatus[0]);
			for(int i=1;i<viewStatus.length;i++)
				buf.append(","+viewStatus[i]);
			//JpcapDumper.JDProperty.setProperty("TableView",buf.toString());
			JpcapDumper.preferences.put("TableView",buf.toString());
		}
	}
}
