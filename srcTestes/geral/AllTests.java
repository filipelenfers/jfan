package geral;

import io.LeitorTxtPadroesTest;
import io.PadroesJsonTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import conjuntosDifusos.TesteConjuntosDifusos;

@RunWith(Suite.class)
@SuiteClasses({
	TesteAND.class,
	TesteConjuntosDifusos.class,
	LeitorTxtPadroesTest.class,
	PadroesJsonTest.class
})
public class AllTests {

	


}
