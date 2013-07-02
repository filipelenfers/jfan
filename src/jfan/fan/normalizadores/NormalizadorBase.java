package jfan.fan.normalizadores;

import java.util.List;

import jfan.fan.padroes.Padrao;
import jfan.io.json.annotations.JsonTransient;

public abstract class NormalizadorBase implements Normalizador, Runnable {

	
	@JsonTransient
	protected transient int caracEscolhida;
	
	@JsonTransient
	protected transient List<Padrao> conjuntoArmazenado;
	
	
	
	@Override
	public void normalizar(List<Padrao> padroes) {
		for (Padrao p : padroes) 
			normalizar(p);
	}

	protected void normalizar(List<Padrao> padroes, int caracteristica) {
		for (Padrao p : padroes) 
			normalizar(p, caracteristica);
	}

	@Override
	public void normalizar(Padrao p) {
		int quantas = p.getQuantasCaracteristicas();
		for (int i = 0; i < quantas; i++) {
			normalizar(p,i);
		}
	}

	
	protected abstract void normalizar(Padrao p, int caracteristica) ;
	
	@Override
	public abstract void normalizarThreaded(List<Padrao> padroes) throws InterruptedException;

	@Override
	public void run() {
		normalizar(conjuntoArmazenado, caracEscolhida);
	}

	@Override
	public abstract void atualizarConfiguracao(NormalizadorConfig config);

}