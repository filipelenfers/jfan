package io;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import jfan.fan.padroes.Padrao;
import jfan.io.LeitorPadroes;
import jfan.io.PersistorPadroes;
import jfan.io.json.LeitorPadroesJson;
import jfan.io.json.PersistorPadroesJSon;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class PadroesJsonTest {
	
	
	List<Padrao> padroes;
	File arquivo = new File(".\\srcTestes\\arquivos\\PadroesTest.json");
	
	@Before
	public void preparaPadroes(){
		padroes = new LinkedList<Padrao>();
		padroes.add(new Padrao(new double[]{0.0,0.0},"0"));
		padroes.add(new Padrao(new double[]{0.0,1.0},"0"));
		padroes.add(new Padrao(new double[]{1.0,0.0},"0"));
		padroes.add(new Padrao(new double[]{1.0,1.0},"1"));
		
	}
	
	
	@Test
	public void gravaLeDados() throws IOException {
					
		//Gravar dados
		PersistorPadroes persistorPadroes = new PersistorPadroesJSon();
		persistorPadroes.persistir(padroes, arquivo);
		Assert.assertTrue(arquivo.exists());

		//Ler Dados
		LeitorPadroes leitorPadroes = new LeitorPadroesJson();
		List<Padrao> c = new LinkedList<Padrao>();
		leitorPadroes.lerDados(arquivo, c);
		double[] esperados;
		double[] atuais;
		for (int i = 0; i < padroes.size(); i++) {
			esperados = padroes.get(i).getCaracteristicas();
			atuais = c.get(i).getCaracteristicas();
			for (int j = 0; j < esperados.length; j++) {
				Assert.assertEquals(esperados[j], atuais[j], 0.0);
			}
			Assert.assertEquals(padroes.get(i).getClasse(), c.get(i).getClasse());
		}
			
			
		
	}
	
	
	@After
	public void limparTeste(){
		arquivo.delete();
	}
	
	public static void main(String[] args) throws IOException {
		PadroesJsonTest t = new PadroesJsonTest();
		t.preparaPadroes();
		t.gravaLeDados();
		t.limparTeste();
	}
	
}
