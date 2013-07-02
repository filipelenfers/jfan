package jfan.fan.normalizadores;

public class FabricaNormalizadorPadrao extends FabricaNormalizador {

	public FabricaNormalizadorPadrao(NormalizadorConfig config) {
		super(config);
	}

	@Override
	public Normalizador criarNormalizador(int id) {
		Normalizador n;
		
		switch (id) {
		case 1:
			n = new NormalizadorMax(super.config);
			break;
		case 2:
			n = new NormalizadorMaxMin(super.config);
			break;
		case 3:
			n = new NormalizadorMaxMean(super.config);
			break;
		default:
			n = null;
			break;
		}
		return n;
		
	}

}
