package jfan.fan.normalizadores;

public enum NormalizadoresEnum {
	MAX(1),MAXMIN(2),MAXMEAN(3);
	
	private int id;
	
	NormalizadoresEnum(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
}
