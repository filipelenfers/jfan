package jfan.console;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
import jfan.fan.padroes.PadraoFAN;
import jfan.fan.temperas.ITemperaSimulada;
import jfan.fan.temperas.TemperaSimuladaFAN;
import jfan.io.LeitorPadroes;
import jfan.io.LeitorRede;
import jfan.io.PersistorRede;
import jfan.io.json.LeitorRedeJson;
import jfan.io.json.PersistorRedeJson;
import jfan.io.txt.LeitorPadroesArquivoTexto;

public class ConsoleRunner {

	private static String trainingSetFile;
	private static String testSetFile;
	private static String validationSetFile;
	private static String outputDirectory;
	private static String fileSeparator = "  ";
	private static int radius = 6;
	private static int support = 100;
	private static NormalizadoresEnum normalizatorType = NormalizadoresEnum.MAX;
	private static long epochs = 1000;

	public static void main(String[] args) throws IOException,
			NumeroCarateristicasIncompativelException,
			PadraoNaoNormalizadoException {
		readParameters(args);
		java.util.Date data = (java.util.Date) Calendar.getInstance().getTime();
		String nomeArquivo = "" + data.getTime();
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
					+ trainingSetFile + " (Teste: "
					+ testSetFile + ")</h3> <br>\n");
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
				html.append("<p class='texto'> Épocas: " + c + " Tempo: "
						+ tempo + " min");
			} else {
				html.append("<p class='texto'> Épocas: " + c + " Tempo: "
						+ tempo + " seg");
			}
			html.append("<table class='texto'>\n");
			html.append("<tr><td>Melhor Máximo do Mínimo: </td><td>"
					+ monitor.getMelhorMaxMin() + "</td></tr>\n");
			html.append("<tr><td>Melhor Média Harmônica: </td><td>"
					+ monitor.getMelhorHarmonica() + "</td></tr>\n");

			html.append("</table>\n");

			//Inicio da validação
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

				// Máximo do Mínimo
				Resultado result = monitorMax.testar(
						conjuntosVl.getConjuntoValidacao(),
						monitor.getRedeMelhorMaxMin());


				String titulo = "Validação Rede Máximo do Mínimo " + nomeDatVl;
				html.append(montaHtml(result, titulo));

				MonitorFAN monitorHarm = new MonitorFAN(
						monitor.getRedeMelhorHarmonica());
				// Média Harmônica
				result = monitorHarm.testar(conjuntosVl.getConjuntoValidacao(),
						monitor.getRedeMelhorHarmonica());

				titulo = "Validação Rede Média Harmônica " + nomeDatVl;
				html.append(montaHtml(result, titulo));

				MonitorFAN monitorArit = new MonitorFAN(
						monitor.getRedeMelhorAritmetica());
				// Média Aritmética
				result = monitorArit.testar(conjuntosVl.getConjuntoValidacao(),
						monitor.getRedeMelhorAritmetica());

			//Fim da validação

			PersistorRede persist = new PersistorRedeJson();

			String redeTrHarmon = outputDirectory + nomeArquivo + "_"
					+ trainingSetFile + "_Harmonica.json";
			nomesRedes.add(redeTrHarmon);
			String redeTrMaxMin = outputDirectory + nomeArquivo + "_"
					+ trainingSetFile + "_MaxMin.json";
			nomesRedes.add(redeTrMaxMin);

			persist.persistir(monitor.getRedeMelhorHarmonica(),
					monitor.getNormalizador(), new File(redeTrHarmon));
			persist.persistir(monitor.getRedeMelhorMaxMin(),
					monitor.getNormalizador(), new File(redeTrMaxMin));

		//fim treinamento
			
		html.append("</html>");

		FileWriter w = new FileWriter(outputDirectory + nomeArquivo
				+ "_Resultado.html");
		w.write(html.toString());
		w.close();

		// INICIA VALIDAÇÃO DOS TREINAMENTOS
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

				String titulo = indice + ") Validação ("
						+ NOME_ARQUIVO_CLASSIFICACAO + ") - Rede: "
						+ nomesRedes.get(i);
				htmlVl.append(montaHtmlValidacao(
						conjuntosVl.getConjuntoValidacao(), padroesCloneVl,
						titulo));
			}
			htmlVl.append("</html>");

			FileWriter wVl = new FileWriter(DIRETORIO + nomeArquivo
					+ "_Validação.html");
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
		}

	}
	
	private static String montarMetricasHtml(int[][] matrix) {

		NumberFormat pr = NumberFormat.getPercentInstance(new Locale("pt-br"));
		pr.setMaximumFractionDigits(2);

		int truePositives = matrix[1][1]; // fraudeApontadaComoFraude
		int falsePositives = matrix[1][0]; // fraudeApontadaComoNormal
		int falseNegatives = matrix[0][1]; // normalApontadaComoFraude

		double precision = Metricas.precision(truePositives, falsePositives);
		double recall = Metricas.recall(truePositives, falseNegatives);
		double fscore = Metricas.fScore(truePositives, falsePositives,
				falseNegatives);

		StringBuilder html = new StringBuilder();
		html.append("<br><table width='100px'  class='texto' border='1' cellspacing='0' cellpading='0'>\n");
		html.append("<tr><td colspan='2' align='center' nowrap><b>Métricas</b></tr>\n");
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
		html.append("<tr><td class='texto'>Máximo do Mínimo: </td><td class='texto'>"
				+ result.getAcertoMaxMin() + "</td></tr>\n");
		html.append("<tr><td class='texto'>Média Harmônica: </td><td class='texto'>"
				+ result.getAcertoHarmonico() + "</td></tr>\n");
		// html.append("<tr><td class='texto'>Média Aritmética: </td><td class='texto'>"+result.getAcertoArimetico()+"</td></tr>\n");
		html.append("</table>\n");

		int[][] matrix = result.getMatrizConfusao();

		int[][] matrixRevert = new int[2][2];
		matrixRevert[0][0] = matrix[0][0];
		matrixRevert[1][1] = matrix[1][0];
		matrixRevert[1][0] = matrix[1][1];
		matrixRevert[0][1] = matrix[0][1];

		html.append(montarMetricasHtml(matrixRevert));

		html.append("<table width='300px'  class='texto' border='1' cellspacing='0' cellpading='0'>\n");
		html.append("<tr><td align='center'><b>Matriz de Confusão </b></td><td align='center'><b>0</td><td align='center'><b>1</td></tr>\n");
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

		// Métricas
		html.append(montarMetricasHtml(matrixQtde));

		html.append("<table width='300px'  class='texto' border='1' cellspacing='0' cellpading='0'>\n");
		html.append("<tr><td align='center' nowrap><b>Matriz de Confusão Quantidade</b></td><td align='center' nowrap><b>Normal Rede</td><td align='center' nowrap><b>Fraude Rede</td></tr>\n");
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
		html.append("<tr><td align='center' nowrap><b>Matriz de Confusão Porcentagem</b></td><td align='center' nowrap><b>Normal Rede</td><td align='center' nowrap><b>Fraude Rede</td></tr>\n");
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
