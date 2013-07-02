package jfan.io.bin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import jfan.fan.MonitorFAN;
import jfan.io.PersistorMonitor;

public class PersistorMonitorBinario implements PersistorMonitor {

	@Override
	public void persistir(MonitorFAN monitor, File arquivo) throws IOException {
		ObjectOutput output = null; 
		try {
			output = new  ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(arquivo)));
			output.writeObject(monitor);
			
		}
		finally{
			if(output != null)
				output.close();
		}

	}

}
