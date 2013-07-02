package jfan.fan.fuzzy;

public enum FuncoesPertinenciaEnum {
	TRIANGULAR(1);
	
	private int id;
	
	FuncoesPertinenciaEnum(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
}
