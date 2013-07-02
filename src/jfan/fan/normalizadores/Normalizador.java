package jfan.fan.normalizadores;

import java.util.List;

import jfan.fan.padroes.Padrao;

public interface Normalizador {
		
	public void normalizar(List<Padrao> padroes);
		
	public void normalizar(Padrao p);
		
	public void normalizarThreaded(List<Padrao> padroes) throws InterruptedException;
	
	public void atualizarConfiguracao(NormalizadorConfig config);
	
	
}
