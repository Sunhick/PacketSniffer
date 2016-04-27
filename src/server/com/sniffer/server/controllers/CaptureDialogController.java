package com.sniffer.server.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.sniffer.server.views.CaptureDialogView;

import jpcap.JpcapCaptor;

public class CaptureDialogController implements ActionListener{
	
	private CaptureDialogView view;
	
	public CaptureDialogController(CaptureDialogView view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		
		if(cmd.equals("Whole")){
			view.caplenField.setText("1514");
			view.caplenField.setEnabled(false);
		}else if(cmd.equals("Head")){
			view.caplenField.setText("68");
			view.caplenField.setEnabled(false);
		}else if(cmd.equals("Other")){
			view.caplenField.setText("");
			view.caplenField.setEnabled(true);
			view.caplenField.requestFocus();
		}else if(cmd.equals("OK")){
			try{
				int caplen=Integer.parseInt(view.caplenField.getText());
				if(caplen<68 || caplen>1514){
					JOptionPane.showMessageDialog(null,"Capture length must be between 68 and 1514.");
					return;
				}
				
				view.jpcap=JpcapCaptor.openDevice(view.devices[view.adapterComboBox.getSelectedIndex()],caplen,
						view.promiscCheck.isSelected(),50);
				
				if(view.filterField.getText()!=null && view.filterField.getText().length()>0){
					view.jpcap.setFilter(view.filterField.getText(),true);
				} 
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null,"Please input valid integer in capture length.");
			}catch(java.io.IOException e){
				JOptionPane.showMessageDialog(null,e.toString());
				view.jpcap=null;
			}finally{
				view.dispose();
			}
		}else if(cmd.equals("Cancel")){
			view.dispose();
		}
	}

}
