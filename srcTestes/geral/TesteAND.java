package geral;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import jfan.fan.MonitorFAN;
import jfan.fan.conjuntos.GerenciadorConjuntos;
import jfan.fan.exceptions.NumeroCarateristicasIncompativelException;
import jfan.fan.exceptions.PadraoNaoNormalizadoException;
import jfan.fan.fuzzy.FabricaFuncaoPertinencia;
import jfan.fan.fuzzy.FabricaFuncaoPertinenciaPadrao;
import jfan.fan.fuzzy.FuncaoPertinencia;
import jfan.fan.fuzzy.FuncoesPertinenciaEnum;
import jfan.fan.normalizadores.FabricaNormalizador;
import jfan.fan.normalizadores.FabricaNormalizadorPadrao;
import jfan.fan.normalizadores.Normalizador;
import jfan.fan.normalizadores.NormalizadorConfig;
import jfan.fan.normalizadores.NormalizadoresEnum;
import jfan.fan.padroes.Padrao;
import jfan.fan.padroes.PadraoFAN;
import jfan.io.LeitorMonitor;
import jfan.io.LeitorRede;
import jfan.io.PersistorMonitor;
import jfan.io.PersistorRede;
import jfan.io.bin.LeitorMonitorBinario;
import jfan.io.bin.LeitorRedeBinario;
import jfan.io.bin.PersistorMonitorBinario;
import jfan.io.bin.PersistorRedeBinario;
import jfan.io.json.LeitorMonitorJson;
import jfan.io.json.LeitorRedeJson;
import jfan.io.json.PersistorMonitorJson;
import jfan.io.json.PersistorRedeJson;

import org.junit.Before;
import org.junit.Test;


public class TesteAND {
	
	//GerenciadorConjuntos gerenciadorConjuntos = new GerenciadorConjuntos();
	
	GerenciadorConjuntos conjuntos;
	
	NormalizadorConfig configNorm;
	FabricaNormalizador fabricaNormalizador;
	Normalizador normalizador;
	FabricaFuncaoPertinencia fabricaFuncaoPertinencia;
	FuncaoPertinencia funcaoPertinencia;
	
	LinkedList<Padrao> padroes = new LinkedList<Padrao>();
	
	int raio = 6;
	int suporte = 100;
	
	
	@Before
	public void carregarPadroes() {
		//0 0 = 0
		Padrao p1 = new Padrao(new double[]{5.0,5.0},"0");
		//0 1 = 0
		Padrao p2 = new Padrao(new double[]{5.0,10.0},"0");
		//1 0 = 0
		Padrao p3 = new Padrao(new double[]{10.0,5.0},"0");
		//1 1 = 1
		Padrao p4 = new Padrao(new double[]{10.0,10.0},"1");
		
		padroes.clear();
		padroes.add(p1);
		padroes.add(p2);
		padroes.add(p3);
		padroes.add(p4);
				
		
		configNorm = NormalizadorConfig.getNormalizadorConfiguration(padroes);
		
		fabricaNormalizador = new FabricaNormalizadorPadrao(configNorm);
			
		fabricaFuncaoPertinencia = new FabricaFuncaoPertinenciaPadrao();
		
	}
	
	@Test
	public void testPadroes() {
		//0 0 = 0
		Padrao p1 = padroes.get(0);
		//0 1 = 0
		Padrao p2 = padroes.get(1);
		//1 0 = 0
		Padrao p3 = padroes.get(2);
		//1 1 = 1
		Padrao p4 = padroes.get(3);
		
		//Teste dos zeros
		assertEquals(5.0, p1.getCaracteristica(0), 0.0000001);
		assertEquals(5.0, p1.getCaracteristica(1), 0.0000001);
		assertEquals(5.0, p2.getCaracteristica(0), 0.0000001);
		assertEquals(5.0, p3.getCaracteristica(1), 0.0000001);
		//Teste dos uns
		assertEquals(10.0, p2.getCaracteristica(1), 0.0000001);
		assertEquals(10.0, p3.getCaracteristica(0), 0.0000001);
		assertEquals(10.0, p4.getCaracteristica(0), 0.0000001);
		assertEquals(10.0, p4.getCaracteristica(1), 0.0000001);
		
		padroes.add(p1);
		padroes.add(p2);
		padroes.add(p3);
		padroes.add(p4);
	}
	
