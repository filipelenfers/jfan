package jfan.io.bin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import jfan.fan.RedeFAN;
import jfan.fan.normalizadores.Normalizador;
import jfan.io.PersistorRede;

public class PersistorRedeBinario implements PersistorRede {

	@Override
	public void persistir(RedeFAN rede, Normalizador normalizador, File arquivo) throws IOException {
		ObjectOutput output = null; 
		try {
			output = new  ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(arquivo)));
			output.writeObject(rede);
			output.writeObject(normalizador);
			
			
			
		}
		finally{
			if(output != null)
				output.close();
		}

	}

}
