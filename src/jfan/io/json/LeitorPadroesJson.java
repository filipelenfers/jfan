package jfan.io.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import jfan.fan.padroes.Padrao;
import jfan.io.LeitorPadroes;

public class LeitorPadroesJson implements LeitorPadroes {

	@Override
	public void lerDados(File arquivo, List<Padrao> c) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(arquivo));
			StringBuilder jsonBuilder = new StringBuilder();
			String linha;
			while ((linha = br.readLine()) != null) {
				jsonBuilder.append(linha);
			}

			DeserializadorJson d = new DeserializadorJson();
			if (!jsonBuilder.toString().isEmpty()) {
				List<Padrao> lista = d.deserializarPadroes(
						jsonBuilder.toString(), c.getClass());
				for (Padrao padrao : lista) {
					c.add(padrao);
				}
			}
		} finally {
			if (br != null)
				br.close();
		}

	}

}