	@Test
	public void testNormalizacaoConfig() {
				
		//minimos
		assertEquals(5.0, configNorm.getMinimos()[0], 0.0000001);
		assertEquals(5.0, configNorm.getMinimos()[1], 0.0000001);
		
		//maximos
		assertEquals(10.0, configNorm.getMaximos()[0], 0.0000001);
		assertEquals(10.0, configNorm.getMaximos()[1], 0.0000001);
		
		//medias
		assertEquals(7.5, configNorm.getMedias()[0], 0.0000001);
		assertEquals(7.5, configNorm.getMedias()[1], 0.0000001);
		
	}
	
	@Test
	public void testNormalizadorMax() {
		
		normalizador = fabricaNormalizador.criarNormalizador(NormalizadoresEnum.MAX.getID());
		//0 0 = 0
		Padrao p1 = padroes.get(0);
		//0 1 = 0
		Padrao p2 = padroes.get(1);
		//1 0 = 0
		Padrao p3 = padroes.get(2);
		//1 1 = 1
		Padrao p4 = padroes.get(3);
		
		//dados normalizados
		normalizador.normalizar(padroes);
		
		
		//Teste dos zeros
		assertEquals(0.5, p1.getCaracteristica(0), 0.0000001);
		assertEquals(0.5, p1.getCaracteristica(1), 0.0000001);
		assertEquals(0.5, p2.getCaracteristica(0), 0.0000001);
		assertEquals(0.5, p3.getCaracteristica(1), 0.0000001);
		
		//Teste dos uns
		assertEquals(1.0, p2.getCaracteristica(1), 0.0000001);
		assertEquals(1.0, p3.getCaracteristica(0), 0.0000001);
		assertEquals(1.0, p4.getCaracteristica(0), 0.0000001);
		assertEquals(1.0, p4.getCaracteristica(1), 0.0000001);
	}
	
	@Test
	public void testNormalizadorMaxMin() {
		
		normalizador = fabricaNormalizador.criarNormalizador(NormalizadoresEnum.MAXMIN.getID());
		//0 0 = 0
		Padrao p1 = padroes.get(0);
		//0 1 = 0
		Padrao p2 = padroes.get(1);
		//1 0 = 0
		Padrao p3 = padroes.get(2);
		//1 1 = 1
		Padrao p4 = padroes.get(3);
		
		//dados normalizados
		normalizador.normalizar(padroes);
		
		
		//Teste dos zeros
		assertEquals(0.0, p1.getCaracteristica(0), 0.0000001);
		assertEquals(0.0, p1.getCaracteristica(1), 0.0000001);
		assertEquals(0.0, p2.getCaracteristica(0), 0.0000001);
		assertEquals(0.0, p3.getCaracteristica(1), 0.0000001);
		
		//Teste dos uns
		assertEquals(1.0, p2.getCaracteristica(1), 0.0000001);
		assertEquals(1.0, p3.getCaracteristica(0), 0.0000001);
		assertEquals(1.0, p4.getCaracteristica(0), 0.0000001);
		assertEquals(1.0, p4.getCaracteristica(1), 0.0000001);
	}
	
