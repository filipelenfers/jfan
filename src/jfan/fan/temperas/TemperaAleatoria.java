package jfan.fan.temperas;

import java.util.EventListener;
import java.util.Random;

import javax.swing.event.EventListenerList;

import jfan.fan.temperas.eventos.TrocaValorTemperaEvent;
import jfan.fan.temperas.eventos.TrocaValorTemperaListener;

/**
 * T�mpera aleat�ria que foi usada em uma das implementa��es originais de fan,
 * essa t�mpera foi adaptada para ser mais gen�rica que o modelo original proposto.
 * Como no modelo original dessa t�mpera o valor m�nimo padr�o � 0.7943 e o
 * valor m�ximo padr�o � 1.0. 
 * @author Filipe Pais Lenfers
 * @version 0.5
 */
public class TemperaAleatoria implements ITemperaSimulada {
	
	private Random random = new Random();
	private transient double valor;
	private double valorMinDefinido = 0.7943;
	private double valorMax = 1.0, valorMin = 0.7943;
	private double step = 0.0;
	private EventListenerList listenerList = new EventListenerList(); //Para multiplos listeners
	
	
	/**
	 * Retorna um valor aleat�rio que flutua entre o limite m�ximo e
	 * o limite m�nimo.
	 */
	public double getValor() {
		//valor = (10000.-random.nextInt(9900))/10000.;
		//A linha abaixo � a generaliza��o da linha acima.
		//valor = (((valorMax*10000.)-random.nextInt((int)((valorMax-valorMin)*10000.)))/10000.);
		//valor = Math.pow(valor,0.05);
		
		//modo generalizado para o modelo raitzz
		if (valorMin >= valorMax) {
			return valorMax;
		}
		valor = ((valorMax*10000.)-(valorMin*10000.));
		if (valor < 1) {
			valor = 1;
		}
		valor = random.nextInt((int)valor);
		
		valor /= 10000;
		valor += this.valorMin;
		fireTrocaValorTemperaEvent(new TrocaValorTemperaEvent(valor));
		return valor;
	}

	/**
	 * Reinicia o gerador de n�meros aleat�rio, uma
	 * inst�ncia da classe <code>Random</code>.
	 */
	public void reset() {
		random = new Random();
		valorMin = valorMinDefinido;
	}

	/**
	 * Define qual o valor m�ximo que o m�todo <code>getValor</code>
	 * pode retornar. Caso n�o seja definido o limite m�ximo (ou seja, esse m�todo n�o seja chamado) ele
	 * � considerado 1.0.
	 * S� ser�o consideradas 4 casas decimais do valor double passado.
	 * @param d o novo limite m�ximo
	 */
	public void setLimiteMaximo(double d) {
		this.valorMax = d;
	}

	/**
	 * Define qual o valor m�ximo que o m�todo <code>getValor</code>
	 * pode retornar. Caso esse m�todo nunca seja chamado o limite 
	 * m�nimo � considerado 0.7943.
	 * S� ser�o consideradas 4 casas decimais do valor double passado.
	 * @param d o novo limite m�nimo.
	 */
	public void setLimiteMinimo(double d) {
		valorMin = d;
		valorMinDefinido = d;
	}

	/**
	 * Define o valor do step para o m�todo <code>step</code>.
	 */
	public void setStep(double d) {
		this.step = d;
	}

	/**
	 * Aumenta o limite m�nimo da t�mpera no valor definido na 
	 * chamada do m�todo <code>setStep</code>.
	 */
	public void step() {
		this.valorMin += step;
		if (valorMin >= valorMax) {
			valorMin = valorMax;
		}
	}
	
	public double getLimiteMaximo() {
		return valorMax;
	}
	
	public double getLimiteMinimo() {
		return valorMinDefinido;
	}

	public double getStepValue() {
		return step;
	}
	
//	---------Para eventos de troca de valor atual-----------
	public void addTrocaValorTemperaListener(TrocaValorTemperaListener listener)
	{
	   listenerList.add(TrocaValorTemperaListener.class, listener);
	}

	public void removeTrocaValorTemperaListener(TrocaValorTemperaListener listener)
	{
	   listenerList.remove(TrocaValorTemperaListener.class, listener);
	}
	
	public void fireTrocaValorTemperaEvent(TrocaValorTemperaEvent event)
	{
	   EventListener[] listeners = listenerList.getListeners(TrocaValorTemperaListener.class);
	   for (EventListener l : listeners)
	      ((TrocaValorTemperaListener) l).trocouValorTempera(event);
	}
	//------------------------------------------------------

}
