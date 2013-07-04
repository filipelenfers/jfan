package jfan.console;

public class Metricas {

	/**
	 * 
	 * @param truePositives Quantidade (da menor classe representada) 
	 * @param falsePositives Quantidade (da menor classe representada) 
	 * @return 
	 * 
	 *    True Positive  | False Positive                 
	 *    _______________________________
	 *    
	 *    False Negative | True Negative
	 *    
	 *    Formula:
	 *    
	 *    True Positive / True Positive + False Positive
	 *
	 */
	public static double precision(int truePositives, int falsePositives)
	{
		return (double)truePositives / ((double)truePositives + (double)falsePositives);
	}


	
	/**
	 * 
	 * @param truePositives Quantidade (da menor classe representada)
	 * @param falseNegative Quantidade (da menor classe representada)
	 * @return 
	 * 
	 *    True Positive  | False Positive
	 *    _______________________________
	 *    
	 *    False Negative | True Negative
	 *    
	 *    Formula:
	 *    
	 *    True Positive / True Positive + False Positive
	 *
	 */
	public static double recall(int truePositives, int falseNegative)
	{
		return (double)truePositives / ((double)truePositives + (double)falseNegative);
	}
	
	
	/**
	 * 
	 * @param truePositives Quantidade (da menor classe representada) 
	 * @param falsePositives Quantidade (da menor classe representada) 
	 * @param falseNegative Quantidade (da menor classe representada) 
	 * @return 
	 * 
	 *    True Positive  | False Positive
	 *    _______________________________
	 *    
	 *    False Negative | True Negative
	 *    
	 *    Formula:
	 *    
	 *   F1-Score = 2 * ((P*R)/(P+R))     
	 *
	 */
	public static double fScore(int truePositives, int falsePositives,  int falseNegative)
	{
		
		double p = precision(truePositives, falsePositives);
		double r = recall(truePositives, falseNegative);
		
		return 2 * ((p*r) / (p + r));
	}
	

	
	

}