	@Test
	public void testNormalizadorMaxMean() {
		
		normalizador = fabricaNormalizador.criarNormalizador(NormalizadoresEnum.MAXMEAN.getID());
		//0 0 = 0
		Padrao p1 = padroes.get(0);
		//0 1 = 0
		Padrao p2 = padroes.get(1);
		//1 0 = 0
		Padrao p3 = padroes.get(2);
		//1 1 = 1
		Padrao p4 = padroes.get(3);
		
		//dados normalizados
		normalizador.normalizar(padroes);
		
		
		//Teste dos zeros
		assertEquals(0.0, p1.getCaracteristica(0), 0.0000001);
		assertEquals(0.0, p1.getCaracteristica(1), 0.0000001);
		assertEquals(0.0, p2.getCaracteristica(0), 0.0000001);
		assertEquals(0.0, p3.getCaracteristica(1), 0.0000001);
		
		//Teste dos uns
		assertEquals(1.0, p2.getCaracteristica(1), 0.0000001);
		assertEquals(1.0, p3.getCaracteristica(0), 0.0000001);
		assertEquals(1.0, p4.getCaracteristica(0), 0.0000001);
		assertEquals(1.0, p4.getCaracteristica(1), 0.0000001);
	}
	
	
	@Test
	public void testTreinamento() throws PadraoNaoNormalizadoException, IOException, NumeroCarateristicasIncompativelException {
		
		normalizador = fabricaNormalizador.criarNormalizador(NormalizadoresEnum.MAX.getID());
		funcaoPertinencia = fabricaFuncaoPertinencia.criarFuncaoPertinencia(FuncoesPertinenciaEnum.TRIANGULAR.getID());
		
		LinkedList<Padrao> padraoNaoNormalizado = new LinkedList<Padrao>();
		
		for (Padrao padrao : padroes) {
			padraoNaoNormalizado.add(padrao.clone());
		}
		
		//dados normalizados
		normalizador.normalizar(padroes);
				
		conjuntos = new GerenciadorConjuntos();
		conjuntos.setRaioDifuso(raio);
		conjuntos.setFuncaoPertinencia(funcaoPertinencia);
		conjuntos.setSuporteConjuntosDifusos(suporte);
		
		//LinkedList<PadraoFAN> padroesFAN = new LinkedList<PadraoFAN>();
		
		//for(Padrao p : padroes) {
			//padroesFAN.add(p.createPadraoFAN(raio, suporte, funcaoPertinencia));
		//}
		conjuntos.addPadroesConjuntoTreinamento(padroes);
		
		MonitorFAN monitor = new MonitorFAN(raio,suporte,2,2);
		monitor.setNormalizador(normalizador);
		
		//RedeFAN redeFAN = new RedeFAN(raio,suporte,2,2);
		
		for (int i = 0; i < 100; i++) {
			//redeFAN.treinar(padroesFAN);
			monitor.treinar(conjuntos.getConjuntoTreinamento(),conjuntos.getConjuntoTreinamento());
		}

		//Grava monitor em Json
		PersistorMonitor persMonitor = new PersistorMonitorJson();
		persMonitor.persistir(monitor, new File(".\\srcTestes\\arquivos\\monitor.json"));
		
		//Grava monitor em Binário
		persMonitor = new PersistorMonitorBinario();
		persMonitor.persistir(monitor, new File(".\\srcTestes\\arquivos\\monitor.jfan"));
		
		double acertoPreLoadMA = monitor.getMelhorAritmetica();
		double acertoPreLoadMH = monitor.getMelhorHarmonica();
		double acertoPreLoadMM = monitor.getMelhorMaxMin();
				
			
		//não deve haver erros
		//int[][] erros = redeFAN.testar(padroesFAN);
		int[][] erros = monitor.testar(conjuntos.getConjuntoTreinamento()).getMatrizConfusao();
		
		//Valida os acertos
		assertEquals("Erro na tabela de erros",erros[0][0], 3);
		assertEquals("Erro na tabela de erros",erros[0][1], 0);
		assertEquals("Erro na tabela de erros",erros[1][0], 1);
		assertEquals("Erro na tabela de erros",erros[1][1], 0);
		
		
		//fazer teste de salvar rede e recuperar.
		PersistorRede persistor = new PersistorRedeBinario();
		PersistorRede persistorJson = new PersistorRedeJson();
		LeitorRede leitor = new LeitorRedeBinario();
		LeitorRede leitorJson = new LeitorRedeJson();
		
		persistor.persistir(monitor.getRedeMelhorAritmetica(), monitor.getNormalizador(), new File(".\\srcTestes\\arquivos\\testeMA.jfan2"));
		persistor.persistir(monitor.getRedeMelhorHarmonica(), monitor.getNormalizador(),  new File(".\\srcTestes\\arquivos\\testeMH.jfan2"));
		persistor.persistir(monitor.getRedeMelhorMaxMin(), monitor.getNormalizador(),  new File(".\\srcTestes\\arquivos\\testeMM.jfan2"));
		
		persistorJson.persistir(monitor.getRedeMelhorAritmetica(),  monitor.getNormalizador(),  new File(".\\srcTestes\\arquivos\\testeMA.json"));
		persistorJson.persistir(monitor.getRedeMelhorHarmonica(),  monitor.getNormalizador(),  new File(".\\srcTestes\\arquivos\\testeMH.json"));
		persistorJson.persistir(monitor.getRedeMelhorMaxMin(),  monitor.getNormalizador(),  new File(".\\srcTestes\\arquivos\\testeMM.json"));
		
				
		
		//Aritmetica - Bin
		monitor = null;
		
		monitor = leitor.ler(new File(".\\srcTestes\\arquivos\\testeMA.jfan2"));
			
		//renormalizar padrões baseado no normalizador carregado.
		LinkedList<Padrao> padroesTemp = new LinkedList<Padrao>();
		for (Padrao padrao : padraoNaoNormalizado) {
			padroesTemp.add(padrao.clone());
		}
		monitor.getNormalizador().normalizar(padroesTemp);
		
		conjuntos = new GerenciadorConjuntos();
		conjuntos.setRaioDifuso(raio);
		conjuntos.setFuncaoPertinencia(funcaoPertinencia);
		conjuntos.setSuporteConjuntosDifusos(suporte);

		conjuntos.addPadroesConjuntoTreinamento(padroes);
		
		
		erros = monitor.testar(conjuntos.getConjuntoTreinamento()).getMatrizConfusao();
		//Valida os acertos
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
		
		assertEquals("Erro de percentual de acerto",acertoPreLoadMA, monitor.getMelhorAritmetica(), 0.000001);
		
			
		//Zera as classes
		for (PadraoFAN p : conjuntos.getConjuntoTreinamento()) {
			p.setClasse(-1);
		}
		
		monitor.classificar(conjuntos.getConjuntoTreinamento());
			
		
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(0).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(1).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(2).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(3).getClasse()),"1");
		
		
		
		//Aritmetica - Json
		monitor = null;
		monitor = leitorJson.ler(new File(".\\srcTestes\\arquivos\\testeMA.json"));
		
		//renormalizar padrões baseado no normalizador carregado.
		padroesTemp = new LinkedList<Padrao>();
		for (Padrao padrao : padraoNaoNormalizado) {
			padroesTemp.add(padrao.clone());
		}
		monitor.getNormalizador().normalizar(padroesTemp);
		
		
		conjuntos = new GerenciadorConjuntos();
		conjuntos.setRaioDifuso(raio);
		conjuntos.setFuncaoPertinencia(funcaoPertinencia);
		conjuntos.setSuporteConjuntosDifusos(suporte);

		conjuntos.addPadroesConjuntoTreinamento(padroes);
		
		
		erros = monitor.testar(conjuntos.getConjuntoTreinamento()).getMatrizConfusao();
		//Valida os acertos
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
		
		assertEquals("Erro de percentual de acerto",acertoPreLoadMA, monitor.getMelhorAritmetica(), 0.000001);
		
			
		//Zera as classes
		for (PadraoFAN p : conjuntos.getConjuntoTreinamento()) {
			p.setClasse(-1);
		}
		
		monitor.classificar(conjuntos.getConjuntoTreinamento());
				
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(0).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(1).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(2).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(3).getClasse()),"1");
		
		//---------------------------------------------------------------------------
		
		
		//Harmonica - Bin
		monitor = null;
		monitor = leitor.ler(new File(".\\srcTestes\\arquivos\\testeMH.jfan2"));
		
		//renormalizar padrões baseado no normalizador carregado.
		padroesTemp = new LinkedList<Padrao>();
		for (Padrao padrao : padraoNaoNormalizado) {
			padroesTemp.add(padrao.clone());
		}
		monitor.getNormalizador().normalizar(padroesTemp);
		
		
		conjuntos = new GerenciadorConjuntos();
		conjuntos.setRaioDifuso(raio);
		conjuntos.setFuncaoPertinencia(funcaoPertinencia);
		conjuntos.setSuporteConjuntosDifusos(suporte);

		conjuntos.addPadroesConjuntoTreinamento(padroes);
		
		erros = monitor.testar(conjuntos.getConjuntoTreinamento()).getMatrizConfusao();
		assertEquals("Erro de percentual de acerto",acertoPreLoadMH, monitor.getMelhorHarmonica(), 0.000001);
		//Valida os acertos
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
					
		//Zera as classes
		for (PadraoFAN p : conjuntos.getConjuntoTreinamento()) {
			p.setClasse(-1);
		}
		
		monitor.classificar(conjuntos.getConjuntoTreinamento());
		
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(0).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(1).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(2).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(3).getClasse()),"1");
		
		
		//Harmonica - Json
		monitor = null;
		monitor = leitorJson.ler(new File(".\\srcTestes\\arquivos\\testeMH.json"));
		
		//renormalizar padrões baseado no normalizador carregado.
		padroesTemp = new LinkedList<Padrao>();
		for (Padrao padrao : padraoNaoNormalizado) {
			padroesTemp.add(padrao.clone());
		}
		monitor.getNormalizador().normalizar(padroesTemp);
		conjuntos = new GerenciadorConjuntos();
		conjuntos.setRaioDifuso(raio);
		conjuntos.setFuncaoPertinencia(funcaoPertinencia);
		conjuntos.setSuporteConjuntosDifusos(suporte);

		conjuntos.addPadroesConjuntoTreinamento(padroes);
		
		erros = monitor.testar(conjuntos.getConjuntoTreinamento()).getMatrizConfusao();
		assertEquals("Erro de percentual de acerto",acertoPreLoadMH, monitor.getMelhorHarmonica(), 0.000001);
		//Valida os acertos
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
					
		//Zera as classes
		for (PadraoFAN p : conjuntos.getConjuntoTreinamento()) {
			p.setClasse(-1);
		}
		
		monitor.classificar(conjuntos.getConjuntoTreinamento());
		
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(0).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(1).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(2).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(3).getClasse()),"1");
		
		
		//---------------------------------------------------------------------------
		
		//Max Min - Bin
		monitor = null;
		monitor = leitor.ler(new File(".\\srcTestes\\arquivos\\testeMM.jfan2"));
		
		//renormalizar padrões baseado no normalizador carregado.
		padroesTemp = new LinkedList<Padrao>();
		for (Padrao padrao : padraoNaoNormalizado) {
			padroesTemp.add(padrao.clone());
		}
		monitor.getNormalizador().normalizar(padroesTemp);
		
		conjuntos = new GerenciadorConjuntos();
		conjuntos.setRaioDifuso(raio);
		conjuntos.setFuncaoPertinencia(funcaoPertinencia);
		conjuntos.setSuporteConjuntosDifusos(suporte);

		conjuntos.addPadroesConjuntoTreinamento(padroes);

		
		erros = monitor.testar(conjuntos.getConjuntoTreinamento()).getMatrizConfusao();
		assertEquals("Erro de percentual de acerto",acertoPreLoadMM, monitor.getMelhorMaxMin(), 0.000001);
		//Valida os acertos
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
					
		//Zera as classes
		for (PadraoFAN p : conjuntos.getConjuntoTreinamento()) {
			p.setClasse(-1);
		}
		
		monitor.classificar(conjuntos.getConjuntoTreinamento());
		
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(0).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(1).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(2).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(3).getClasse()),"1");
		
		//Max Min - json
		monitor = null;
		monitor = leitorJson.ler(new File(".\\srcTestes\\arquivos\\testeMM.json"));
		
		//renormalizar padrões baseado no normalizador carregado.
		padroesTemp = new LinkedList<Padrao>();
		for (Padrao padrao : padraoNaoNormalizado) {
			padroesTemp.add(padrao.clone());
		}
		monitor.getNormalizador().normalizar(padroesTemp);
		
		conjuntos = new GerenciadorConjuntos();
		conjuntos.setRaioDifuso(raio);
		conjuntos.setFuncaoPertinencia(funcaoPertinencia);
		conjuntos.setSuporteConjuntosDifusos(suporte);

		conjuntos.addPadroesConjuntoTreinamento(padroes);

		
		erros = monitor.testar(conjuntos.getConjuntoTreinamento()).getMatrizConfusao();
		assertEquals("Erro de percentual de acerto",acertoPreLoadMM, monitor.getMelhorMaxMin(), 0.000001);
		//Valida os acertos
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
		assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
		assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
					
		//Zera as classes
		for (PadraoFAN p : conjuntos.getConjuntoTreinamento()) {
			p.setClasse(-1);
		}
		
		monitor.classificar(conjuntos.getConjuntoTreinamento());
		
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(0).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(1).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(2).getClasse()),"0");
		assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(3).getClasse()),"1");		

		//testCarregaMonitorFANBin(acertoPreLoadMA, acertoPreLoadMH, acertoPreLoadMM);
		testCarregaMonitorFANJson(acertoPreLoadMA, acertoPreLoadMH, acertoPreLoadMM);
		
	}
	
	
	public void testCarregaMonitorFANJson(double acertoPreLoadMA, double acertoPreLoadMH, double acertoPreLoadMM) throws PadraoNaoNormalizadoException, IOException, NumeroCarateristicasIncompativelException{
		
		//------------------------------------------------------
		// Testar tudo novamente restaurando o Monitor inteiro JSON
		//------------------------------------------------------
		carregarPadroes();
		LinkedList<Padrao> padraoNaoNormalizado = new LinkedList<Padrao>();
		LinkedList<Padrao> padroesTemp = new LinkedList<Padrao>();
		
		//LinkedList<PadraoFAN> padroesFAN = new LinkedList<PadraoFAN>();
		
		//for(Padrao p : padroes) {
			//padroesFAN.add(p.createPadraoFAN(raio, suporte, funcaoPertinencia));
		//}
		
		for (Padrao padrao : padroes) {
			padraoNaoNormalizado.add(padrao.clone());
		}
		
		conjuntos = new GerenciadorConjuntos();
		conjuntos.setRaioDifuso(raio);
		conjuntos.setFuncaoPertinencia(funcaoPertinencia);
		conjuntos.setSuporteConjuntosDifusos(suporte);
		
		LeitorMonitor leitorMonitor = new LeitorMonitorJson();
		MonitorFAN monitor = null;
		monitor = leitorMonitor.ler(new File(".\\srcTestes\\arquivos\\monitor.json"));

			//Aritmetica - Json
			//renormalizar padrões baseado no normalizador carregado.
			padroesTemp = new LinkedList<Padrao>();
			for (Padrao padrao : padraoNaoNormalizado) {
				padroesTemp.add(padrao.clone());
			}
			monitor.getNormalizador().normalizar(padroesTemp);
			
			conjuntos.addPadroesConjuntoTreinamento(padroesTemp);
			
			//int[][] erros = monitor.testarRedeMelhorAritmetica(conjuntos.getConjuntoTreinamento()).getMatrizConfusao();
			//Valida os acertos
			//assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
			//assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
			//assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
			//assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
			
			assertEquals("Erro de percentual de acerto",acertoPreLoadMA, monitor.getMelhorAritmetica(), 0.000001);
			
				
			//Zera as classes
			for (PadraoFAN p : conjuntos.getConjuntoTreinamento()) {
				p.setClasse(-1);
			}
			
			monitor.classificarRedeMelhorAritmetica(conjuntos.getConjuntoTreinamento());
			
			assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(0).getClasse()),"0");
			assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(1).getClasse()),"0");
			assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(2).getClasse()),"0");
			assertEquals("Erro de classificação",conjuntos.getMapaInternoParaInteiroReal().get(conjuntos.getConjuntoTreinamento().get(3).getClasse()),"1");		
			
			
			//---------------------------------------------------------------------------
			/*
			//Harmonica - Json
						
			//renormalizar padrões baseado no normalizador carregado.
			padroesTemp = new LinkedList<Padrao>();
			for (Padrao padrao : padraoNaoNormalizado) {
				padroesTemp.add(padrao.clone());
			}
			monitor.getNormalizador().normalizar(padroesTemp);
			padroesFANTemp = new LinkedList<PadraoFAN>();
			
			for(Padrao p : padroesTemp) {
				padroesFANTemp.add(p.createPadraoFAN(raio, suporte, funcaoPertinencia));
			}
			
			erros = monitor.testarRedeMelhorHarmonica(padroesFAN).getMatrizConfusao();
			assertEquals("Erro de percentual de acerto",acertoPreLoadMH, monitor.getMelhorHarmonica(), 0.000001);
			//Valida os acertos
			assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
			assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
			assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
			assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
						
			//Zera as classes
			for (PadraoFAN p : padroesFAN) {
				p.setClasse(-1);
			}
			
			monitor.classificarRedeMelhorHarmonica(padroesFAN);
			
			assertEquals("Erro de classificação",padroesFAN.get(0).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(1).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(2).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(3).getClasse(),1);
			
			//Max Min - json
			
			//renormalizar padrões baseado no normalizador carregado.
			padroesTemp = new LinkedList<Padrao>();
			for (Padrao padrao : padraoNaoNormalizado) {
				padroesTemp.add(padrao.clone());
			}
			monitor.getNormalizador().normalizar(padroesTemp);
			padroesFANTemp = new LinkedList<PadraoFAN>();
			
			for(Padrao p : padroesTemp) {
				padroesFANTemp.add(p.createPadraoFAN(raio, suporte, funcaoPertinencia));
			}
			
			erros = monitor.testarRedeMelhorMaxMin(padroesFAN).getMatrizConfusao();
			assertEquals("Erro de percentual de acerto",acertoPreLoadMM, monitor.getMelhorMaxMin(), 0.000001);
			//Valida os acertos
			assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
			assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
			assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
			assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
						
			//Zera as classes
			for (PadraoFAN p : padroesFAN) {
				p.setClasse(-1);
			}
			
			monitor.classificarRedeMelhorMaxMin(padroesFAN);
			
			assertEquals("Erro de classificação",padroesFAN.get(0).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(1).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(2).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(3).getClasse(),1);
		
		
		//------------------------------------------------------
		// Fim ** Testar tudo novamente restaurando o Monitor inteiro JSON
		//------------------------------------------------------
		 
		 */
	}
	
