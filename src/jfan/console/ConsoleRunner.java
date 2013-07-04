package jfan.console;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;

import jfan.fan.MonitorFAN;
import jfan.fan.Resultado;
import jfan.fan.conjuntos.GerenciadorConjuntos;
import jfan.fan.exceptions.NumeroCarateristicasIncompativelException;
import jfan.fan.exceptions.PadraoNaoNormalizadoException;
import jfan.fan.fuzzy.FabricaFuncaoPertinencia;
import jfan.fan.fuzzy.FabricaFuncaoPertinenciaPadrao;
import jfan.fan.fuzzy.FuncaoPertinencia;
import jfan.fan.fuzzy.FuncoesPertinenciaEnum;
import jfan.fan.normalizadores.FabricaNormalizador;
import jfan.fan.normalizadores.FabricaNormalizadorPadrao;
import jfan.fan.normalizadores.Normalizador;
import jfan.fan.normalizadores.NormalizadorConfig;
import jfan.fan.normalizadores.NormalizadoresEnum;
import jfan.fan.padroes.Padrao;
import jfan.io.LeitorPadroes;
import jfan.io.PersistorRede;
import jfan.io.json.PersistorRedeJson;
import jfan.io.txt.LeitorPadroesArquivoTexto;

public class ConsoleRunner {

	private static String trainingSetFile;
	private static String testSetFile;
	private static String validationSetFile;
	private static String outputDirectory;
	private static String fileSeparator = ";";
	private static int radius = 6;
	private static int support = 100;
	private static NormalizadoresEnum normalizatorType = NormalizadoresEnum.MAX;
	private static long epochs = 1000;

