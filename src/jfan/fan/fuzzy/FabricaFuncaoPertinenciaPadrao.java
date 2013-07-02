package jfan.fan.fuzzy;


public class FabricaFuncaoPertinenciaPadrao implements FabricaFuncaoPertinencia {

	@Override
	public FuncaoPertinencia criarFuncaoPertinencia(int id) {
		FuncaoPertinencia f;
		
		if (id == 1) {
			f = new FuncaoPertinenciaTriangular();
		} else {
			f = null;
		}
		return f;
	}

}
