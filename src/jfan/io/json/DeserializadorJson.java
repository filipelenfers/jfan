package jfan.io.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import jfan.fan.MonitorFAN;
import jfan.fan.NeuronioFAN;
import jfan.fan.RedeFAN;
import jfan.fan.normalizadores.Normalizador;
import jfan.fan.padroes.Padrao;

public class DeserializadorJson {
	
	public MonitorFAN deserializarMonitor(String json) {
		
		String jsonRede = getValor("redeAtual", json);
		RedeFAN redeAtual = deserializarRede(jsonRede);
				
		jsonRede = getValor("redeMelhorAritmetica", json);
		RedeFAN redeMelhorAritmetica = deserializarRede(jsonRede);
		
		jsonRede = getValor("redeMelhorHarmonica", json);
		RedeFAN redeMelhorHarmonica = deserializarRede(jsonRede);
		
		jsonRede = getValor("redeMelhorMaxMin", json);
		RedeFAN redeMelhorMaxMin = deserializarRede(jsonRede);
		
		double melhorAritmetica = Double.parseDouble(getValor("melhorAritmetica", json));
		double melhorHarmonica = Double.parseDouble(getValor("melhorHarmonica", json));
		double melhorMaxMin = Double.parseDouble(getValor("melhorMaxMin", json));
		
		int epoca = Integer.parseInt(getValor("epoca", json));
		int epocasStepTempera = Integer.parseInt(getValor("epocasStepTempera", json));
		int epocasReiniciarTempera = Integer.parseInt(getValor("epocasReiniciarTempera", json));
		int epocasEmbaralharConjuntoTreinamento = Integer.parseInt(getValor("epocasEmbaralharConjuntoTreinamento", json));
		
		
		String jsonNormalizador = getValor("normalizador", json);
		
		Normalizador normalizador = deserializarNormalizador(jsonNormalizador, redeAtual.getNumeroCaracteristicas());
		
		//Montar monitor		
		MonitorFAN monitor = new MonitorFAN(redeAtual);
		monitor.setNormalizador(normalizador);
		
		//Setar propriedades protected e privadas
		try {
			Field campo;

			//epoca
			campo = monitor.getClass().getDeclaredField("epoca");
			campo.setAccessible(true);
			campo.set(monitor, epoca);
			
			//epocasStepTempera
			campo = monitor.getClass().getDeclaredField("epocasStepTempera");
			campo.setAccessible(true);
			campo.set(monitor, epocasStepTempera);
			
			//epocasReiniciarTempera
			campo = monitor.getClass().getDeclaredField("epocasReiniciarTempera");
			campo.setAccessible(true);
			campo.set(monitor, epocasReiniciarTempera);
			
			//epocasEmbaralharConjuntoTreinamento
			campo = monitor.getClass().getDeclaredField("epocasEmbaralharConjuntoTreinamento");
			campo.setAccessible(true);
			campo.set(monitor, epocasEmbaralharConjuntoTreinamento);
			
			//melhorAritmetica
			campo = monitor.getClass().getDeclaredField("melhorAritmetica");
			campo.setAccessible(true);
			campo.set(monitor, melhorAritmetica);
			
			//melhorHarmonica
			campo = monitor.getClass().getDeclaredField("melhorHarmonica");
			campo.setAccessible(true);
			campo.set(monitor, melhorHarmonica);
			
			//melhorMaxMin
			campo = monitor.getClass().getDeclaredField("melhorMaxMin");
			campo.setAccessible(true);
			campo.set(monitor, melhorMaxMin);
			
			//redeMelhorAritmetica
			campo = monitor.getClass().getDeclaredField("redeMelhorAritmetica");
			campo.setAccessible(true);
			campo.set(monitor, redeMelhorAritmetica);
			
			//redeMelhorHarmonica
			campo = monitor.getClass().getDeclaredField("redeMelhorHarmonica");
			campo.setAccessible(true);
			campo.set(monitor, redeMelhorHarmonica);
			
			//redeMelhorMaxMin
			campo = monitor.getClass().getDeclaredField("redeMelhorMaxMin");
			campo.setAccessible(true);
			campo.set(monitor, redeMelhorMaxMin);
			
			
		} catch (IllegalArgumentException e) {
			 throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
		
		
		return monitor;
	}
	
	@SuppressWarnings("unchecked")
	public List<Padrao> deserializarPadroes(String jsonPadroes, Class<? extends List> classeIntanciar ) {
				
		try {
			List<Padrao> padroes = classeIntanciar.newInstance();
			String[] stringPadroes = getArray(jsonPadroes);
			Padrao p;
			double[]  caracteristicas;
			String[]  caracteristicasString;
			String classe;
			int i ;
			for(String s : stringPadroes) {
				i = 0;
				classe = getValor("classe", s);
				caracteristicasString = getArray("caracteristicas", s);
				caracteristicas = new double[caracteristicasString.length];
				for (String sCarac : caracteristicasString) {
					caracteristicas[i] = Double.parseDouble(sCarac);
					i++;
				}
				
				p = new Padrao(caracteristicas,classe);
				padroes.add(p);
			}
			
			return padroes;
			
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		
		
		
		
	}

	public RedeFAN deserializarRede(String jsonRede) {

		int pesoBase = Integer.parseInt(getValor("pesoBase", jsonRede));
		int numeroCaracteristicas = Integer.parseInt(getValor(
				"numeroCaracteristicas", jsonRede));
		int raioDifuso = Integer.parseInt(getValor("raioDifuso", jsonRede));
		int suporteConjuntosDifusos = Integer.parseInt(getValor(
				"suporteConjuntosDifusos", jsonRede));

		int numNeuronios = getNumeroNeuronios(jsonRede);

		String[] temp = getArray("pesosNeuronios", jsonRede);
		int[] pesosNeuronios = new int[temp.length];
		for (int i = 0; i < pesosNeuronios.length; i++) {
			pesosNeuronios[i] = Integer.parseInt(temp[i]);
		}

		double[][][] matrizesNeurais = getMatrizesNeurais("matrizNeural",
				jsonRede, numeroCaracteristicas, suporteConjuntosDifusos,
				raioDifuso, numNeuronios);
		double[][] somatorios = getSomatorios("somatorios", jsonRede,
				numNeuronios, numeroCaracteristicas);

		NeuronioFAN[] neuroniosFAN = new NeuronioFAN[somatorios.length];
		for (int i = 0; i < somatorios.length; i++) {
			neuroniosFAN[i] = new NeuronioFAN(somatorios[i], matrizesNeurais[i]);
		}

		RedeFAN rede = new RedeFAN(neuroniosFAN, pesosNeuronios, pesoBase,
				raioDifuso, suporteConjuntosDifusos, numeroCaracteristicas);

		return rede;

	}

	public Normalizador deserializarNormalizador(String jsonNormalizador, int numeroCarateristicas) {
		Normalizador norm = null;
		try {
		
		
		String classe = getValor("class", jsonNormalizador);
		
		if (classe.equals("jfan.fan.normalizadores.NormalizadorMax")) {

			String[] max = getArray("max", jsonNormalizador);
			
			double[] maxD = new double[max.length];
			
			for (int i = 0; i < max.length; i++) {
				maxD[i] = Double.parseDouble(max[i]);				
			}

			norm = (Normalizador) Class.forName(classe).getConstructor(maxD.getClass()).newInstance(maxD);
			
		}
		else if (classe.equals("jfan.fan.normalizadores.NormalizadorMaxMin")) {
			
			String[] max = getArray("max", jsonNormalizador);
			String[] min = getArray("min", jsonNormalizador);
			String[] maxMinusMin = getArray("maxMinusMin", jsonNormalizador);
			
			double[] maxD = new double[max.length];
			double[] minD = new double[max.length];
			double[] maxMinusMinD = new double[max.length];
			
			for (int i = 0; i < max.length; i++) {
				maxD[i] = Double.parseDouble(max[i]);				
			}
			
			for (int i = 0; i < min.length; i++) {
				minD[i] = Double.parseDouble(min[i]);				
			}
			
			for (int i = 0; i < maxMinusMin.length; i++) {
				maxMinusMinD[i] = Double.parseDouble(maxMinusMin[i]);				
			}
		
			norm = (Normalizador) Class.forName(classe).getConstructor(maxD.getClass(),minD.getClass()).newInstance(maxD,minD);
			
		} 
		else if (classe.equals("jfan.fan.normalizadores.NormalizadorMaxMean")) {
		
			String[] max = getArray("max", jsonNormalizador);
			String[] mean = getArray("mean", jsonNormalizador);
			
			double[] maxD = new double[max.length];
			double[] meanD = new double[max.length];
			
			for (int i = 0; i < max.length; i++) {
				maxD[i] = Double.parseDouble(max[i]);				
			}
			
			for (int i = 0; i < mean.length; i++) {
				meanD[i] = Double.parseDouble(mean[i]);				
			}
			
			norm = (Normalizador) Class.forName(classe).getConstructor(maxD.getClass(),meanD.getClass()).newInstance(maxD,meanD);
		}
				
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		
		
		return norm;
	}

	/**
	 * Pega o valor para uma chave, só funciona para valores numéricos.
	 * 
	 * @param chave
	 *            A chave.
	 * @param json
	 *            A string json aonde o valor será procurado.
	 * @return O valor específicado para chave.
	 */
	private String getValor(String chave, String json) {

		int i = json.indexOf(chave);
		i += chave.length() + (chave.equals("class") ? 1 : 2); // O nome da chave mais o ":
		int j;
		if (json.charAt(i) == '{') {
			//procurar todos os { (e contar) até achar o primeiro }. Dai procurar o número de } até a contagem. 
			int temp = i;
			j = json.indexOf("}", i);
			int count = 0;
			//TODO estudar melhor isso, aquele if -1 me parece desnecessário
			while(temp < j) {
				count++;
				temp = json.indexOf("{", temp+1);
				if(temp == -1){
					count = 0;
					break;
				}
			}
			
			for(int c = 0; c < count; c++) {
				j++;
				j = json.indexOf("}", j);
				
			}
			j++; // Pra pegar o }
		
		}
		else {
			j = json.indexOf(",", i);
		}

		// se for o último campo
		if (j < 0) {
			j = json.indexOf("}", i);
			if (j < 0) {
				j = json.length();
			}	
		}
		
		

		// Se for string
		if (json.charAt(j - 1) == '"') {
			j--;
			i++;
		}
		
		

		// System.out.println(json.substring(i, j));
		return json.substring(i, j);
	}

	private String[] getArray(String chave, String json) {

		int i = json.indexOf(chave);
		i += chave.length() + 3; // O nome da chave mais o ":[

		int j = json.indexOf("]", i);

		String[] arrString = json.substring(i, j).split(",");

		return arrString;
	}
	
	private String[] getArray(String json) {

		int i = json.indexOf("[");
		int j = json.lastIndexOf("]");

		String[] arrString = json.substring(i+2, j-1).split("\\},\\{");

		return arrString;
	}
	


	private double[][] getSomatorios(String chave, String json,
			int numNeuronios, int numCaracteristicas) {

		double[][] somatorios = new double[numNeuronios][numCaracteristicas];

		int i = json.indexOf(chave);
		int j;
		int neuronio = 0;

		while (i > 0) {

			i += chave.length() + 3; // O nome da chave mais o ":

			j = json.indexOf("]", i);

			String[] matrix = json.substring(i, j).split(",");

			for (int k = 0; k < matrix.length; k++) {
				somatorios[neuronio][k] = Double.parseDouble(matrix[k]);
			}

			// Próxima matriz
			i = json.indexOf(chave, j);
			neuronio++;
		}

		return somatorios;
	}

	private int getNumeroNeuronios(String json) {
		// Contar quantos neuronios.
		int numNeuronios = 0;
		int i = json.indexOf("jfan.fan.NeuronioFAN");
		while (i > 0) {

			numNeuronios++;

			i = json.indexOf("jfan.fan.NeuronioFAN", i
					+ "jfan.fan.NeuronioFAN".length());
		}

		return numNeuronios;
	}

	private double[][][] getMatrizesNeurais(String chave, String json,
			int numCaracteristicas, int suporteConjuntosDifusos,
			int raioDifuso, int numNeuronios) {

		// Calcular escape
		int escape = (raioDifuso << 2) - 2;
		if (escape < 0) {
			escape = 0;
		}

		double[][][] retorno = new double[numNeuronios][numCaracteristicas][suporteConjuntosDifusos
				+ escape];

		int i = json.indexOf(chave);
		int j;
		int neuronio = 0;
		int caracteristica;
		int arrValor;
		while (i > 0) {

			i += chave.length() + 4; // O nome da chave mais o ":

			j = json.indexOf("]]", i);

			caracteristica = 0;

			String[] matrix = json.substring(i, j).split("\\],\\[");
			String[] array;
			for (String s : matrix) {
				array = s.split(",");
				arrValor = 0;
				for (String ss : array) {
					retorno[neuronio][caracteristica][arrValor] = Double
							.parseDouble(ss);
					arrValor++;
				}
				caracteristica++;
			}

			// System.out.println(json.substring(i,j));

			// Próxima matriz
			i = json.indexOf(chave, j);
			neuronio++;
		}

		return retorno;
	}

	public static void main(String[] args) {
		String jsonRede = "{class:\"jfan.fan.RedeFAN\",\"pesoBase\":0,\"neuronios\":[{class:\"jfan.fan.NeuronioFAN\",\"matrizNeural\":[[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]],\"somatorios\":[0.0,0.0]},,{class:\"jfan.fan.NeuronioFAN\",\"matrizNeural\":[[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]],\"somatorios\":[0.0,0.0]},],\"pesosNeuronios\":[1000,1000],\"raioDifuso\":2,\"suporteConjuntosDifusos\":100,\"erros\":0,\"errosClasses\":null,\"errosClassesValidacao\":null,\"numeroCaracteristicas\":2}";

		DeserializadorJson d = new DeserializadorJson();
		RedeFAN rede = d.deserializarRede(jsonRede);

		String jsonNorm = "{class:\"jfan.fan.normalizadores.NormalizadorMax\",\"max\":[10.0,10.0]}";
		d.deserializarNormalizador(jsonNorm, rede.getNumeroCaracteristicas());
		
		String jsonMonitor = "{class:\"jfan.fan.MonitorFAN\",\"redeAtual\":{class:\"jfan.fan.RedeFAN\",\"pesoBase\":0,\"tempera\":null,\"neuronios\":[{class:\"jfan.fan.NeuronioFAN\",\"matrizNeural\":[[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,7.272727272727243,14.545454545454486,21.818181818181856,29.090909090908973,36.363636363636445,30.303030303030408,24.242424242424224,18.181818181818223,12.121212121212112,6.060606060606056,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,3.635041322314041,7.267438016528908,10.897190082644642,14.524297520661122,18.14876033057852,15.128558310376523,12.10651974288337,9.082644628099176,6.05693296602387,3.029384756657482,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,7.272727272727243,14.545454545454486,21.818181818181856,29.090909090908973,36.363636363636445,30.303030303030408,24.242424242424224,18.181818181818223,12.121212121212112,6.060606060606056,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,3.635041322314041,7.267438016528908,10.897190082644642,14.524297520661122,18.14876033057852,15.128558310376523,12.10651974288337,9.082644628099176,6.05693296602387,3.029384756657482,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]],\"somatorios\":[299.9999999999987,299.9999999999987]},{class:\"jfan.fan.NeuronioFAN\",\"matrizNeural\":[[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,3.6193641417935845,7.2054776586298575,10.759431623522996,14.282275145140325,17.775015367802748,14.866462437211277,11.937118914329023,8.986408032238238,6.013732786585274,3.018475935580867,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,3.607320811419976,7.156748309541681,10.648570999248697,14.08307738542446,17.46055597295267,14.649942955728108,11.799760692322668,8.909842223891813,5.980020591590835,3.0101288365751153,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]],\"somatorios\":[100.0000000000008,100.0000000000008]}],\"pesosNeuronios\":[1000,1000],\"raioDifuso\":6,\"suporteConjuntosDifusos\":100,\"numeroCaracteristicas\":2},\"melhorAritmetica\":1.0,\"redeMelhorAritmetica\":{class:\"jfan.fan.RedeFAN\",\"pesoBase\":0,\"tempera\":null,\"neuronios\":[{class:\"jfan.fan.NeuronioFAN\",\"matrizNeural\":[[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.14545454545454548,0.29090909090909095,0.43636363636363634,0.5818181818181819,0.7272727272727273,0.6060606060606061,0.4848484848484848,0.36363636363636365,0.2424242424242424,0.1212121212121212,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.14545454545454548,0.29090909090909095,0.43636363636363634,0.5818181818181819,0.7272727272727273,0.6060606060606061,0.4848484848484848,0.36363636363636365,0.2424242424242424,0.1212121212121212,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]],\"somatorios\":[299.9999999999987,299.9999999999987]},{class:\"jfan.fan.NeuronioFAN\",\"matrizNeural\":[[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]],\"somatorios\":[100.0000000000008,100.0000000000008]}],\"pesosNeuronios\":[1000,1000],\"raioDifuso\":6,\"suporteConjuntosDifusos\":100,\"numeroCaracteristicas\":2},\"eventoRedeAritmeticaSuperada\":null,\"melhorHarmonica\":1.0,\"redeMelhorHarmonica\":{class:\"jfan.fan.RedeFAN\",\"pesoBase\":0,\"tempera\":null,\"neuronios\":[{class:\"jfan.fan.NeuronioFAN\",\"matrizNeural\":[[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.14545454545454548,0.29090909090909095,0.43636363636363634,0.5818181818181819,0.7272727272727273,0.6060606060606061,0.4848484848484848,0.36363636363636365,0.2424242424242424,0.1212121212121212,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.14545454545454548,0.29090909090909095,0.43636363636363634,0.5818181818181819,0.7272727272727273,0.6060606060606061,0.4848484848484848,0.36363636363636365,0.2424242424242424,0.1212121212121212,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]],\"somatorios\":[299.9999999999987,299.9999999999987]},{class:\"jfan.fan.NeuronioFAN\",\"matrizNeural\":[[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]],\"somatorios\":[100.0000000000008,100.0000000000008]}],\"pesosNeuronios\":[1000,1000],\"raioDifuso\":6,\"suporteConjuntosDifusos\":100,\"numeroCaracteristicas\":2},\"eventoRedeHarmonicaSuperada\":null,\"melhorMaxMin\":1.0,\"redeMelhorMaxMin\":{class:\"jfan.fan.RedeFAN\",\"pesoBase\":0,\"tempera\":null,\"neuronios\":[{class:\"jfan.fan.NeuronioFAN\",\"matrizNeural\":[[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.14545454545454548,0.29090909090909095,0.43636363636363634,0.5818181818181819,0.7272727272727273,0.6060606060606061,0.4848484848484848,0.36363636363636365,0.2424242424242424,0.1212121212121212,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.14545454545454548,0.29090909090909095,0.43636363636363634,0.5818181818181819,0.7272727272727273,0.6060606060606061,0.4848484848484848,0.36363636363636365,0.2424242424242424,0.1212121212121212,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]],\"somatorios\":[299.9999999999987,299.9999999999987]},{class:\"jfan.fan.NeuronioFAN\",\"matrizNeural\":[[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.07140495867768595,0.14016528925619837,0.20628099173553716,0.2697520661157025,0.3305785123966942,0.2800734618916437,0.22773186409550045,0.17355371900826447,0.1175390266299357,0.05968778696051423,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]],\"somatorios\":[100.0000000000008,100.0000000000008]}],\"pesosNeuronios\":[1000,1000],\"raioDifuso\":6,\"suporteConjuntosDifusos\":100,\"numeroCaracteristicas\":2},\"eventoRedeMaxMinSuperada\":null,\"monitorListener\":null,\"normalizador\":{class:\"jfan.fan.normalizadores.NormalizadorMax\",\"max\":[10.0,10.0]},\"epoca\":100,\"epocasStepTempera\":0,\"epocasAcumuladasStepTempera\":100,\"epocasReiniciarTempera\":0,\"epocasAcumuladasReinicarTempera\":100,\"epocasEmbaralharConjuntoTreinamento\":0,\"epocasAcumuladasEmbaralharConjuntoTreinamento\":100}";
		d.deserializarMonitor(jsonMonitor);

	}

}
