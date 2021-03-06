/*
 * Created on Apr 4, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sniffer.server.model;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sniffer.server.stats.JDStatisticsTaker;
import com.sniffer.server.views.CaptureDialogView;
import com.sniffer.server.views.CumlativeStatFrameView;
import com.sniffer.server.views.FrameView;
import com.sniffer.server.views.JpcapDumper;
import com.sniffer.server.views.StatFrameView;

import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class PacketCaptor {
	long MAX_PACKETS_HOLD=10000;

	List<Packet> packets = new ArrayList<Packet>();

	JpcapCaptor jpcap=null;
	ImportExportData importExportTool = new PcapImportExportData();

	boolean isLiveCapture;
	boolean isSaved = false;

	FrameView frame;

	public void setJDFrame(FrameView frame){
		this.frame=frame;
	}

	public List<Packet> getPackets(){
		return packets;
	}


	public void capturePacketsFromDevice() {
		if(jpcap!=null)
			jpcap.close();
		jpcap = CaptureDialogView.getJpcap(frame);
		clear();
		
		if (jpcap != null) {
			isLiveCapture = true;
			frame.disableCapture();

			startCaptureThread();
		}
	}

	public void loadPacketsFromFile() {
		isLiveCapture = false;
		clear();

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pcap files", "pcap");
		JFileChooser chooser = JpcapDumper.chooser;
		chooser.setFileFilter(filter);
		int ret = chooser.showOpenDialog(frame);
		if (ret == JFileChooser.APPROVE_OPTION) {
			String path = JpcapDumper.chooser.getSelectedFile().getPath();

			try {
				if(jpcap!=null){
					jpcap.close();
				}
				jpcap = importExportTool.Open(path);
			} catch (java.io.IOException e) {
				JOptionPane.showMessageDialog(
					frame,
					"Can't open file: " + path);
				e.printStackTrace();
				return;
			}

			frame.disableCapture();

			startCaptureThread();
		}
	}

	private void clear(){
		packets.clear();
		frame.clear();

		for(int i=0;i<sframes.size();i++)
			((StatFrameView)sframes.get(i)).clear();
	}

	public void saveToFile() {
		if (packets == null)
			return;

		int ret = JpcapDumper.chooser.showSaveDialog(frame);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = JpcapDumper.chooser.getSelectedFile();

			if (file.exists()) {
				if (JOptionPane
					.showConfirmDialog(
						frame,
						"Overwrite " + file.getName() + "?",
						"Overwrite?",
						JOptionPane.YES_NO_OPTION)
					== JOptionPane.NO_OPTION) {
					return;
				}
			}

			try {
				//System.out.println("link:"+info.linktype);
				//System.out.println(lastJpcap);
				isSaved = importExportTool.Save(file, jpcap, packets);
				
				//JOptionPane.showMessageDialog(frame,file+" was saved correctly.");
			} catch (java.io.IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
					frame,
					"Can't save file: " + file.getPath());
			}
		}
	}

	public void stopCapture() {
		stopCaptureThread();
	}

	public void saveIfNot() {
		if (isLiveCapture && !isSaved) {
			int ret =
				JOptionPane.showConfirmDialog(
					null,
					"Save this data?",
					"Save this data?",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION)
				saveToFile();
		}
	}

	List<StatFrameView> sframes=new ArrayList<StatFrameView>();
	public void addCumulativeStatFrame(JDStatisticsTaker taker) {
		sframes.add(CumlativeStatFrameView.openWindow(packets,taker.newInstance()));
	}

	public void closeAllWindows(){
		for(int i=0;i<sframes.size();i++)
			((StatFrameView)sframes.get(i)).dispose();
	}



	private Thread captureThread;

	private void startCaptureThread() {
		if (captureThread != null)
			return;

		captureThread = new Thread(new Runnable(){
			//body of capture thread
			public void run() {
				while (captureThread != null) {
					if (jpcap.processPacket(1, handler) == 0 && !isLiveCapture)
						stopCaptureThread();
					Thread.yield();
				}

				jpcap.breakLoop();
				//jpcap = null;
				frame.enableCapture();
			}
		});
		captureThread.setPriority(Thread.MIN_PRIORITY);
		
		frame.startUpdating();
		for(int i=0;i<sframes.size();i++){
			((StatFrameView)sframes.get(i)).startUpdating();
		}
		
		captureThread.start();
	}

	void stopCaptureThread() {
		captureThread = null;
		frame.stopUpdating();
		for(int i=0;i<sframes.size();i++){
			((StatFrameView)sframes.get(i)).stopUpdating();
		}
	}

	private ExecutorService exe=Executors.newFixedThreadPool(10);
	public static final Map<InetAddress,String> hostnameCache=new HashMap<InetAddress, String>();
	
	private PacketReceiver handler=new PacketReceiver(){
		public void receivePacket(final Packet packet) {
			packets.add(packet);
			while (packets.size() > MAX_PACKETS_HOLD) {
				packets.remove(0);
			}
			if (!sframes.isEmpty()) {
				for (int i = 0; i < sframes.size(); i++)
					((StatFrameView)sframes.get(i)).addPacket(packet);
			}
			isSaved = false;
			
			if(packet instanceof IPPacket){
				exe.execute(new Runnable(){
					public void run() {
						IPPacket ip=(IPPacket)packet;
						if(!hostnameCache.containsKey(ip.src_ip))
							hostnameCache.put(ip.src_ip,ip.src_ip.getHostName());
						if(!hostnameCache.containsKey(ip.dst_ip))
							hostnameCache.put(ip.dst_ip,ip.dst_ip.getHostName());
						System.out.println(hostnameCache.size());
					}
				});
			}
		}
	};

}
