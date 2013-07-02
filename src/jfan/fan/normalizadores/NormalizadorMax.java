package jfan.fan.normalizadores;

import java.io.Serializable;
import java.util.List;

import jfan.fan.padroes.Padrao;


/**
 * Normaliza as características usando o valor/máximo característica.
 * @author Filipe Pais Lenfers
 * @version 0.4.0
 */

public class NormalizadorMax extends NormalizadorBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6449273764948594027L;
	
	private double[] max = null;
			
	/**
	 * Cria inst�ncia de NormalizadorMax usando os m�ximos dentro do NormalizatorConfiguration passado
	 * como par�metro.
	 * @param cfg O configurador que ter� os m�ximos.
	 */
	public NormalizadorMax(NormalizadorConfig cfg){
		max = cfg.getMaximos();
	}
	
	public NormalizadorMax(double[] maximos){
		max = maximos;
	}


	protected void normalizar(Padrao p, int caracteristica) {
		double caracNorm = p.getCaracteristica(caracteristica);
		caracNorm /= max[caracteristica];
		if (caracNorm > 1.0) {
			caracNorm = 1.0;
		}
		if (caracNorm < 0) {
			caracNorm = 0.0;
		}
		p.setCaracteristica(caracteristica, caracNorm);
	}

	public void normalizarThreaded(List<Padrao> padroes) throws InterruptedException {
		if (padroes.size() <= 0) {
			return;
		}
		int quantCarac = padroes.get(0).getQuantasCaracteristicas();
		Thread[] threads = new Thread[quantCarac];
		NormalizadorMax nm;
		for (int i = 0; i< quantCarac; i++){
			nm = new NormalizadorMax(max);
			nm.conjuntoArmazenado = padroes;
			nm.caracEscolhida = i;
			threads[i] = new Thread(nm);
			threads[i-1].start();
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
	}


	
}
