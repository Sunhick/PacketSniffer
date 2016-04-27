package com.sniffer.server.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jpcap.JpcapCaptor;
import jpcap.packet.Packet;

public class XmlImportExportData implements ImportExportData {

	@Override
	public JpcapCaptor Open(String file) throws IOException {
		throw new IOException("Not implemented");
	}

	@Override
	public boolean Save(File file, JpcapCaptor jpcap, List<Packet> packets) throws IOException {
		throw new IOException("Not Implemented");
	}

}
