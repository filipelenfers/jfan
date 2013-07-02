package jfan.fan.padroes;

import jfan.fan.exceptions.PadraoNaoNormalizadoException;
import jfan.fan.fuzzy.FuncaoPertinencia;



/**
 * Classe armazena o padrão "bruto" (numérico). 
 * @author filipe
 * @version 0.5
 */
public class Padrao{
	
	
	private static final String MENSAGEM_EXCESAO_PADRAO_NAO_NORMALIZADO = "A característica não está normalizada. Valor da característica: "; //$NON-NLS-1$

	/**
	 * Guarda as caracteristicas (x1,x2,...,xn) no formato double.
	 */
	double[] caracteristicas;

	/**
	 * Guarda a classe associada as características.
	 */
	String classe;
		

	/**
	 * Inst�ncia um padr�o j� com suas caracter�sticas e a sua classe.
	 * 
	 * @param caracteristicas
	 *            as caracter�sticas
	 * @param classe
	 *            a classe associada ao padr�o
	 */
	public Padrao(double[] caracteristicas, String classe) {
		this.caracteristicas = caracteristicas;
		this.classe = classe;
	}

	/**
	 * Retorna a caracter�stica no �ndice espec�ficado.
	 * 
	 * @param numCaracteristica
	 *            o n�mero da caracter�stica (nunca numCaracteristica <= 0)
	 * @return a caracter�stica
	 */
	public double getCaracteristica(int numCaracteristica) {
		return this.caracteristicas[numCaracteristica];
	}


	/**
	 * Retorna a classe associada ao padr�o.
	 * 
	 * @return a classe.
	 */
	public String getClasse() {
		return this.classe;
	}

	/**
	 * Retorna um array de doubles contendo todas as caracter�stica, mantendo os
	 * mesmos �ndices que o objeto mant�m internamente.
	 * 
	 * @return as caracteristicas
	 */
	public double[] getCaracteristicas() {
		return this.caracteristicas;
	}

	/**
	 * Retorna quantas caracter�sticas o padr�o possui.
	 * 
	 * @return o n�mero de caracte?isticas
	 */
	public int getQuantasCaracteristicas() {
		return this.caracteristicas.length;
	}
	
	public void setCaracteristica(int caracteristica, double valor) {
		this.caracteristicas[caracteristica] = valor;
	}
		
	public void setClasse(String classe) {
		this.classe = classe;
	}
	
	public Padrao clone() {
		return new Padrao(caracteristicas.clone(),classe);
	}
	
	/**
	 * Cria um PadraoFAN baseado nos parâmetros passados e no padrão atual. Para chamar esse método o 
	 * padrão deve estar normalizado. 
	 * @param raioDifuso Número do raio difuso (d).
	 * @param suporteConjuntosDifusos Numero de suporte a conjuntos difusos (tamanho dos arrays do neurônio, J)
	 * @throws PadraoNaoNormalizadoException Se o padrão não tiver as caraterística entre 0 e 1.
	 * @return O padraoFAN já com as características FAN calculadas.
	 */
	public PadraoFAN createPadraoFAN(int raioDifuso, int suporteConjuntosDifusos, FuncaoPertinencia funcaoPertinencia) throws PadraoNaoNormalizadoException {
		
		PadraoFAN padraoFAN = new PadraoFAN(-1);
		
		CaracteristicaFAN[] caracteristicasFAN = new CaracteristicaFAN[caracteristicas.length];
			
		int inicio, fim;
		double[] conjuntoDifuso;
		double caracteristicaTransformada, somatorio, a, b;
		int tamanhoArray;
		
		int conversorEscape = raioDifuso - 2 ; 
		
				
		for(int i = 0; i < caracteristicas.length; i++) {
			//Gera uma exceção caso detecte que a caraterística não está normalizada.
			if (caracteristicas[i] > 1.0 || caracteristicas[i] < 0.0) {
				throw new PadraoNaoNormalizadoException(MENSAGEM_EXCESAO_PADRAO_NAO_NORMALIZADO + caracteristicas[i]);
			}
			
			//Calcula a posição da caraterística no conjunto difuso.
			caracteristicaTransformada = caracteristicas[i] * suporteConjuntosDifusos;
			
			//incio(a) e fim(b) do conjunto difuso formado pelo padrão
			//agora ficam a e b originais matemáticos
			//$ANALYSIS-IGNORE
			int absoluto = (int)Math.abs(caracteristicaTransformada);
			a = absoluto - raioDifuso + 1;
			//$ANALYSIS-IGNORE
			inicio = (int) a;
			
			b = absoluto + raioDifuso;
			//$ANALYSIS-IGNORE
			fim = (int) b;
			
		
			
			inicio++;
			fim--;
						
			tamanhoArray = (raioDifuso-1)<<1; //<<1 é equivalente a endLoop * 2, só que bem mais rápido
						
			conjuntoDifuso = new double[tamanhoArray];
			
			
			somatorio = 0.0;
			
			//Calcula primeira parte do array
			for(int k = 0;k<tamanhoArray;k++)
			{
				conjuntoDifuso[k] = funcaoPertinencia.calcularPertinencia(inicio+k, a,caracteristicaTransformada,b );
				somatorio+=conjuntoDifuso[k];
			}
			
			//Normaliza o conjunto
			for(int j = 0; j < tamanhoArray; j++) {
				conjuntoDifuso[j] /= somatorio;
			}
			
			
			//Desloca o início e o fim para se encaixarem no array com área de escape.
			inicio += conversorEscape;
			fim += conversorEscape;
			
			caracteristicasFAN[i] = new CaracteristicaFAN(conjuntoDifuso,inicio,fim,somatorio);
			
			
		}
		
		padraoFAN.setCaracteristicasFAN(caracteristicasFAN);
		//padraoFAN.setClasse(this.classe);
		
		return padraoFAN;
	}
	
		
}
