package com.sniffer.server.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sniffer.server.model.PacketCaptor;
import com.sniffer.server.model.PacketAnalyzerLoader;
import com.sniffer.server.stats.ApplicationProtocolStat;
import com.sniffer.server.stats.PacketStat;
import com.sniffer.server.views.FrameView;
import com.sniffer.server.views.JpcapDumper;

public class FrameController implements ActionListener {
	
	private PacketCaptor captor;
	private FrameView view;
	
	public FrameController(PacketCaptor captor, FrameView view) {
		this.captor = captor;
		this.view = view;
		
		this.view.newWinMenu.addActionListener(this);
		this.view.exitMenu.addActionListener(this);
		this.view.openMenu.addActionListener(this);
		this.view.saveMenu.addActionListener(this);
		this.view.captureMenu.addActionListener(this);
		this.view.stopMenu.addActionListener(this);
		
		this.view.openButton.addActionListener(this);
		this.view.saveButton.addActionListener(this);
		this.view.captureButton.addActionListener(this);
		this.view.stopButton.addActionListener(this);
		
		this.view.overallmenu.addActionListener(this);
		this.view.netprotostat.addActionListener(this);

		PacketAnalyzerLoader.loadDefaultAnalyzer();
	}

	void saveProperty(){
		//JpcapDumper.JDProperty.setProperty("WinWidth",String.valueOf(getBounds().width));
		//JpcapDumper.JDProperty.setProperty("WinHeight",String.valueOf(getBounds().height));
		JpcapDumper.preferences.put("WinWidth",String.valueOf(view.getBounds().width));
		JpcapDumper.preferences.put("WinHeight",String.valueOf(view.getBounds().height));
		JpcapDumper.preferences.put("WinX",String.valueOf(view.getBounds().x));
		JpcapDumper.preferences.put("WinY",String.valueOf(view.getBounds().y));
		
		JpcapDumper.saveProperty();
	}
	
	@Override
	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		
		if(cmd.equals("Open")){
			captor.loadPacketsFromFile();
		}else if(cmd.equals("Save")){
			captor.saveToFile();
		}else if(cmd.equals("NewWin")){
			JpcapDumper.openNewWindow();
		}else if(cmd.equals("Exit")){
			saveProperty();
			System.exit(0);
		}else if(cmd.equals("Start")){
			captor.capturePacketsFromDevice();
		}else if(cmd.equals("Stop")){
			captor.stopCapture();
		}else if(cmd.startsWith("summary")){
			captor.addCumulativeStatFrame(new PacketStat());
		}else if(cmd.startsWith("networkprotocolstat")){
			captor.addCumulativeStatFrame(new ApplicationProtocolStat());
		}
	}
}
