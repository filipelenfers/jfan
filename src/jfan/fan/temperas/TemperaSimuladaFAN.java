package jfan.fan.temperas;

import java.util.EventListener;

import javax.swing.event.EventListenerList;

import jfan.fan.temperas.eventos.TrocaValorTemperaEvent;
import jfan.fan.temperas.eventos.TrocaValorTemperaListener;


/**
 * Esta classe representa a t�mpera simulada (simulated annealing) para 
 * a rede FAN. Por padr�o o valor inicial � 1.0, o step � 0.0, o limite m�ximo � 1.0 e
 * o limite m�nimo � 0.0.
 * @author Filipe Pais Lenfers
 * @version 0.1
 */
public class TemperaSimuladaFAN implements ITemperaSimulada {

	private double valorAtual = 1.0;
	private double valorStep = 0.1;
	private double limiteMaximo =  1.0;
	private double limiteMinimo =  0.0;
	private EventListenerList listenerList = new EventListenerList();
	
	public double getValor() {
		return this.valorAtual;
	}

	public void reset() {
		this.valorAtual = this.limiteMinimo;
	}

	public void setLimiteMaximo(double d) {
		this.limiteMaximo = d;
	}

	public void setLimiteMinimo(double d) {
		this.limiteMinimo = d;
	}

	public void setStep(double d) {
		this.valorStep = d;

	}
	
	public void setValorAtual(double d) {
		this.valorAtual = d;
	}

	public void step() {
		this.valorAtual += this.valorStep;
		if (this.valorAtual > this.limiteMaximo) {
			this.valorAtual = this.limiteMaximo;
		}
		else if(this.valorAtual < this.limiteMinimo) {
			this.valorAtual = this.limiteMinimo;
		}
		fireTrocaValorTemperaEvent(new TrocaValorTemperaEvent(valorAtual));
	}

	public double getLimiteMaximo() {
		return limiteMaximo;
	}

	public double getLimiteMinimo() {
		return limiteMinimo;
	}

	public double getStepValue() {
		return valorStep;
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
