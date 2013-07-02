package jfan.io.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jfan.fan.MonitorFAN;
import jfan.fan.RedeFAN;
import jfan.fan.normalizadores.Normalizador;
import jfan.fan.normalizadores.NormalizadorConfig;
import jfan.fan.normalizadores.NormalizadorMaxMin;
import jfan.io.PersistorMonitor;

public class PersistorMonitorJson implements PersistorMonitor {

	@Override
	public void persistir(MonitorFAN monitor, File arquivo) throws IOException {
		BufferedWriter bw  = null;
		
		try {
			SerializadorJson ser = new SerializadorJson();

			String s = ser.serializar(monitor);
			
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
	
	public static void main(String[] args) {
		RedeFAN rede = new RedeFAN(2,100,2,2);
		Normalizador norm = new NormalizadorMaxMin();
		norm.atualizarConfiguracao(new NormalizadorConfig());
		MonitorFAN monitor = new MonitorFAN(rede);
		monitor.setNormalizador(norm);
		
		PersistorMonitorJson p = new PersistorMonitorJson();
		try {
			p.persistir(monitor, new File("/home/filipe/monitorJson.json"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("fim");
	}

}
