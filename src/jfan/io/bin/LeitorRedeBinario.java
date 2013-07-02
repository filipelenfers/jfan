package jfan.io.bin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import jfan.fan.MonitorFAN;
import jfan.fan.NeuronioFAN;
import jfan.fan.RedeFAN;
import jfan.fan.normalizadores.Normalizador;
import jfan.io.LeitorRede;

public class LeitorRedeBinario implements LeitorRede {

	@Override
	public MonitorFAN ler(File arquivo) throws IOException{
		ObjectInputStream in = null;
		RedeFAN rede = null;
		Normalizador normalizador = null;
		try
		{
		
		in = new ObjectInputStream(new
                BufferedInputStream(new FileInputStream(arquivo)));
		rede = (RedeFAN) in.readObject();
		normalizador = (Normalizador) in.readObject();
		
		
		//Seta a rede para os neurônios
		NeuronioFAN[] neuronios = rede.getNeuronios();
		for(NeuronioFAN n : neuronios) {
			n.setRede(rede);
		}
		
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (in != null) {
				in.close();
			}
		}
		
		MonitorFAN monitor = new MonitorFAN(rede);
		monitor.setNormalizador(normalizador);
		
		return monitor;
	}

}
