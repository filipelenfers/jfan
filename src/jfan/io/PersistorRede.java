package jfan.io;

import java.io.File;
import java.io.IOException;

import jfan.fan.RedeFAN;
import jfan.fan.normalizadores.Normalizador;

public interface PersistorRede {
	
	public void persistir(RedeFAN rede, Normalizador normalizador , File arquivo) throws IOException;

}
