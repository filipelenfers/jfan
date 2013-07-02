package jfan.fan.padroes;

/**
 * Classe que representa um padr�o (indiv�duo ou X) em FAN.
 * Contêm somente as caraterísticas já decompostas.
 * 
 * @author Filipe Pais Lenfers
 * @version 0.0.1
 */
public class PadraoFAN {
	
	/**
	 * Guarda as caracteristicasFAN do padrão.
	 * São as únicas necessárias para treinar a rede.
	 */
	CaracteristicaFAN[] caracteristicasFAN;
	
	/**
	 * Guarda a classe associada as caracter�sticas.
	 */
	int classe;
	
	public PadraoFAN(int classe) {
		//super(caracteristicas,classe);
		//this.caracteristicaNormalizada = new double[caracteristicas.length];
	}
	
	/**
	 * Retorna a classe associada ao padrão.
	 * 
	 * @return a classe.
	 */
	public int getClasse() {
		return this.classe;
	}

	
	/**
	 * Retorna quantas caracter�sticas o padr�o possui.
	 * 
	 * @return o n�mero de caracte?isticas
	 */
	public int getQuantasCaracteristicas() {
		return this.caracteristicasFAN.length;
	}

	/**
	 * Retorna o objeto caracteristicaFAN que representa a caracteristica.
	 * @param numCaracteristica
	 *            o n�mero da caracter�stica (nunca numCaracteristica < 0) 
	 * @return o objeto caracteristicaFAN que representa a caracteristica
	 */
	public CaracteristicaFAN getCaracteristicaFAN(int numCaracteristica) {
		return caracteristicasFAN[numCaracteristica];
	}

	/**
	 * Define um objeto caracteristicaFAN para a caracter�stica especificada.
	 * @param numCaracteristica
	 *            o n�mero da caracter�stica (nunca numCaracteristica < 0)
	 * @param caracteristicaFAN o objeto caracteristicaFAN que representa a caracteristica
	 */
	public void setCaracteristicaFAN(int numCaracteristica, CaracteristicaFAN caracteristicaFAN) {
		this.caracteristicasFAN[numCaracteristica] = caracteristicaFAN;
	}

	/**
	 * Retorna um array contendo todas as caracter�sticasFAN do padr�o.
	 * @return um array de CaracteristicasFAN com cada �ndice apontando a sua (caracter�stica) espec�fica .
	 */
	public CaracteristicaFAN[] getCaracteristicasFAN() {
		return caracteristicasFAN;
	}
	
	public void setClasse(int classe) {
		this.classe = classe;
	}

	public void setCaracteristicasFAN(CaracteristicaFAN[] caracteristicasFAN) {
		this.caracteristicasFAN = caracteristicasFAN;
	}
	
}
