package jfan.fan.conjuntos;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jfan.fan.exceptions.NumeroCarateristicasIncompativelException;
import jfan.fan.exceptions.PadraoNaoNormalizadoException;
import jfan.fan.fuzzy.FuncaoPertinencia;
import jfan.fan.padroes.MapeadorClasses;
import jfan.fan.padroes.Padrao;
import jfan.fan.padroes.PadraoFAN;

/**
 * Gerenciador de conjuntos padrão. O LinkedList foi escolhido por ser mais rápido em iterações usando o iterador,
 * ou seja o for enhanced também é otimizado. 
 * 
 * @author Filipe Pais Lenfers
 * @version 0.2
 */
//TODO XXXXXX extrair interface disto XXXXXX
	//TODO XXXXXX criar novo gerenciador mais "esperto", mantendo fora de memoria os arquivos brutos e normalizados. Talvez java BD ?XXXXXX
public class GerenciadorConjuntos  {	

	private List<PadraoFAN> conjuntoTreinamento;
	private LinkedList<Padrao> conjuntoTreinamentoNormalizado;
	private List<PadraoFAN> conjuntoValidacao;
	private List<Padrao> conjuntoValidacaoNormalizado;
	private List<PadraoFAN> conjuntoTeste;
	private List<Padrao> conjuntoTesteNormalizado;
	private List<PadraoFAN> conjuntoClassificacao; // Precisa disso realmente?
	private List<Padrao> conjuntoClassificacaoNormalizado;
	
	//Padr�es brutos, necess�rios para recuperar tudo
	private LinkedList<Padrao> conjuntoTreinamentoBruto = new LinkedList<Padrao>();
	private LinkedList<Padrao> conjuntoTesteBruto  = new LinkedList<Padrao>() ; 
	private LinkedList<Padrao> conjuntoValidacaoBruto  = new LinkedList<Padrao>();
	private LinkedList<Padrao> conjuntoClassificacaoBruto  = new LinkedList<Padrao>();
	
	
	
	Map<String, Integer> mapaInteiroRealParaInterno;
	Map<Integer, String> mapaInternoParaInteiroReal;
	

	MapeadorClasses mapeadorClasses = new MapeadorClasses();
	
	private int suporteConjuntosDifusos;
	

	private FuncaoPertinencia funcaoPertinencia;
	private int raioDifuso;
	
	public GerenciadorConjuntos() {
	}
	
