package jfan.io.bin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import jfan.fan.MonitorFAN;
import jfan.io.LeitorMonitor;

public class LeitorMonitorBinario implements LeitorMonitor {

	@Override
	public MonitorFAN ler(File arquivo) throws IOException {
		ObjectInputStream in = null;
		MonitorFAN monitor = null;
		try
		{
		
		in = new ObjectInputStream(new
                BufferedInputStream(new FileInputStream(arquivo)));
		monitor = (MonitorFAN) in.readObject();
		
		
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (in != null) {
				in.close();
			}
		}

		
		return monitor;
	}

}
