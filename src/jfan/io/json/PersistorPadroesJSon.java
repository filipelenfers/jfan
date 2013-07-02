package jfan.io.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import jfan.fan.padroes.Padrao;
import jfan.io.PersistorPadroes;

public class PersistorPadroesJSon implements PersistorPadroes {

	@Override
	public void persistir(List<Padrao> padroes, File arquivo) throws IOException {
		
		BufferedWriter bw  = null;
		
		try {
			SerializadorJson ser = new SerializadorJson();
			
			Padrao[] padroesArray = new Padrao[padroes.size()];// = (Padrao[]) padroes.toArray();
			padroes.toArray(padroesArray);
			
			String s = ser.serializar(padroesArray);
			
			FileWriter fw = new FileWriter(arquivo);
			
			bw = new BufferedWriter(fw);
			
			bw.write(s);
			
			//Não sei pq, mas se não criar a nova linha ele não imprime o texto...
			bw.newLine();
			
		
		
			bw.close();
		
			
		
		}
		finally {
			if(bw != null) {
				bw.close();
			}
			
		}
		
		

	}

}
