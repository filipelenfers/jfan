package jfan.fan.normalizadores;

import java.util.Arrays;
import java.util.List;

import jfan.fan.padroes.Padrao;

public class NormalizadorConfig {
	private double[] maxs;
	private double[] mins;
	private double[] means;
	
	public NormalizadorConfig() {}
	
	public NormalizadorConfig(double[] max, double[] min, double[] mean) {
		maxs = max;
		mins = min;
		means = mean;
	}

	public double[] getMaximos() {
		return this.maxs;
	}

	public void setMaximos(double[] maxs) {
		this.maxs = maxs;
	}

	public double[] getMedias() {
		return this.means;
	}

	public void setMedias(double[] means) {
		this.means = means;
	}

	public double[] getMinimos() {
		return this.mins;
	}

	public void setMinimos(double[] mins) {
		this.mins = mins;
	}
	
	public static NormalizadorConfig getNormalizadorConfiguration(List<Padrao> padroes)	{
		//TODO disparar exce��o quando a lista de padr�es tiver tamanho 0;
		NormalizadorConfig config = new NormalizadorConfig();
		int numCarateristicas = padroes.get(0).getQuantasCaracteristicas();
		
		double[] max = new double[numCarateristicas];
		double[] min = new double[numCarateristicas];
		double[] media = new double[numCarateristicas];
		
		//Preenche o array de minimos com o valor máximo de double para facilitar
		Arrays.fill(min, Double.MAX_VALUE);
		
		//Preenche o array de máximos com o valor máximo de double para facilitar
		Arrays.fill(max, Double.MIN_VALUE);
		
		//Preenche a média com 0.0 para usar inicialmente o arrai como somatório
		Arrays.fill(media, 0.);
		
		for(Padrao p : padroes) {
			for(int i = 0; i < numCarateristicas; i++)
			{
				//ajusta o máximo
				if (max[i] < p.getCaracteristica(i)) {
					max[i] = p.getCaracteristica(i);
				}
				
				//ajusta o mínimo
				if (min[i] > p.getCaracteristica(i)) {
					min[i] = p.getCaracteristica(i);
				}
				
				//somatorio para média
				media[i] += p.getCaracteristica(i);
			}
		}
		
		//Calula as médias, nesse ponto o array só tem o somatório
		for(int i = 0; i < numCarateristicas; i++) {
			media[i] /= padroes.size();
		}
		
		config.setMaximos(max);
		config.setMinimos(min);
		config.setMedias(media);
		
		return config;
	}
	
	
	
}