	/*public void addPadroesConjuntoTreinamento(List<PadraoFAN> padroes) throws NumeroCarateristicasIncompativelException {
		
		if (conjuntoTreinamento == null) {
			conjuntoTreinamento = new LinkedList<PadraoFAN>();
		}
		
		addPadroes(padroes, conjuntoTreinamento);
	}
	
	private void addPadroes(List<PadraoFAN> padroesNovos, List<PadraoFAN> conjunto ) throws NumeroCarateristicasIncompativelException {
		
		//Se qualquer conjunto 0, validar entrada de carateristicas 
		if (conjuntoTreinamento != null && conjuntoTreinamento.size() > 0 && conjuntoTreinamento.get(0).getQuantasCaracteristicas() != padroesNovos.get(0).getQuantasCaracteristicas()) {
			 throw new NumeroCarateristicasIncompativelException("O número de característica do conjunto novo deve ser igual ao já carregado.");
		}
		if (conjuntoTeste != null && conjuntoTeste.size() > 0 && conjuntoTeste.get(0).getQuantasCaracteristicas() != padroesNovos.get(0).getQuantasCaracteristicas()) {
			 throw new NumeroCarateristicasIncompativelException("O número de característica do conjunto novo deve ser igual ao já carregado.");
		}
		if (conjuntoValidacao != null && conjuntoValidacao.size() > 0 && conjuntoValidacao.get(0).getQuantasCaracteristicas() != padroesNovos.get(0).getQuantasCaracteristicas()) {
			 throw new NumeroCarateristicasIncompativelException("O número de característica do conjunto novo deve ser igual ao já carregado.");
		}
		
		
		if (mapaInteiroRealParaInterno == null) {
			mapaInteiroRealParaInterno = mapeadorClasses.gerarMapaClassesPadraoFAN(padroesNovos);
			mapaInternoParaInteiroReal = inverterMapa(mapaInteiroRealParaInterno);
			
		}
		else {
			mapeadorClasses.gerarMapaClassesPadraoFAN(padroesNovos, mapaInteiroRealParaInterno);
			mapaInternoParaInteiroReal = inverterMapa(mapaInteiroRealParaInterno);
		}
		//mapeadorClasses.mapearClasses(padroesBrutos, mapa);
		
		for(PadraoFAN p : padroesNovos){
			//Mapeia a classe
			p.setClasse(mapaInteiroRealParaInterno.get(p.getClasse()));
			
			conjunto.add(p);
		}	
	}
	
	
	public void addPadroesConjuntoValidacao(List<PadraoFAN> padroes) throws NumeroCarateristicasIncompativelException {
		if (conjuntoValidacao == null) {
			conjuntoValidacao = new LinkedList<PadraoFAN>();
		}
		addPadroes(padroes, conjuntoValidacao);
	}
	
	public void addPadroesConjuntoTeste(List<PadraoFAN> padroes) throws NumeroCarateristicasIncompativelException {
		if (conjuntoTeste == null) {
			conjuntoTeste = new LinkedList<PadraoFAN>();
		}
		addPadroes(padroes, conjuntoTeste);	
	}*/
		
	
private void addPadroes(List<Padrao> padroesNovos, List<PadraoFAN> conjunto, List<Padrao> conjuntoOriginal) throws NumeroCarateristicasIncompativelException, PadraoNaoNormalizadoException {
	
		if(padroesNovos.size() == 0) return;
	
		//Se qualquer conjunto 0, validar entrada de carateristicas 
		if (conjuntoTreinamento != null && conjuntoTreinamento.size() > 0 && conjuntoTreinamento.get(0).getQuantasCaracteristicas() != padroesNovos.get(0).getQuantasCaracteristicas()) {
			 throw new NumeroCarateristicasIncompativelException("O número de característica do conjunto novo deve ser igual ao já carregado.");
		}
		if (conjuntoTeste != null && conjuntoTeste.size() > 0 && conjuntoTeste.get(0).getQuantasCaracteristicas() != padroesNovos.get(0).getQuantasCaracteristicas()) {
			 throw new NumeroCarateristicasIncompativelException("O número de característica do conjunto novo deve ser igual ao já carregado.");
		}
		if (conjuntoValidacao != null && conjuntoValidacao.size() > 0 && conjuntoValidacao.get(0).getQuantasCaracteristicas() != padroesNovos.get(0).getQuantasCaracteristicas()) {
			 throw new NumeroCarateristicasIncompativelException("O número de característica do conjunto novo deve ser igual ao já carregado.");
		}
		
		
		if (mapaInteiroRealParaInterno == null) {
			mapaInteiroRealParaInterno = mapeadorClasses.gerarMapaClassesPadrao(padroesNovos);
			mapaInternoParaInteiroReal = inverterMapa(mapaInteiroRealParaInterno);
			
		}
		else {
			mapeadorClasses.gerarMapaClassesPadrao(padroesNovos, mapaInteiroRealParaInterno);
			mapaInternoParaInteiroReal = inverterMapa(mapaInteiroRealParaInterno);
		}
		//mapeadorClasses.mapearClasses(padroesBrutos, mapa);
		
		PadraoFAN pTemp;
		for(Padrao p : padroesNovos){
			
			//conjunto original
			conjuntoOriginal.add(p);
			
			//Mapeia a classe
			pTemp = p.createPadraoFAN(raioDifuso, suporteConjuntosDifusos, funcaoPertinencia);
			pTemp.setClasse(mapaInteiroRealParaInterno.get(p.getClasse()));
			
			conjunto.add(pTemp);
		}	
	}

private void addPadroesBrutos(List<Padrao> padroesBrutosNovos, List<Padrao> destino) throws NumeroCarateristicasIncompativelException {
	if(padroesBrutosNovos.size() == 0) return;
	
	//Se qualquer conjunto 0, validar entrada de carateristicas 
	if (conjuntoTreinamentoBruto != null && conjuntoTreinamentoBruto.size() > 0 && conjuntoTreinamentoBruto.get(0).getQuantasCaracteristicas() != padroesBrutosNovos.get(0).getQuantasCaracteristicas()) {
		 throw new NumeroCarateristicasIncompativelException("O número de característica do conjunto novo deve ser igual ao já carregado.");
	}
	if (conjuntoTesteBruto != null && conjuntoTesteBruto.size() > 0 && conjuntoTesteBruto.get(0).getQuantasCaracteristicas() != padroesBrutosNovos.get(0).getQuantasCaracteristicas()) {
		 throw new NumeroCarateristicasIncompativelException("O número de característica do conjunto novo deve ser igual ao já carregado.");
	}
	if (conjuntoValidacaoBruto != null && conjuntoValidacaoBruto.size() > 0 && conjuntoValidacaoBruto.get(0).getQuantasCaracteristicas() != padroesBrutosNovos.get(0).getQuantasCaracteristicas()) {
		 throw new NumeroCarateristicasIncompativelException("O número de característica do conjunto novo deve ser igual ao já carregado.");
	}
	for(Padrao p : padroesBrutosNovos){
		
		//conjunto original
		destino.add(p);
	}
}

public void addPadroesConjuntoTreinamentoBruto(List<Padrao> padroes) throws NumeroCarateristicasIncompativelException {

	addPadroesBrutos(padroes, conjuntoTreinamentoBruto);
}

public void addPadroesConjuntoTesteBruto(List<Padrao> padroes) throws NumeroCarateristicasIncompativelException {

	addPadroesBrutos(padroes, conjuntoTesteBruto);
}

public void addPadroesConjuntoValidacaoBruto(List<Padrao> padroes) throws NumeroCarateristicasIncompativelException {

	addPadroesBrutos(padroes, conjuntoValidacaoBruto);
}

public void addPadroesConjuntoClassificacaoBruto(List<Padrao> padroes) throws NumeroCarateristicasIncompativelException {

	addPadroesBrutos(padroes, conjuntoClassificacaoBruto);
}
	
public void addPadroesConjuntoTreinamento(List<Padrao> padroes) throws NumeroCarateristicasIncompativelException, PadraoNaoNormalizadoException {
	
	if (conjuntoTreinamento == null) {
		conjuntoTreinamento = new LinkedList<PadraoFAN>();
	}
	
	if (conjuntoTreinamentoNormalizado == null) {
		conjuntoTreinamentoNormalizado = new LinkedList<Padrao>();
	}
	
	addPadroes(padroes, conjuntoTreinamento, conjuntoTreinamentoNormalizado);
}
	
