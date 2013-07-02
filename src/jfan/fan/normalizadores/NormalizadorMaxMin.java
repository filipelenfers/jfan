package jfan.fan.normalizadores;

import java.io.Serializable;
import java.util.List;

import jfan.fan.padroes.Padrao;
import jfan.io.json.annotations.JsonTransient;

/**
 * Normaliza os padr�es, deixando eles entre 0.0 e 1.0. Essa normaliza��o
 * usa a seguinte f�rmula:
 * <p> (valorCaracteristica - valorMinimoCaracteristica) / (ValorMaximoCaracteristica - valorMinimoCaracteristica) </p>  
 * @author filipe
 *
 */
public class NormalizadorMaxMin extends NormalizadorBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6437854935089998203L;
	
	protected double[] max = null;
	protected double[] min = null;
	
	@JsonTransient
	protected double[]  maxMinusMin;
	
	List<Padrao> conjuntoArmazenado = null;
	int caracEscolhida;
	
	public NormalizadorMaxMin(){}
	
	public NormalizadorMaxMin(NormalizadorConfig config){
		atualizarConfiguracao(config);
	}
	
	private void calcularMaxMinusMin() {
		if (min != null && max != null && min.length == max.length) {
			maxMinusMin = new double[min.length];
			for (int i = 0; i < maxMinusMin.length; i++) {
				maxMinusMin[i] = max[i] - min[i];
			}
		}
	}
		
	
	public NormalizadorMaxMin(double[] max, double[] min) {
		this.max = max;
		this.min = min;
		calcularMaxMinusMin();
	}
	
	
	public void normalizar(Padrao p, int caracteristica) {
		double caracNorm;
		caracNorm = p.getCaracteristica(caracteristica) - min[caracteristica];
		caracNorm /= maxMinusMin[caracteristica];
		if (caracNorm > 1.0) {
			caracNorm = 1.0;
		}
		if (caracNorm < 0) {
			caracNorm = 0.0;
		}
		p.setCaracteristica(caracteristica, caracNorm);
	}
	
	/**
	 * Normaliza os padr�es iniciando uma thread para cada caracter�stica.
	 * O m�todo s� retorna caso todas as threads tenham completado seu processamento,
	 * assim os padr�es estar�o normalizados. 
	 * @param padroes Os padr�es que ser�o normalizados.
	 * @throws InterruptedException 
	 */
	public void normalizarThreaded(List<Padrao> padroes) throws InterruptedException {
		if (padroes.size() <= 0) {
			return;
		}
		int quantCarac = padroes.get(0).getQuantasCaracteristicas();
		Thread[] threads = new Thread[quantCarac];
		NormalizadorMaxMin np;
		for (int i = 0; i< quantCarac; i++){
			np = new NormalizadorMaxMin(max,min);
			np.conjuntoArmazenado = padroes;
			np.caracEscolhida = i;
			threads[i] = new Thread(np);
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
		min = config.getMinimos();
		calcularMaxMinusMin();
	}
		
	
}

