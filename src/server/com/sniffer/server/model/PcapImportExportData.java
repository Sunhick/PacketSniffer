package com.sniffer.server.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.packet.Packet;

public class PcapImportExportData implements ImportExportData {

	@Override
	public JpcapCaptor Open(String path) throws IOException {
		JpcapCaptor jpcap = JpcapCaptor.openFile(path);
		return jpcap;
	}

	@Override
	public boolean Save(File file, JpcapCaptor jpcap, List<Packet> packets) throws IOException {
		//System.out.println("link:"+info.linktype);
		//System.out.println(lastJpcap);
		JpcapWriter writer = JpcapWriter.openDumpFile(jpcap,file.getPath());

		for (Packet p:packets) {
			writer.writePacket(p);
		}

		writer.close();
		return true;
	}

}
