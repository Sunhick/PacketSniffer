package com.sniffer.server.model;
import java.util.*;

import com.sniffer.server.analyzer.*;

public class PacketAnalyzerLoader
{
	static List<PacketAnalyzer> analyzers=new ArrayList<PacketAnalyzer>();
	static List<List<PacketAnalyzer>> layerAnalyzers=new ArrayList<List<PacketAnalyzer>>();
	
	public static void loadDefaultAnalyzer(){
		analyzers.add(new EthernetAnalyzer());
		analyzers.add(new IPv4Analyzer());
		analyzers.add(new TCPAnalyzer());
		analyzers.add(new UDPAnalyzer());
		analyzers.add(new ICMPAnalyzer());
		analyzers.add(new HTTPAnalyzer());
		
		for(int i=0;i<10;i++)
			layerAnalyzers.add(new ArrayList<PacketAnalyzer>());
		
		for(PacketAnalyzer a:analyzers)
			layerAnalyzers.get(a.layer).add(a);
	}
	
	public static List<PacketAnalyzer> getAnalyzers(){
		return analyzers;
	}
	
	public static List<PacketAnalyzer> getAnalyzersOf(int layer){
		return layerAnalyzers.get(layer);
	}
}
