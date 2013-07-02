package jfan.fan.temperas;

import jfan.fan.temperas.eventos.TrocaValorTemperaEvent;
import jfan.fan.temperas.eventos.TrocaValorTemperaListener;

public interface ITemperaSimulada {
	
	/**
	 * A implementa��o deste m�todo deve retornar o valor atual
	 * da t�mpera.
	 * @return o valor atual da t�mpera
	 */
	public double getValor();
	
	/**
	 * O menor valor que a T�mpera Simulada pode fornecer.
	 * @param d o valor do limite.
	 */
	public void setLimiteMinimo(double d);
	
	/**
	 * O maior valor que a T�mpera Simulada pode fornecer.
	 * @param d o valor do limite.
	 */
	public void setLimiteMaximo(double d);
	
	/**
	 * Define o step da t�mpera, de quanto em quanto se aumenta a t�mpera,
	 * ou diminui a t�mpera no caso de par�metro negativo.
	 * @param d o valor do step.
	 */
	public void setStep(double d);
	
	/**
	 * A implementa��o desse m�todo dever fazer a t�mpera adicionar o valor
	 * definido como "step" ao valor atual da t�mpera. 
	 */
	public void step();
	
	/**
	 * A implementa��o deste m�todo deve fazer a t�mpera ter seu
	 * valor atual igual ao inicial.
	 */
	public void reset();
	
	public double getLimiteMaximo();
	
	public double getLimiteMinimo();
	
	public double getStepValue();
	
	public void addTrocaValorTemperaListener(TrocaValorTemperaListener listener);

	public void removeTrocaValorTemperaListener(TrocaValorTemperaListener listener);
		
	public void fireTrocaValorTemperaEvent(TrocaValorTemperaEvent event);
	
	
}