	public void addPadroesConjuntoValidacao(List<Padrao> padroes) throws NumeroCarateristicasIncompativelException, PadraoNaoNormalizadoException {
		if (conjuntoValidacao == null) {
			conjuntoValidacao = new LinkedList<PadraoFAN>();
		}
		if (conjuntoValidacaoNormalizado == null) {
			conjuntoValidacaoNormalizado = new LinkedList<Padrao>();
		}
		addPadroes(padroes, conjuntoValidacao, conjuntoValidacaoNormalizado);
	}
	
	public void addPadroesConjuntoTeste(List<Padrao> padroes) throws NumeroCarateristicasIncompativelException, PadraoNaoNormalizadoException {
		if (conjuntoTeste == null) {
			conjuntoTeste = new LinkedList<PadraoFAN>();
		}
		if (conjuntoTesteNormalizado == null) {
			conjuntoTesteNormalizado = new LinkedList<Padrao>();
		}
		addPadroes(padroes, conjuntoTeste, conjuntoTesteNormalizado);	
	}
	
	public void addPadroesConjuntoClassificacao(List<Padrao> padroes) throws NumeroCarateristicasIncompativelException, PadraoNaoNormalizadoException {
		if (conjuntoClassificacao == null) {
			conjuntoClassificacao = new LinkedList<PadraoFAN>();
		}
		if (conjuntoClassificacaoNormalizado == null) {
			conjuntoClassificacaoNormalizado = new LinkedList<Padrao>();
		}
		addPadroes(padroes, conjuntoClassificacao, conjuntoClassificacaoNormalizado);	
	}

