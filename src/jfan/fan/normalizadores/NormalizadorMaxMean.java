package jfan.fan.normalizadores;

import java.io.Serializable;
import java.util.List;

import jfan.fan.padroes.Padrao;

public class NormalizadorMaxMean extends NormalizadorBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3380224353376484776L;
	
	private double[] max;
	private double[] mean;

	public NormalizadorMaxMean(NormalizadorConfig config) {
		atualizarConfiguracao(config);
	}
	
	public NormalizadorMaxMean(double[] max, double[] mean) {
		this.max = max;
		this.mean = mean;
	}
	
	
	public void normalizar(Padrao p, int caracteristica) {
		double caracNorm = p.getCaracteristica(caracteristica) - mean[caracteristica];
		caracNorm /= (max[caracteristica] - mean[caracteristica]);
		caracNorm++;
		caracNorm /= 2;
		p.setCaracteristica(caracteristica, caracNorm);
	}

	public void normalizarThreaded(List<Padrao> padroes) throws InterruptedException {
		if (padroes.size() <= 0) {
			return;
		}
		int quantCarac = padroes.get(0).getQuantasCaracteristicas();
		Thread[] threads = new Thread[quantCarac];
		NormalizadorMaxMean nm;
		for (int i = 0; i< quantCarac; i++){
			nm = new NormalizadorMaxMean(max, mean);
			nm.conjuntoArmazenado = padroes;
			nm.caracEscolhida = i;
			threads[i] = new Thread(nm);
			threads[i].start();
		}
		boolean loop;
		do {
			Thread.yield();
			loop = false;
			for (Thread t : threads) {
				loop = loop || t.isAlive();
			}
		}
		while(loop);
	}

	@Override
	public void atualizarConfiguracao(NormalizadorConfig config) {
		max = config.getMaximos();
		mean = config.getMedias();
	}
	
	

}
