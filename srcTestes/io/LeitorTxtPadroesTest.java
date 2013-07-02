package io;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jfan.fan.padroes.Padrao;
import jfan.io.LeitorPadroes;
import jfan.io.txt.LeitorPadroesArquivoTexto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class LeitorTxtPadroesTest {

	LeitorPadroes leitor;
	
	@Before
	public void setUp() throws Exception {
		leitor = new LeitorPadroesArquivoTexto(";");
		System.out.println((new File(".").getAbsolutePath()) );
	}

	@Test
	public void testLerDados2() {
		try {
			ArrayList<Padrao> padroes = new ArrayList<Padrao>();
			String fileSeparator = System.getProperty("file.separator");
			
			System.out.println(fileSeparator);
		
			leitor.lerDados(new File("."+fileSeparator+"srcTestes"+fileSeparator+"arquivos"+fileSeparator+"xor.txt"),padroes);
			
			//Padrao 1 - 0 0 = 0
			Assert.assertEquals(0.0, padroes.get(0).getCaracteristica(0), 0.000001);
			Assert.assertEquals(0.0, padroes.get(0).getCaracteristica(1), 0.000001);
			Assert.assertEquals("0", padroes.get(0).getClasse());
			
			//Padrao 2 - 0 1 = 1
			Assert.assertEquals(0.0, padroes.get(1).getCaracteristica(0), 0.000001);
			Assert.assertEquals(1.0, padroes.get(1).getCaracteristica(1), 0.000001);
			Assert.assertEquals("1", padroes.get(1).getClasse());
			
			//Padrao 3 - 1 0 = 1
			Assert.assertEquals(1.0, padroes.get(2).getCaracteristica(0), 0.000001);
			Assert.assertEquals(0.0, padroes.get(2).getCaracteristica(1), 0.000001);
			Assert.assertEquals("1", padroes.get(2).getClasse());
			
			//Padrao 4 - 1 1 = 0
			Assert.assertEquals(1.0, padroes.get(3).getCaracteristica(0), 0.000001);
			Assert.assertEquals(1.0, padroes.get(3).getCaracteristica(1), 0.000001);
			Assert.assertEquals("0", padroes.get(3).getClasse());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
		}
		
	}

}
