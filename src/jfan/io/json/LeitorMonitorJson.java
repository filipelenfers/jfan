package jfan.io.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jfan.fan.MonitorFAN;
import jfan.io.LeitorMonitor;

public class LeitorMonitorJson implements LeitorMonitor {

	@Override
	public MonitorFAN ler(File arquivo) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(arquivo));
		StringBuilder jsonBuilder = new StringBuilder();
		String linha;
		while((linha = br.readLine())!= null) {
			jsonBuilder.append(linha);
		}
		
		
		DeserializadorJson d = new DeserializadorJson();
		
				
		MonitorFAN monitor = d.deserializarMonitor(jsonBuilder.toString());
		
		
		return monitor;
	}

}
