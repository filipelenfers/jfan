package jfan.fan.padroes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class MapeadorClasses {
	
	/*public void mapearClasses(List<Padrao> padroes, Map<Integer, Integer> mapa) {
		
		for (Padrao padrao : padroes) {
			padrao.setClasse(mapa.get(padrao.getClasse()));
		}
		
	}*/
	
	/*public Map<String, Integer> gerarMapaClassesPadraoFAN(List<PadraoFAN> padroes) {
		Map<String, Integer> mapa = null;
		TreeSet<String> classes = new TreeSet<String>();

		// pegar todos os números.
		for (PadraoFAN padrao : padroes) {
			classes.add(padrao.getClasse());
		}

		// Verificar se começam em 0 e Verificar se estão ordenados de 1 em um
		int i = 0;
		boolean mapear = false;
		for (String classe : classes) {
			if (!Integer.toString(i).equals(classe)) {
				mapear = true;
				break;
			}
			i++;
		}

		if(mapear) {
			mapa = new HashMap<String, Integer>();
			i = 0;
			for (String classe : classes) {
				mapa.put(classe, i);
				i++;
			}
		}

		return mapa;
	}*/
	
	/*public void gerarMapaClassesPadraoFAN(List<PadraoFAN> padroes, Map<String, Integer> mapa) {
		 
		TreeSet<String> classes = new TreeSet<String>();

		// pegar todos os números.
		for (PadraoFAN padrao : padroes) {
			classes.add(padrao.getClasse());
		}

		// Verificar se começam em 0 e Verificar se estão ordenados de 1 em um
		int i = 0;
		boolean mapear = false;
		for (String classe : classes) {
			if (!Integer.toString(i).equals(classe)) {
				mapear = true;
				break;
			}
			i++;
		}

		if(mapear) {
			mapa = new HashMap<String, Integer>();
			i = 0;
			for (String classe : classes) {
				mapa.put(classe, i);
				i++;
			}
		}

	}*/
	

	public Map<String, Integer> gerarMapaClassesPadrao(List<Padrao> padroes) {
		Map<String, Integer> mapa = null;
		TreeSet<String> classes = new TreeSet<String>();

		// pegar todos os números.
		for (Padrao padrao : padroes) {
			classes.add(padrao.getClasse());
		}

		// Verificar se começam em 0 e Verificar se estão ordenados de 1 em um
		/*int i = 0;
		boolean mapear = false;
		for (String integer : classes) {
			if (!Integer.toString(i).equals(integer)) {
				mapear = true;
				break;
			}
			i++;
		}

		if(mapear) {*/
			mapa = new HashMap<String, Integer>();
			int i = 0;
			for (String integer : classes) {
				mapa.put(integer, i);
				i++;
			}
		//}

		return mapa;
	}
	
	public void gerarMapaClassesPadrao(List<Padrao> padroes, Map<String, Integer> mapa) {
		
		TreeSet<String> classes = new TreeSet<String>();

		// pegar todos os números.
		for (Padrao padrao : padroes) {
			classes.add(padrao.getClasse());
		}

		// Verificar se começam em 0 e Verificar se estão ordenados de 1 em um
		int i = 0;
		boolean mapear = false;
		for (String integer : classes) {
			if (!Integer.toString(i).equals(integer)) {
				mapear = true;
				break;
			}
			i++;
		}

		if(mapear) {
			mapa = new HashMap<String, Integer>();
			i = 0;
			for (String integer : classes) {
				mapa.put(integer, i);
				i++;
			}
		}

		
	}
	
	
	

}
