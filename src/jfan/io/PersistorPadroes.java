package jfan.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jfan.fan.padroes.Padrao;

public interface PersistorPadroes {
	
	public void persistir(List<Padrao> padroes, File file) throws IOException;

}