	public static void main(String[] args) throws IOException,
			NumeroCarateristicasIncompativelException,
			PadraoNaoNormalizadoException {
		readParameters(args);
		ArrayList<String> nomesRedes = new ArrayList<String>();

		// 7 - Rodar a rede
		GerenciadorConjuntos conjuntos;
		NormalizadorConfig configNorm;

		FabricaNormalizador fabricaNormalizador;
		Normalizador normalizador;
		FabricaFuncaoPertinencia fabricaFuncaoPertinencia;
		FuncaoPertinencia funcaoPertinencia;

		LinkedList<Padrao> padroes = new LinkedList<Padrao>();

		fabricaFuncaoPertinencia = new FabricaFuncaoPertinenciaPadrao();
		funcaoPertinencia = fabricaFuncaoPertinencia
				.criarFuncaoPertinencia(FuncoesPertinenciaEnum.TRIANGULAR
						.getID());

		StringBuilder html = new StringBuilder();
		html.append("<html><body><style>body{	font-family:Calibri;	font-size:15px;}.texto{    font-size:13px;}</style>");

			System.out.println("Treinando: " + trainingSetFile);

			html.append("<hr><h3>Treinamento "
					+ trainingSetFile + " </br>Teste: "
					+ testSetFile + "</br> Valida&ccedil;&atilde;o: " + validationSetFile + "</h3> <br>\n");
			conjuntos = new GerenciadorConjuntos();
			conjuntos.setRaioDifuso(radius);
			conjuntos.setFuncaoPertinencia(funcaoPertinencia);
			conjuntos.setSuporteConjuntosDifusos(support);

			padroes = montarPadroes(trainingSetFile);
			configNorm = NormalizadorConfig
					.getNormalizadorConfiguration(padroes);
			fabricaNormalizador = new FabricaNormalizadorPadrao(configNorm);
			normalizador = fabricaNormalizador
					.criarNormalizador(normalizatorType.getID());

			// dados normalizados
			normalizador.normalizar(padroes);
			conjuntos.addPadroesConjuntoTreinamento(padroes);

			padroes = montarPadroes(testSetFile);
			configNorm = NormalizadorConfig
					.getNormalizadorConfiguration(padroes);
			fabricaNormalizador = new FabricaNormalizadorPadrao(configNorm);
			normalizador = fabricaNormalizador
					.criarNormalizador(normalizatorType.getID());

			// dados normalizados
			normalizador.normalizar(padroes);
			conjuntos.addPadroesConjuntoTeste(padroes);

			MonitorFAN monitor = new MonitorFAN(radius, support, padroes.get(0)
					.getQuantasCaracteristicas(), 2);
			monitor.setNormalizador(normalizador);
			int c = 0;
			Calendar calendario = Calendar.getInstance();

			long milisArmazenado = calendario.getTimeInMillis();


			for (c = 0; c < epochs; c++) {

				if (c % 2 == 0) {
					Collections.shuffle(conjuntos.getConjuntoTreinamento());
				}

				

				monitor.treinar(conjuntos.getConjuntoTreinamento(),
						conjuntos.getConjuntoTeste());

			}

			calendario = Calendar.getInstance();
			double milisAtual = calendario.getTimeInMillis();
			double tempo = (milisAtual - milisArmazenado) / 1000;

			if (tempo > 59) {
				tempo = tempo / 60;
				html.append("<p class='texto'> &eacute;pocas: " + c + " Tempo: "
						+ tempo + " min");
			} else {
				html.append("<p class='texto'> &eacute;pocas: " + c + " Tempo: "
						+ tempo + " seg");
			}
			html.append("<table class='texto'>\n");
			html.append("<tr><td>Melhor M&aacute;ximo do M&iacute;nimo: </td><td>"
					+ monitor.getMelhorMaxMin() + "</td></tr>\n");
			html.append("<tr><td>Melhor M&eacute;dia Harm&ocirc;nica: </td><td>"
					+ monitor.getMelhorHarmonica() + "</td></tr>\n");

			html.append("</table>\n");

			//Inicio da valida&ccedil;&atilde;o
				String nomeDatVl = "";
				nomeDatVl = validationSetFile;

				GerenciadorConjuntos conjuntosVl = new GerenciadorConjuntos();
				conjuntosVl.setRaioDifuso(radius);
				conjuntosVl.setFuncaoPertinencia(funcaoPertinencia);
				conjuntosVl.setSuporteConjuntosDifusos(support);

				LinkedList<Padrao> padroesVl = montarPadroes(nomeDatVl);
				// configNorm =
				// NormalizadorConfig.getNormalizadorConfiguration(padroesVl);
				fabricaNormalizador = new FabricaNormalizadorPadrao(configNorm);
				normalizador = fabricaNormalizador
						.criarNormalizador(normalizatorType.getID());

				normalizador.normalizar(padroesVl);
				conjuntosVl.addPadroesConjuntoValidacao(padroesVl);

				MonitorFAN monitorMax = new MonitorFAN(
						monitor.getRedeMelhorMaxMin());

				// M&aacute;ximo do M&iacute;nimo
				Resultado result = monitorMax.testar(
						conjuntosVl.getConjuntoValidacao(),
						monitor.getRedeMelhorMaxMin());


				String titulo = "Valida&ccedil;&atilde;o Rede M&aacute;ximo do M&iacute;nimo ";
				html.append(montaHtml(result, titulo));

				MonitorFAN monitorHarm = new MonitorFAN(
						monitor.getRedeMelhorHarmonica());
				// M&eacute;dia Harm&ocirc;nica
				result = monitorHarm.testar(conjuntosVl.getConjuntoValidacao(),
						monitor.getRedeMelhorHarmonica());

				titulo = "Valida&ccedil;&atilde;o Rede M&eacute;dia Harm&ocirc;nica ";
				html.append(montaHtml(result, titulo));

				MonitorFAN monitorArit = new MonitorFAN(
						monitor.getRedeMelhorAritmetica());
				// M&eacute;dia Aritm&eacute;tica
				result = monitorArit.testar(conjuntosVl.getConjuntoValidacao(),
						monitor.getRedeMelhorAritmetica());

			//Fim da valida&ccedil;&atilde;o

			PersistorRede persist = new PersistorRedeJson();

			String redeTrHarmon = outputDirectory + "/FanHarmonica.json";
			nomesRedes.add(redeTrHarmon);
			String redeTrMaxMin = outputDirectory + "/FanMaxMin.json";
			nomesRedes.add(redeTrMaxMin);

			persist.persistir(monitor.getRedeMelhorHarmonica(),
					monitor.getNormalizador(), new File(redeTrHarmon));
			persist.persistir(monitor.getRedeMelhorMaxMin(),
					monitor.getNormalizador(), new File(redeTrMaxMin));

		//fim treinamento
			
		html.append("</html>");

		FileWriter w = new FileWriter(outputDirectory + "/Resultado.html");
		w.write(html.toString());
		w.close();

		// INICIA VALIDA&ccedil;&atilde;O DOS TREINAMENTOS
		/*if (GERARCLASSIFICACAO) {

			StringBuilder htmlVl = new StringBuilder();
			htmlVl.append("<html><body><style>body{	font-family:Calibri;	font-size:15px;}.texto{    font-size:13px;}</style>");

			for (int i = 0; i < nomesRedes.size(); i++) {

				System.out.println("Validando: " + nomesRedes.get(i));

				LeitorRede leitorRede = new LeitorRedeJson();
				MonitorFAN monitor = leitorRede
						.ler(new File(nomesRedes.get(i)));
				LinkedList<Padrao> padroesVl = montarPadroes(NOME_ARQUIVO_CLASSIFICACAO);
				LinkedList<Padrao> padroesCloneVl = new LinkedList<Padrao>();
				for (Padrao padrao : padroesVl) {
					padroesCloneVl.add(padrao.clone());
				}

				GerenciadorConjuntos conjuntosVl = new GerenciadorConjuntos();

				conjuntosVl.setRaioDifuso(raio);
				conjuntosVl.setFuncaoPertinencia(funcaoPertinencia);
				conjuntosVl.setSuporteConjuntosDifusos(suporte);

				normalizador = monitor.getNormalizador();
				normalizador.normalizar(padroesVl);

				conjuntosVl.addPadroesConjuntoValidacao(padroesVl);

				for (PadraoFAN padraoFAN : conjuntosVl.getConjuntoValidacao()) {
					padraoFAN.setClasse(-1);
				}

				monitor.classificar(conjuntosVl.getConjuntoValidacao());

				int indice = i + 1;

				String titulo = indice + ") Valida&ccedil;&atilde;o ("
						+ NOME_ARQUIVO_CLASSIFICACAO + ") - Rede: "
						+ nomesRedes.get(i);
				htmlVl.append(montaHtmlValidacao(
						conjuntosVl.getConjuntoValidacao(), padroesCloneVl,
						titulo));
			}
			htmlVl.append("</html>");

			FileWriter wVl = new FileWriter(DIRETORIO + nomeArquivo
					+ "_Valida&ccedil;&atilde;o.html");
			wVl.write(htmlVl.toString());
			wVl.close();
		}*/

		System.out.println("ThE eNd");
	}

