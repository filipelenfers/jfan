package jfan.fan;

import java.io.Serializable;
import java.util.Random;

import jfan.fan.padroes.PadraoFAN;


/**
 * Classe que representa o neurônio fan.
 * 
 * @author Filipe Pais Lenfers
 * @version 0.7.0
 */
public class NeuronioFAN implements Serializable,Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8998384799540666571L;
		
	
	private transient RedeFAN rede;
	/**
	 * Cont�m os arrays com os valores do neur�nio, a primeira dimens�o se
	 * refere ao n�mero da caracter�stica, a segunda dimens�o se refere a
	 * posi��o no array de tamanho J.
	 */
	private double[][] matrizNeural;

	/**
	 * O somat�rio de cada caracter�stica (�ndice da dimens�o) da matrizNeural.
	 */
	private double[] somatorios;
	
	public NeuronioFAN(RedeFAN rede) {
		this.rede = rede;
		this.somatorios = new double[rede.getNumeroCaracteristicas()];
		int escape = (rede.getRaioDifuso()<<2) - 2;
		if (escape < 0) {
			escape = 0;
		}
		this.matrizNeural = new double[rede.getNumeroCaracteristicas()][rede.getNumeroSuporteConjuntosDifusos() +  escape ]; 
	}
	
	public NeuronioFAN(double[] somatorios, double[][] matrizNeural) {
		this.somatorios = somatorios;
		this.matrizNeural = matrizNeural;
	}
		

	/**
	 * Inicializa o neur�nio com valores entre 0.0 (inclusive) e 1.0(exclusive).
	 */
	public void inicializarAleatorio() {
		Random rand;
		for (int i = 0; i < rede.getNumeroCaracteristicas(); i++) {
			rand = new Random();
			this.somatorios[i] = 0;
			for (int j = 0; j < this.matrizNeural[0].length; j++) {
				this.matrizNeural[i][j] = rand.nextDouble();
				this.somatorios[i] += this.matrizNeural[i][j];
			}
		}
	}

	/**
	 * Inicializa o neur�nio com 0.0 em todas as posi��es.
	 */
	public void inicializarZerado() {
		for (int i = 0; i < rede.getNumeroCaracteristicas(); i++) {
			this.somatorios[i] = 0;
			for (int j = 0; j < this.matrizNeural[0].length; j++) {
				this.matrizNeural[i][j] = 0;
			}
		}
	}



	/**
	 * Normaliza os valores do neur�nio.
	 */
	public void normalizar() {
		for (int i = 0; i < rede.getNumeroCaracteristicas(); i++) {
			for (int j = 0; j < this.matrizNeural[0].length; j++) {
				this.matrizNeural[i][j] /= somatorios[i];
			}
		}
	}

	/**
	 * Determina a for�a de representa��o de um padr�o neste neur�nio.
	 * 
	 * @param p
	 *            o padr�o do qual ser� determinada a for�a de representa��o.
	 * @return retorna o valor da for�a de representa��o do neur�nio para o
	 *         padr�o fornecido.
	 */
	public double determinarForca(PadraoFAN p) {
		// i = cada característica
		// j = cada parte de entre [a,b]
		double[] arrayCaracFAN;
		int fim;
		double[] arrCaracteristica;
		double forca = 1.0;
		for(int i = 0; i < matrizNeural.length; i++  ) {
			arrayCaracFAN = p.getCaracteristicaFAN(i).getConjuntoDifuso();
			fim = p.getCaracteristicaFAN(i).getFim();
			arrCaracteristica = matrizNeural[i];
			for (int j = p.getCaracteristicaFAN(i).getInicio(), k = 0; j <= fim; j++, k++) {
				forca *= 1 - (arrayCaracFAN[k] * arrCaracteristica[j]) / somatorios[i]; 
			}
		}
		//forca = 1 - Math.pow(forca, matrizNeural.length) // ??? esse é o padrão usado no labfan,mas não existe nada descrito na tese sobre a exponenciação pelo número de características 
		return 1 - forca;
	}

	
	public void setValor(int caracteristica, int posicao, double valor) {
		this.matrizNeural[caracteristica][posicao] = valor;
	}

	public double getValor(int caracteristica, int posicao) {
		return this.matrizNeural[caracteristica][posicao];
	}

	/**
	 * Quantas caracter�sticas o neuronio representa.
	 * 
	 * @return o n�mero de caracteristica que o neur�nio est� tratando.
	 */
	public int quantasCaracteristicas() {
		return this.matrizNeural.length;
	}

	
	public void adicionaValor(int caracteristica, int posicao, double valor) {
		this.matrizNeural[caracteristica][posicao] += valor;
		this.somatorios[caracteristica] += valor;
	}
	
	/**
	 * 
	 * @return
	 */
	public final double[][] getMatrizNeural() {
		return this.matrizNeural;
	}
	
	

	/**
	 * Pega o somat�rio da caracter�stica especificada.
	 * @param caracteristica O n�mero da caracter�stica.
	 * @return O somat�rio da caracter�stica.
	 */
	public double getSomatorio(int caracteristica) {
		return this.somatorios[caracteristica - 1];
	}
	
	/**
	 * Clona este neur�nio. Todos os valores do neur�nio s�o mantidos,
	 * mas o neur�nio de origem � um inst�ncia diferente do novo neur�nio
	 * gerado por este m�todo. 
	 */
	/*public NeuronioFAN clone() {
		NeuronioFAN n = new NeuronioFAN(this.numCaractertisticas,this.J,this.raioDifuso,this.classe,false);
		n.tamanhoArray = this.tamanhoArray;
		int fim1 = this.matrizNeural.length;
		int fim2 = this.matrizNeural[0].length;
		for (int i = 0; i < fim1; i++) {
			for (int j = 0; j < fim2; j++) {
				n.matrizNeural[i][j] = this.matrizNeural[i][j];
			}
			n.somatorios[i] = this.somatorios[i];
		}
		return n;
	}*/
	
	/**
	 * 
	 * @param indiceCaracteristica qual caracter�stica ser� alterada
	 * @param somatorioCaracteristica o somat�rio da caracter�stica
	 */
	public void setSomatorio(int indiceCaracteristica, double somatorioCaracteristica){
		this.somatorios[indiceCaracteristica]=somatorioCaracteristica;
	}


	@Override
	protected Object clone() {
		NeuronioFAN n = new NeuronioFAN(rede);

		n.matrizNeural = new double[matrizNeural.length][matrizNeural[0].length];
		int i = 0;
		for (double[] arrFonte : matrizNeural) {
			System.arraycopy(arrFonte, 0, n.matrizNeural[i], 0, arrFonte.length);
			i++;
		}
		
		n.somatorios = somatorios;
		
		return n;
	}


	public RedeFAN getRede() {
		return rede;
	}


	public void setRede(RedeFAN rede) {
		this.rede = rede;
	}
	
	
	
	
}
