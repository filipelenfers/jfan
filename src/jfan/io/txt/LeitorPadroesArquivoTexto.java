package jfan.io.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import jfan.fan.padroes.Padrao;
import jfan.io.LeitorPadroes;

public class LeitorPadroesArquivoTexto implements LeitorPadroes {

	
	private String token;
	
	public LeitorPadroesArquivoTexto(String token) {
		this.token = token;
	}
	public LeitorPadroesArquivoTexto() {
	}
	
	public void lerDados(File arquivo, List<Padrao> c) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(arquivo));
		try {
			String linha;
			String[] dados;
			double[] dadosNumericos;
			int fim; //,classe
			Padrao p;
			//Double classeTemp;
			while((linha = reader.readLine()) != null){
				linha = linha.trim();
				dados = linha.split(token);
				dadosNumericos = new double[dados.length-1];
				fim = dados.length - 1 ;
				for (int i = 0; i < fim; i++) {
					dadosNumericos[i] = Double.parseDouble(dados[i]);
				}
				
				/*try {
					classeTemp = Double.parseDouble(dados[fim]);
					if (Math.floor(classeTemp) == classeTemp) { //para pegar inteiro disfarçado de double
						classe = classeTemp.intValue();	
					}
					else {
						 classe = mapearClasseString(dados[fim]);
					}
				}
				catch (NumberFormatException e) {
					 classe = mapearClasseString(dados[fim]);
				}*/
				
				p = new Padrao(dadosNumericos, dados[fim]);
				c.add(p);
			}
		}
		finally {
			reader.close();
		}
		
		
	}
	
	//TODO deve mapear classe para string e enviar o mapa para alguem tratar isso.
//	private int mapearClasseString(String classe){
//		return -1;
//	}

	
	public void setToken(String token){
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

	



}
