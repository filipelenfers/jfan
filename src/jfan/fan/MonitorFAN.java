package jfan.fan;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jfan.fan.eventos.MonitorListener;
import jfan.fan.eventos.RedeSuperadaEvent;
import jfan.fan.exceptions.PadraoNaoNormalizadoException;
import jfan.fan.fuzzy.FuncaoPertinencia;
import jfan.fan.fuzzy.FuncaoPertinenciaTriangular;
import jfan.fan.normalizadores.FabricaNormalizador;
import jfan.fan.normalizadores.FabricaNormalizadorPadrao;
import jfan.fan.normalizadores.Normalizador;
import jfan.fan.normalizadores.NormalizadorConfig;
import jfan.fan.normalizadores.NormalizadoresEnum;
import jfan.fan.padroes.Padrao;
import jfan.fan.padroes.PadraoFAN;
import jfan.fan.temperas.ITemperaSimulada;

public class MonitorFAN implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8408609482590720532L;

	protected RedeFAN redeAtual;
	
	protected double aritmetica;
	protected double melhorAritmetica;
	protected RedeFAN redeMelhorAritmetica;
	protected transient RedeSuperadaEvent eventoRedeAritmeticaSuperada;

	protected double harmonica;
	protected double melhorHarmonica;
	protected RedeFAN redeMelhorHarmonica;
	protected transient RedeSuperadaEvent eventoRedeHarmonicaSuperada;
	
	protected double maxMin;
	protected double melhorMaxMin;
	protected RedeFAN redeMelhorMaxMin;
	protected transient RedeSuperadaEvent eventoRedeMaxMinSuperada;
	
	protected transient MonitorListener monitorListener;
	
	protected Normalizador normalizador;
	
	protected long epoca = 0;
	
	protected int epocasStepTempera = 0;
	protected transient int epocasAcumuladasStepTempera = 0;
	
	protected int epocasReiniciarTempera = 0;
	protected transient int epocasAcumuladasReinicarTempera = 0;
	
	protected int epocasEmbaralharConjuntoTreinamento = 0;
	protected transient int epocasAcumuladasEmbaralharConjuntoTreinamento= 0;
	
	
	
	public Normalizador getNormalizador() {
		return normalizador;
	}

	public void setNormalizador(Normalizador normalizador) {
		this.normalizador = normalizador;
	}

	public MonitorFAN(int raio, int suporte, int numCarateristicas, int numClasses) 
	{
		redeAtual = new RedeFAN(raio,suporte, numCarateristicas, numClasses);
		//reiniciarMonitor(raio, suporte, numCarateristicas, numClasses);
		reiniciarMonitor();
	}
	
	public MonitorFAN(RedeFAN rede) {
		redeAtual = rede;
		
		reiniciarMonitor();
	}

	//int raio, int suporte, int numCarateristicas, int numClasses
	public void reiniciarMonitor() {
		
		
		redeMelhorAritmetica = null;
		redeMelhorHarmonica = null;
		redeMelhorMaxMin = null;
		
		melhorAritmetica = melhorHarmonica = melhorMaxMin = 0.0;
		
		eventoRedeAritmeticaSuperada = eventoRedeHarmonicaSuperada = eventoRedeMaxMinSuperada = null;
		
		
	}
	
	/*
	 * Funções rede
	 */
	
	public Resultado treinar(List<PadraoFAN> conjuntoTreinamento, Collection<PadraoFAN> conjuntoTeste) {
		
		epoca++;
		epocasAcumuladasStepTempera++;
		epocasAcumuladasReinicarTempera++;
		epocasAcumuladasEmbaralharConjuntoTreinamento++;
		
		//Valida o embaralhar
		if (epocasAcumuladasEmbaralharConjuntoTreinamento == epocasEmbaralharConjuntoTreinamento) {
			Collections.shuffle(conjuntoTreinamento);
			epocasAcumuladasEmbaralharConjuntoTreinamento = 0;
		}
		
		//Validar step de tempera
		if (epocasAcumuladasStepTempera == epocasStepTempera) {
		
			if (redeAtual.getTemperaSimulada() != null) {
				redeAtual.getTemperaSimulada().step();
			}
			
			epocasAcumuladasStepTempera = 0;
		}
		
		//Validar reset de tempera 
		if (epocasAcumuladasReinicarTempera == epocasReiniciarTempera) {
			
			if (redeAtual.getTemperaSimulada() != null) {
				redeAtual.getTemperaSimulada().reset();
			}
			
			epocasAcumuladasReinicarTempera = 0;
		}
		
		Resultado resultado = new Resultado();
		redeAtual.treinar(conjuntoTreinamento);
		int[][] matrizConfusao = redeAtual.testar(conjuntoTeste);
		
		int[] totalClasses = calcularTotalClasses(matrizConfusao);
		double[] acertoClasses = calcularAcertoAritmeticoClasses(matrizConfusao, totalClasses);
		resultado.setAcertosAritmeticosClasses(acertoClasses);
		
		resultado.setAcertoArimetico(calcularAcertoAritmetico(acertoClasses));
		resultado.setAcertoHarmonico(calcularAcertoHarmonico(acertoClasses, totalClasses));
		resultado.setAcertoMaxMin(calcularAcertoMaxMin(acertoClasses));
		
		resultado.setMatrizConfusao(matrizConfusao);
		
		//verificar se quebrou recorde e disparar eventos
		verificarRecordeAritmetica(resultado);
		verificarRecordeHarmonica(resultado);
		verificarRecordeMaxMin(resultado);
		
		
		
				
		return resultado;
	}
	
	public Resultado testar(Collection<PadraoFAN> conjuntoTeste, RedeFAN rede) {
		return testar(conjuntoTeste, rede, true);
	}
	
	private Resultado testar(Collection<PadraoFAN> conjuntoTeste, RedeFAN rede, boolean verificarRecordes) {
		Resultado resultado = new Resultado();
	
		int[][] matrizConfusao = rede.testar(conjuntoTeste);
		
		int[] totalClasses = calcularTotalClasses(matrizConfusao);
		double[] acertoClasses = calcularAcertoAritmeticoClasses(matrizConfusao, totalClasses);
		resultado.setAcertosAritmeticosClasses(acertoClasses);
		
		resultado.setAcertoArimetico(calcularAcertoAritmetico(acertoClasses));
		resultado.setAcertoHarmonico(calcularAcertoHarmonico(acertoClasses, totalClasses));
		resultado.setAcertoMaxMin(calcularAcertoMaxMin(acertoClasses));
		
		resultado.setMatrizConfusao(matrizConfusao);
		
		//verificar se quebrou recorde e disparar eventos
		if (verificarRecordes) {
			verificarRecordeAritmetica(resultado);
			verificarRecordeHarmonica(resultado);
			verificarRecordeMaxMin(resultado);
		}
				
		return resultado;
	}
	
	public Resultado validar(Collection<PadraoFAN> conjuntoValidacao, RedeFAN rede) {
		return testar(conjuntoValidacao, rede, false);
	}
	
	public Resultado validar(Collection<PadraoFAN> conjuntoValidacao) {
		return testar(conjuntoValidacao, this.redeAtual, false);
	}
		
	
	
	public RedeFAN getRedeMelhorAritmetica() {
		return redeMelhorAritmetica;
	}

	public RedeFAN getRedeMelhorHarmonica() {
		return redeMelhorHarmonica;
	}

	public RedeFAN getRedeMelhorMaxMin() {
		return redeMelhorMaxMin;
	}

	public double getMelhorAritmetica() {
		return melhorAritmetica;
	}

	public double getMelhorHarmonica() {
		return melhorHarmonica;
	}

	public double getMelhorMaxMin() {
		return melhorMaxMin;
	}

	public void classificar(Collection<PadraoFAN> conjuntoClassificacao, RedeFAN rede) {
		rede.classificar(conjuntoClassificacao);
	}
	
	public void setTempera(ITemperaSimulada tempera) {
		redeAtual.setTemperaSimulada(tempera);
	}
	
	
	/*
	 * ---Fim funções rede---
	 */
	
	/*
	 * Região recordes
	 */
	
	
	private void verificarRecordeAritmetica(Resultado resultado) {
		
		aritmetica = resultado.getAcertoArimetico();
		if (aritmetica > melhorAritmetica) {
	
			melhorAritmetica = aritmetica;
			
			//NeuronioFAN[] neuroniosAtuais = redeAtual.getNeuronios();
			
			//if(redeMelhorAritmetica == null) {
				redeMelhorAritmetica = (RedeFAN) redeAtual.clone();
			//}
			
			//for(byte i = 0; i < neuroniosAtuais.length; i++) {
				//neuroniosMelhorAritmetica[i] = (NeuronioFAN) neuroniosAtuais[i].clone();
			//}
			
			if(monitorListener != null && eventoRedeAritmeticaSuperada == null) { //Variável fixa com lazy init
				eventoRedeAritmeticaSuperada = new RedeSuperadaEvent(this,redeMelhorAritmetica);
			}
			
			if (monitorListener != null) {
				monitorListener.melhorAritmeticaSuperada(eventoRedeAritmeticaSuperada);
			}
		}
		
	}
	
	private void verificarRecordeHarmonica(Resultado resultado) {
		
		harmonica = resultado.getAcertoHarmonico();
		if (harmonica > melhorHarmonica) {
	
			melhorHarmonica = harmonica;
			
			//NeuronioFAN[] neuroniosAtuais = redeAtual.getNeuronios();
			
			//if(redeMelhorHarmonica == null) {
				redeMelhorHarmonica = (RedeFAN) redeAtual.clone();
			//}
			
			/*for(byte i = 0; i < neuroniosAtuais.length; i++) {
				neuroniosMelhorHarmonica[i] = (NeuronioFAN) neuroniosAtuais[i].clone();
			}*/
			
			if(monitorListener != null && eventoRedeHarmonicaSuperada == null) { //Variável fixa com lazy init
				eventoRedeHarmonicaSuperada = new RedeSuperadaEvent(this,redeMelhorHarmonica);
			}
			
			if (monitorListener != null) {
				monitorListener.melhorHarmonicaSuperada(eventoRedeHarmonicaSuperada);
			}
		}
		
	}
	
	private void verificarRecordeMaxMin(Resultado resultado) {
		
		maxMin = resultado.getAcertoMaxMin();		
		if (maxMin > melhorMaxMin) {
	
			melhorMaxMin = maxMin;
			
			//NeuronioFAN[] neuroniosAtuais = redeAtual.getNeuronios();
			
			//if(redeMelhorMaxMin == null) {
				redeMelhorMaxMin = (RedeFAN) redeAtual.clone();
			//}
			
		/*	for(byte i = 0; i < neuroniosAtuais.length; i++) {
				neuroniosMelhorMaxMin[i] = (NeuronioFAN) neuroniosAtuais[i].clone();
			}*/
			
			if(monitorListener != null && eventoRedeMaxMinSuperada == null) { //Variável fixa com lazy init
				eventoRedeMaxMinSuperada = new RedeSuperadaEvent(this,redeMelhorMaxMin);
			}
			
			if (monitorListener != null) {
				monitorListener.melhorMaxMinSuperada(eventoRedeMaxMinSuperada);
			}
		}
	}
		
	
	/*
	 * Fim região recordes
	 */
	
	/*
	 * Região estatística
	 */

	private int[] calcularTotalClasses(int[][] matrizConfusao) {
		// 1a Dimensão -> Classe
		// 2a Dimensão:
		//			índice 0 -> Acertos da classe
		//			índice 1 -> Erros da classe
		int[] totalClasses = new int[matrizConfusao.length];
		for(int i = 0; i < matrizConfusao.length; i++) {
			totalClasses[i] = matrizConfusao[i][0] + matrizConfusao[i][1]; 
		}
		return totalClasses;
	}
	
	private double[] calcularAcertoAritmeticoClasses(int[][] matrizConfusao, int[] totalClasses) {
		// 1a Dimensão -> Classe
		// 2a Dimensão:
		//			índice 0 -> Acertos da classe
		double[] acertoClasses = new double[matrizConfusao.length];
		for(int i = 0; i < matrizConfusao.length; i++) {
			//acertoClasses[i] = matrizConfusao[i][0]/((double)totalClasses[i]); 
			acertoClasses[i] += matrizConfusao[i][0]/((double)totalClasses[i]);
		}
		return acertoClasses;
	}
	
	private double calcularAcertoAritmetico(double[] acertoClasses) {
		// 1a Dimensão -> Classe
		// 2a Dimensão:
		//			índice 0 -> Acertos da classe
		//			índice 1 -> Erros da classe
		double acertos = 0;
		//int total = 0;
		for (int i = 0; i < acertoClasses.length; i++) {
			acertos += acertoClasses[i];
			//total += totalClasses[i];
		}
		
		return acertos/acertoClasses.length;
	}
	
	private double calcularAcertoHarmonico(double[] acertoClasses, int[] totalClasses) {
		// 1a Dimensão -> Classe
		// 2a Dimensão:
		//			índice 0 -> Acertos da classe
		//			índice 1 -> Erros da classe
		
		double mediaHarmonica = 0.0; 
				
		for(int i = 0; i<acertoClasses.length; i++) {
			//mediaHarmonica += acertoClasses[i]/totalClasses[i];
			mediaHarmonica += 1./acertoClasses[i];
		}
		
		//mediaHarmonica /= acertoClasses.length;
		
		mediaHarmonica = acertoClasses.length / mediaHarmonica;
		
		return mediaHarmonica;
	}
	
	private double calcularAcertoMaxMin(double[] acertoClasses) {
		
		// 1a Dimensão -> Classe
		// 2a Dimensão:
		//			índice 0 -> Acertos da classe
		//			índice 1 -> Erros da classe
		
		double temp = 2.0;
		for (double acerto : acertoClasses) {
			if (acerto < temp){
				temp = acerto;
			}
		}
		
		return temp;
		
	}
	
	/*
	 * Fim região estatística
	 */
	
	

	
	public static void main(String[] args) {
		
		int raio = 6;
		int suporte = 100;
		
		FuncaoPertinencia funcao = new FuncaoPertinenciaTriangular();
		
		//GerenciadorConjuntos gerenciadorConjuntos = new GerenciadorConjuntos();
		
		NormalizadorConfig configNorm;
		FabricaNormalizador fabricaNormalizador;
		Normalizador normalizador;	
		
		LinkedList<Padrao> padroes = new LinkedList<Padrao>();
		
		
		//0 0 = 0
		Padrao p1 = new Padrao(new double[]{5.0,5.0},"0");
		//0 1 = 0
		Padrao p2 = new Padrao(new double[]{5.0,10.0},"0");
		//1 0 = 0
		Padrao p3 = new Padrao(new double[]{10.0,5.0},"0");
		//1 1 = 1
		Padrao p4 = new Padrao(new double[]{10.0,10.0},"1");
		
		padroes.add(p1);
		padroes.add(p2);
		padroes.add(p3);
		padroes.add(p4);
				
		
		configNorm = NormalizadorConfig.getNormalizadorConfiguration(padroes);
		
		fabricaNormalizador = new FabricaNormalizadorPadrao(configNorm);
		
		normalizador = fabricaNormalizador.criarNormalizador(NormalizadoresEnum.MAX.getID());
				
		//dados normalizados
		normalizador.normalizar(padroes);
		
		LinkedList<PadraoFAN> padroesFAN = new LinkedList<PadraoFAN>();
		
		for(Padrao p : padroes) {
			try {
				padroesFAN.add(p.createPadraoFAN(raio, suporte,funcao));
			} catch (PadraoNaoNormalizadoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		RedeFAN redeFAN = new RedeFAN(raio,suporte,2,2);
		
		for (int i = 0; i < 100; i++) {
			redeFAN.treinar(padroesFAN);	
		}
		
		int[][] erros = redeFAN.testar(padroesFAN);
		
		System.out.println("Acertos 0:" + erros[0][0]);
		System.out.println("Erros 0:" + erros[0][1]);
		System.out.println("Acertos 1:" + erros[1][0]);
		System.out.println("Erros 1:" + erros[1][1]);
		
		
		for (PadraoFAN p : padroesFAN) {
			p.setClasse(-1);
		}
		
		redeFAN.classificar(padroesFAN);
				
		for (PadraoFAN p : padroesFAN) {
			System.out.println(p.getClasse());
		}
		
		System.console().readLine();
		
	}

	public MonitorListener getMonitorListener() {
		return monitorListener;
	}

	public void setMonitorListener(MonitorListener monitorListener) {
		this.monitorListener = monitorListener;
	}
	
	public Resultado testar(
			List<PadraoFAN> padroesFAN) {
		return testar(padroesFAN, redeAtual);
	}

	public Resultado testarRedeMelhorAritmetica(
			List<PadraoFAN> padroesFAN) {
		return testar(padroesFAN, redeMelhorAritmetica);
	}

	public Resultado testarRedeMelhorHarmonica(
			LinkedList<PadraoFAN> padroesFAN) {
		return testar(padroesFAN, redeMelhorHarmonica);
	}

	public Resultado testarRedeMelhorMaxMin(
			LinkedList<PadraoFAN> padroesFAN) {
		return testar(padroesFAN, redeMelhorMaxMin);
	}

	public void classificar(List<PadraoFAN> padroesFAN) {
		classificar(padroesFAN, redeAtual);
	}
	
	public void classificarRedeMelhorAritmetica(List<PadraoFAN> list) {
		classificar(list, redeMelhorAritmetica);
	}
	
	public void classificarRedeMelhorHarmonica(LinkedList<PadraoFAN> padroesFAN) {
		classificar(padroesFAN, redeMelhorHarmonica);
	}
	
	public void classificarRedeMelhorMaxMin(LinkedList<PadraoFAN> padroesFAN) {
		classificar(padroesFAN, redeMelhorMaxMin);
	}
	
	public long getEpoca() {
		return epoca;
	}
	
	public double getAritmetica(){
		return aritmetica;
	}
	
	public int getQuantasClasses(){
		return redeAtual.getNeuronios().length;
	}
	
}
