package jfan.fan;

public class Resultado {
	
	private int[][] matrizConfusao;
	private double[] acertosAritmeticosClasses;
	
	private double acertoArimetico;
	private double acertoHarmonico;
	private double acertoMaxMin;
	
	public int[][] getMatrizConfusao() {
		return matrizConfusao;
	}
	
	public void setMatrizConfusao(int[][] matrizConfusao) {
		this.matrizConfusao = matrizConfusao;
	}
	
	public double[] getAcertosAritmeticosClasses() {
		return acertosAritmeticosClasses;
	}
	
	public void setAcertosAritmeticosClasses(double[] acertosAritmeticosClasses) {
		this.acertosAritmeticosClasses = acertosAritmeticosClasses;
	}

	public double getAcertoArimetico() {
		return acertoArimetico;
	}

	public void setAcertoArimetico(double acertoArimetico) {
		this.acertoArimetico = acertoArimetico;
	}

	public double getAcertoHarmonico() {
		return acertoHarmonico;
	}

	public void setAcertoHarmonico(double acertoHarmonico) {
		this.acertoHarmonico = acertoHarmonico;
	}

	public double getAcertoMaxMin() {
		return acertoMaxMin;
	}

	public void setAcertoMaxMin(double acertoMaxMin) {
		this.acertoMaxMin = acertoMaxMin;
	}
	

}
