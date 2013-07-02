package jfan.io;

import java.io.File;
import java.io.IOException;

import jfan.fan.MonitorFAN;

public interface LeitorRede {
	
	public MonitorFAN ler(File arquivo) throws IOException ;

}
