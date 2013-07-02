package jfan.fan.normalizadores;

public abstract class FabricaNormalizador   {

	protected NormalizadorConfig config;
	
	public FabricaNormalizador(NormalizadorConfig config) {
		this.config = config;
	}
			
	public abstract Normalizador criarNormalizador(int id);
	
	
	
}
