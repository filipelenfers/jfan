package jfan.io;

import java.io.File;
import java.io.IOException;

import jfan.fan.MonitorFAN;

public interface PersistorMonitor {
	
	public void persistir(MonitorFAN monitor, File arquivo) throws IOException;

}