public void testCarregaMonitorFANBin(double acertoPreLoadMA, double acertoPreLoadMH, double acertoPreLoadMM) throws PadraoNaoNormalizadoException, IOException{
		
		//------------------------------------------------------
		// Testar tudo novamente restaurando o Monitor inteiro JSON
		//------------------------------------------------------
		
		LinkedList<Padrao> padraoNaoNormalizado = new LinkedList<Padrao>();
		LinkedList<PadraoFAN> padroesFANTemp = new LinkedList<PadraoFAN>();
		LinkedList<PadraoFAN> padroesFAN = new LinkedList<PadraoFAN>();
		
		for(Padrao p : padroes) {
			padroesFAN.add(p.createPadraoFAN(raio, suporte, funcaoPertinencia));
		}
		
		for (Padrao padrao : padroes) {
			padraoNaoNormalizado.add(padrao.clone());
		}
		
		LinkedList<Padrao> padroesTemp = new LinkedList<Padrao>();
		
		LeitorMonitor leitorMonitor = new LeitorMonitorBinario();
		MonitorFAN monitor = null;
		monitor = leitorMonitor.ler(new File("/home/filipe/monitor.json"));

			//Aritmetica - Json
			//renormalizar padrões baseado no normalizador carregado.
			padroesTemp = new LinkedList<Padrao>();
			for (Padrao padrao : padraoNaoNormalizado) {
				padroesTemp.add(padrao.clone());
			}
			monitor.getNormalizador().normalizar(padroesTemp);
			padroesFANTemp = new LinkedList<PadraoFAN>();
			
			for(Padrao p : padroesTemp) {
				padroesFANTemp.add(p.createPadraoFAN(raio, suporte, funcaoPertinencia));
			}
			
			int[][] erros = monitor.testarRedeMelhorAritmetica(padroesFAN).getMatrizConfusao();
			//Valida os acertos
			assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
			assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
			assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
			assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
			
			assertEquals("Erro de percentual de acerto",acertoPreLoadMA, monitor.getMelhorAritmetica(), 0.000001);
			
				
			//Zera as classes
			for (PadraoFAN p : padroesFAN) {
				p.setClasse(-1);
			}
			
			monitor.classificarRedeMelhorAritmetica(padroesFAN);
			
			assertEquals("Erro de classificação",padroesFAN.get(0).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(1).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(2).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(3).getClasse(),1);
			
			//---------------------------------------------------------------------------
			
			//Harmonica - Json
						
			//renormalizar padrões baseado no normalizador carregado.
			padroesTemp = new LinkedList<Padrao>();
			for (Padrao padrao : padraoNaoNormalizado) {
				padroesTemp.add(padrao.clone());
			}
			monitor.getNormalizador().normalizar(padroesTemp);
			padroesFANTemp = new LinkedList<PadraoFAN>();
			
			for(Padrao p : padroesTemp) {
				padroesFANTemp.add(p.createPadraoFAN(raio, suporte, funcaoPertinencia));
			}
			
			erros = monitor.testarRedeMelhorHarmonica(padroesFAN).getMatrizConfusao();
			assertEquals("Erro de percentual de acerto",acertoPreLoadMH, monitor.getMelhorHarmonica(), 0.000001);
			//Valida os acertos
			assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
			assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
			assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
			assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
						
			//Zera as classes
			for (PadraoFAN p : padroesFAN) {
				p.setClasse(-1);
			}
			
			monitor.classificarRedeMelhorHarmonica(padroesFAN);
			
			assertEquals("Erro de classificação",padroesFAN.get(0).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(1).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(2).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(3).getClasse(),1);
			
			//Max Min - json
			
			//renormalizar padrões baseado no normalizador carregado.
			padroesTemp = new LinkedList<Padrao>();
			for (Padrao padrao : padraoNaoNormalizado) {
				padroesTemp.add(padrao.clone());
			}
			monitor.getNormalizador().normalizar(padroesTemp);
			padroesFANTemp = new LinkedList<PadraoFAN>();
			
			for(Padrao p : padroesTemp) {
				padroesFANTemp.add(p.createPadraoFAN(raio, suporte, funcaoPertinencia));
			}
			
			erros = monitor.testarRedeMelhorMaxMin(padroesFAN).getMatrizConfusao();
			assertEquals("Erro de percentual de acerto",acertoPreLoadMM, monitor.getMelhorMaxMin(), 0.000001);
			//Valida os acertos
			assertEquals("Erro na tabela de erros, pós leitura",erros[0][0], 3);
			assertEquals("Erro na tabela de erros, pós leitura",erros[0][1], 0);
			assertEquals("Erro na tabela de erros, pós leitura",erros[1][0], 1);
			assertEquals("Erro na tabela de erros, pós leitura",erros[1][1], 0);
						
			//Zera as classes
			for (PadraoFAN p : padroesFAN) {
				p.setClasse(-1);
			}
			
			monitor.classificarRedeMelhorMaxMin(padroesFAN);
			
			assertEquals("Erro de classificação",padroesFAN.get(0).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(1).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(2).getClasse(),0);
			assertEquals("Erro de classificação",padroesFAN.get(3).getClasse(),1);
		
		
		//------------------------------------------------------
		// Fim ** Testar tudo novamente restaurando o Monitor inteiro JSON
		//------------------------------------------------------
	}
	

public static void main(String[] args) {
	TesteAND t = new TesteAND();
	try {
		t.carregarPadroes();
		t.testTreinamento();
	} catch (PadraoNaoNormalizadoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NumeroCarateristicasIncompativelException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


}
