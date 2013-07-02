package jfan.io.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jfan.fan.MonitorFAN;
import jfan.fan.RedeFAN;
import jfan.fan.normalizadores.Normalizador;
import jfan.io.LeitorRede;

public class LeitorRedeJson implements LeitorRede {

	@Override
	public MonitorFAN ler(File arquivo) throws IOException {
		 
		BufferedReader br = new BufferedReader(new FileReader(arquivo));
		StringBuilder jsonBuilder = new StringBuilder();
		String linha;
		while((linha = br.readLine())!= null) {
			jsonBuilder.append(linha);
		}
		
		String jsonRede, jsonNormalizador;
		
		int indiceSepara = jsonBuilder.indexOf("}{");
				
		jsonRede = jsonBuilder.substring(0, ++indiceSepara);
		jsonNormalizador = jsonBuilder.substring(indiceSepara);
		
		DeserializadorJson d = new DeserializadorJson();
		
		RedeFAN rede = d.deserializarRede(jsonRede);
		
		Normalizador normalizador = d.deserializarNormalizador(jsonNormalizador,rede.getNumeroCaracteristicas());
		
		MonitorFAN monitor = new MonitorFAN(rede);
		monitor.setNormalizador(normalizador);
		
		return monitor;
	}
	
	

}
