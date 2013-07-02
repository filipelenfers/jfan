package jfan.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jfan.fan.padroes.Padrao;

public interface LeitorPadroes {
	
	/**
	 * Método aonde deve ser implementada a leitura dos padrões.
	 * @param c Coleção aonde serão adicionados os padrões.
	 * @return Retorna a coleção com os padrões brutos.
	 * @throws IOException Quando arquivo não é encontrado ou existe algum problema com a leitura do arquivo.
	 */
	public void lerDados(File arquivo, List<Padrao> c) throws IOException;
	

}
