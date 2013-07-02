package jfan.fan.padroes;

/**
 * Classe que representa o conjunto difuso de pertin�ncias 
 * de uma caracter�stica.
 * @author Filipe Pais Lenfers
 * @version 0.1.2
 */
public class CaracteristicaFAN {

	/**
	 * O conjunto difuso que representa a caracter�stica.
	 */
	private double[] conjuntoDifuso;
	/**
	 * In�cio e Fim da caracter�stica no array do neur�nio. 
	 */
	private int inicio,fim;
	
	/**
	 * O somat�rio de todo conjunto difuso.
	 */
	private double somaConjuntoDifuso;
	
	/**
	 * Inst�ncia a CarateristicaFAN.
	 * @param conjuntoDifuso o conjunto difuso que representa a caracter�stica.
	 * @param a o suporte a que representa o come�o do conjunto difuso, (a+1) passa a ser o ponto de in�cio.
	 * @param b o suporte b que representa o fim do conjunto difuso, (b-1) passa a ser o ponto de in�cio.
	 */
	public CaracteristicaFAN(double[] conjuntoDifuso, int inicio, int fim, double somatorio) {
		this.conjuntoDifuso = conjuntoDifuso;
		
		this.somaConjuntoDifuso = somatorio;
		//Variável a - 1
		this.inicio = inicio;
		//this.inicio = inicio + 1; //TODO averiguar esse + 1;
		//TODO averiguar esse + 1;
		//Variável b
		this.fim = fim;
		//this.fim = fim - 1; //- 1; //antes tava + 1 , ver se isso esta certo.
	}

	/**
	 * Retorna o conjunto difuso.
	 * @return um array de double com o conjunto difuso.
	 */
	final public double[] getConjuntoDifuso() {
		return this.conjuntoDifuso;
	}

	/**
	 * O in�cio da CaracteristicaFAN, que � dado por a + 1.
	 * @return o in�cio da caracter�stica fan no array do neur�nio.
	 */
	final public int getInicio() {
		return this.inicio;
	}

	/**
	 * O fim da CaracteristicaFAN, que � dado por b - 1.
	 * @return o fim da caracter�stica fan no array do neur�nio.
	 */
	final public int getFim() {
		return this.fim;
	}
	
	final public double getSomatorio() {
		return this.somaConjuntoDifuso;
	}
	
	public CaracteristicaFAN clone() {
		double[] arr = new double[this.conjuntoDifuso.length];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = conjuntoDifuso[i];
		}
		CaracteristicaFAN c = new CaracteristicaFAN(arr,0,0,0);
		c.fim = this.fim;
		c.inicio = this.inicio;
		c.somaConjuntoDifuso = this.somaConjuntoDifuso;
		return c;
	}
}
