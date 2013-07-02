package jfan.fan.temperas.eventos;

public class TrocaValorTemperaEvent {
	private double valor;
	
	public TrocaValorTemperaEvent(double valor) {
		this.valor = valor;
	}
	
	public double getValor() {
		return valor;
	}
}