	public LinkedList<Padrao> getConjuntoTreinamentoNormalizado() {
		return conjuntoTreinamentoNormalizado;
	}

	public List<Padrao> getConjuntoValidacaoNormalizado() {
		return conjuntoValidacaoNormalizado;
	}

	public List<Padrao> getConjuntoTesteNormalizado() {
		return conjuntoTesteNormalizado;
	}
	
	public List<Padrao> getConjuntoClassificacaoNormalizado() {
		return conjuntoClassificacaoNormalizado;
	}

	public List<PadraoFAN>getConjuntoTreinamento() {
		return conjuntoTreinamento;
	}

	

	public List<PadraoFAN>getConjuntoValidacao() {
		return conjuntoValidacao;
	}
	

	public List<PadraoFAN>getConjuntoTeste() {
		return conjuntoTeste;
	}
	
	public List<PadraoFAN>getConjuntoClassificacao() {
		return conjuntoClassificacao;
	}

	public Map<String, Integer> getMapaInteiroRealParaInterno() {
		return mapaInteiroRealParaInterno;
	}
	
	public Map<Integer, String> getMapaInternoParaInteiroReal() {
		return mapaInternoParaInteiroReal;
	}
	
	public void limpar() {
		mapaInteiroRealParaInterno = null;
		mapaInternoParaInteiroReal = null;
		conjuntoTreinamento = null;
		conjuntoTeste = null;
		conjuntoValidacao = null;
	}
	
	private Map<Integer, String> inverterMapa(Map<String, Integer> mapa) {
		Map<Integer, String> mapaInvertido = new HashMap<Integer, String>();
		for (String i : mapa.keySet()) {
			mapaInvertido.put(mapa.get(i), i);
		}
		return mapaInvertido;
	}

	
	public int getSuporteConjuntosDifusos() {
		return suporteConjuntosDifusos;
	}

	public FuncaoPertinencia getFuncaoPertinencia() {
		return funcaoPertinencia;
	}

	public int getRaioDifuso() {
		return raioDifuso;
	}

	public void setSuporteConjuntosDifusos(int suporteConjuntosDifusos) {
		this.suporteConjuntosDifusos = suporteConjuntosDifusos;
	}

	public void setFuncaoPertinencia(FuncaoPertinencia funcaoPertinencia) {
		this.funcaoPertinencia = funcaoPertinencia;
	}

	public void setRaioDifuso(int raioDifuso) {
		this.raioDifuso = raioDifuso;
	}

	public LinkedList<Padrao> getConjuntoTreinamentoBruto() {
		return conjuntoTreinamentoBruto;
	}

	public LinkedList<Padrao> getConjuntoTesteBruto() {
		return conjuntoTesteBruto;
	}

	public LinkedList<Padrao> getConjuntoValidacaoBruto() {
		return conjuntoValidacaoBruto;
	}

	public LinkedList<Padrao> getConjuntoClassificacaoBruto() {
		return conjuntoClassificacaoBruto;
	}	
	
}