	public static void readParameters(String[] args) {
		for (int i = 0; i < args.length; i += 2) {
			String parameterType = args[i];
			String parameterValue = args[i + 1];
			readParameter(parameterType, parameterValue);
		}
	}

	private static void readParameter(String parameterType,
			String parameterValue) {
		switch (parameterType) {
		case "-t":
			trainingSetFile = parameterValue;
			break;
		case "-s":
			testSetFile = parameterValue;
			break;
		case "-l":
			validationSetFile = parameterValue;
			break;
		case "-o":
			outputDirectory = parameterValue;
			break;
		case "-p":
			fileSeparator = parameterValue;
			break;
		case "-r":
			radius = Integer.parseInt(parameterValue);
			break;
		case "-u":
			support = Integer.parseInt(parameterValue);
			break;
		case "-e":
			epochs = Integer.parseInt(parameterValue);
			break;
		}

	}
	
	private static String montarMetricasHtml(int[][] matrix) {

		NumberFormat pr = NumberFormat.getPercentInstance(new Locale("pt-br"));
		pr.setMaximumFractionDigits(2);

		int truePositives = matrix[1][1];
		int falsePositives = matrix[1][0];
		int falseNegatives = matrix[0][1];

		double precision = Metricas.precision(truePositives, falsePositives);
		double recall = Metricas.recall(truePositives, falseNegatives);
		double fscore = Metricas.fScore(truePositives, falsePositives,
				falseNegatives);

		StringBuilder html = new StringBuilder();
		html.append("<br><table width='100px'  class='texto' border='1' cellspacing='0' cellpading='0'>\n");
		html.append("<tr><td colspan='2' align='center' nowrap><b>M&eacute;tricas</b></tr>\n");
		html.append("<tr><td align='center' nowrap><b>Precison</td><td align='center'>"
				+ pr.format(precision) + "</td></tr>\n");
		html.append("<tr><td align='center' nowrap><b>Recall</td><td align='center'>"
				+ pr.format(recall) + "</td></tr>\n");
		html.append("<tr><td align='center' nowrap><b>F-Score</td><td align='center'>"
				+ pr.format(fscore) + "</td></tr>\n");
		html.append("</table>\n");
		html.append("<br><br>\n");

		return html.toString();

	}

