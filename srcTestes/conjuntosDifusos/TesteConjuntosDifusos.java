package conjuntosDifusos;

import static org.junit.Assert.*;
import jfan.fan.exceptions.PadraoNaoNormalizadoException;
import jfan.fan.fuzzy.FabricaFuncaoPertinencia;
import jfan.fan.fuzzy.FabricaFuncaoPertinenciaPadrao;
import jfan.fan.fuzzy.FuncaoPertinencia;
import jfan.fan.fuzzy.FuncoesPertinenciaEnum;
import jfan.fan.padroes.CaracteristicaFAN;
import jfan.fan.padroes.Padrao;
import jfan.fan.padroes.PadraoFAN;

import org.junit.Before;
import org.junit.Test;


public class TesteConjuntosDifusos {

	Padrao p1;
	int raio, suporte;
	FabricaFuncaoPertinencia fabricaFuncaoPertinencia;
	FuncaoPertinencia funcaoPertinencia;
	
	
	@Before
	public void setUp() throws Exception {
		p1 = new Padrao(new double[]{0.5,0.505},"1");
		raio = 3;
		suporte = 100;
		fabricaFuncaoPertinencia = new FabricaFuncaoPertinenciaPadrao();
		funcaoPertinencia = fabricaFuncaoPertinencia.criarFuncaoPertinencia(FuncoesPertinenciaEnum.TRIANGULAR.getID());
	}

	@Test
	public void testCreatePadraoFAN() throws PadraoNaoNormalizadoException {
		PadraoFAN p = p1.createPadraoFAN(raio, suporte, funcaoPertinencia);
		CaracteristicaFAN c = p.getCaracteristicaFAN(0);
		
		assertEquals("Erro no calculo do inicio",49 + raio - 2, c.getInicio(), 0);
		assertEquals("Erro no calculo do fim",52 + raio - 2, c.getFim(), 0);
		assertEquals("Erro no tamanho Array",4, c.getConjuntoDifuso().length, 0);
		
		double soma = 0.5 + 1.0 + 0.666666666666 + 0.3333333333;
		
		assertEquals(0.5/soma, c.getConjuntoDifuso()[0], 0.00001);
		assertEquals(1.0/soma, c.getConjuntoDifuso()[1], 0.00001);
		assertEquals(0.6666666/soma, c.getConjuntoDifuso()[2], 0.00001);
		assertEquals(0.3333333/soma, c.getConjuntoDifuso()[3], 0.00001);
		
		
		c = p.getCaracteristicaFAN(1);
		
		assertEquals("Erro no calculo do inicio 2",49 + raio - 2, c.getInicio(), 0);
		assertEquals("Erro no calculo do fim 2",52 + raio - 2, c.getFim(), 0);
		assertEquals("Erro no tamanho Array 2",4, c.getConjuntoDifuso().length, 0);
		soma = 0.4+0.8+0.4+0.8;
		
		assertEquals(0.4/soma, c.getConjuntoDifuso()[0], 0.00001);
		assertEquals(0.8/soma, c.getConjuntoDifuso()[1], 0.00001);
		//assertEquals(1.0/soma, c.getConjuntoDifuso()[2], 0.00001);
		assertEquals(0.8/soma, c.getConjuntoDifuso()[2], 0.00001);
		assertEquals(0.4/soma, c.getConjuntoDifuso()[3], 0.00001);
		
		
	}

}
