package com.sniffer.server.model;

import java.io.File;
import java.io.IOException;
import java.util.List;
import jpcap.packet.Packet;
import jpcap.JpcapCaptor;

public interface ImportExportData {
	public JpcapCaptor Open(String file) throws IOException;
	public boolean Save(File file, JpcapCaptor jpcap, List<Packet> packets) 
			throws IOException;
}