	private static String montaHtml(Resultado result, String titulo) {

		StringBuilder html = new StringBuilder();
		html.append("<p><b> " + titulo + "</b></p>\n");
		html.append("<table>\n");
		html.append("<tr><td class='texto'>M&aacute;ximo do M&iacute;nimo: </td><td class='texto'>"
				+ result.getAcertoMaxMin() + "</td></tr>\n");
		html.append("<tr><td class='texto'>M&eacute;dia Harm&ocirc;nica: </td><td class='texto'>"
				+ result.getAcertoHarmonico() + "</td></tr>\n");
		// html.append("<tr><td class='texto'>M&eacute;dia Aritm&eacute;tica: </td><td class='texto'>"+result.getAcertoArimetico()+"</td></tr>\n");
		html.append("</table>\n");

		int[][] matrix = result.getMatrizConfusao();

		int[][] matrixRevert = new int[2][2];
		matrixRevert[0][0] = matrix[0][0];
		matrixRevert[1][1] = matrix[1][0];
		matrixRevert[1][0] = matrix[1][1];
		matrixRevert[0][1] = matrix[0][1];

		html.append(montarMetricasHtml(matrixRevert));

		html.append("<table width='300px'  class='texto' border='1' cellspacing='0' cellpading='0'>\n");
		html.append("<tr><td align='center'><b>Matriz de Confus&atilde;o </b></td><td align='center'><b>0</td><td align='center'><b>1</td></tr>\n");
		html.append("<tr><td align='center'><b>0</td><td align='center'>"
				+ matrix[0][0] + "</td><td align='center'>" + matrix[0][1]
				+ "</td></tr>\n");
		html.append("<tr><td align='center'><b>1</td><td align='center'>"
				+ matrix[1][1] + "</td><td align='center'>" + matrix[1][0]
				+ "</td></tr>\n");
		html.append("</table>\n");

		return html.toString();
	}

	/*private static String montaHtmlValidacao(List<PadraoFAN> conjuntoValidacao,
			List<Padrao> padroesCloneVl, String titulo) {

		StringBuilder html = new StringBuilder();
		int matrixQtde[][] = new int[2][2];

		for (int i = 0; i < conjuntoValidacao.size(); i++) {

			// Real 0
			if (Double.parseDouble(padroesCloneVl.get(i).getClasse()) == 0) {

				if (conjuntoValidacao.get(i).getClasse() == 0) {
					// Rede 0
					matrixQtde[0][0]++;
				} else {
					// Rede 1
					matrixQtde[0][1]++;
				}
			} else { // Real 1

				if (conjuntoValidacao.get(i).getClasse() == 0) {
					// Rede 0
					matrixQtde[1][0]++;
				} else {
					// Rede 1
					matrixQtde[1][1]++;
				}
			}
		}

		NumberFormat pr = NumberFormat.getPercentInstance(new Locale("pt-br"));
		pr.setMaximumFractionDigits(2);

		html.append("<p><b> " + titulo + "</b></p>\n");

		// M&eacute;tricas
		html.append(montarMetricasHtml(matrixQtde));

		html.append("<table width='300px'  class='texto' border='1' cellspacing='0' cellpading='0'>\n");
		html.append("<tr><td align='center' nowrap><b>Matriz de Confus&atilde;o Quantidade</b></td><td align='center' nowrap><b>Normal Rede</td><td align='center' nowrap><b>Fraude Rede</td></tr>\n");
		html.append("<tr><td align='center' nowrap><b>0 Real</td><td align='center'>"
				+ matrixQtde[0][0]
				+ "</td><td align='center'>"
				+ matrixQtde[0][1] + "</td></tr>\n");
		html.append("<tr><td align='center' nowrap><b>1 Real</td><td align='center'>"
				+ matrixQtde[1][0]
				+ "</td><td align='center'>"
				+ matrixQtde[1][1] + "</td></tr>\n");
		html.append("</table>\n");

		html.append("<br><br><table width='300px'  class='texto' border='1' cellspacing='0' cellpading='0'>\n");
		html.append("<tr><td align='center' nowrap><b>Matriz de Confus&atilde;o Porcentagem</b></td><td align='center' nowrap><b>Normal Rede</td><td align='center' nowrap><b>Fraude Rede</td></tr>\n");
		html.append("<tr><td align='center' nowrap><b>0 Real</td><td align='center'>"
				+ pr.format(matrixQtde[0][0] * 1.0
						/ (matrixQtde[0][0] + matrixQtde[0][1]))
				+ "</td><td align='center'>"
				+ pr.format(matrixQtde[0][1] * 1.0
						/ (matrixQtde[0][0] + matrixQtde[0][1]))
				+ "</td></tr>\n");
		html.append("<tr><td align='center' nowrap><b>1 Real</td><td align='center'>"
				+ pr.format(matrixQtde[1][0] * 1.0
						/ (matrixQtde[1][0] + matrixQtde[1][1]))
				+ "</td><td align='center'>"
				+ pr.format(matrixQtde[1][1] * 1.0
						/ (matrixQtde[1][0] + matrixQtde[1][1]))
				+ "</td></tr>\n");
		html.append("</table>\n");

		return html.toString();
	}*/

	private static LinkedList<Padrao> montarPadroes(String nomeArquivo)
			throws IOException {
		LinkedList<Padrao> padroes = new LinkedList<Padrao>();

		LeitorPadroes leitor;
		leitor = new LeitorPadroesArquivoTexto(fileSeparator);

		File f = new File(nomeArquivo);
		leitor.lerDados(f, padroes);

		return padroes;
	}

}
