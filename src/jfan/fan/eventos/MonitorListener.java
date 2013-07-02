package jfan.fan.eventos;

import java.util.EventListener;

public interface MonitorListener extends EventListener {
	
	void melhorAritmeticaSuperada(RedeSuperadaEvent e);
	
	void melhorHarmonicaSuperada(RedeSuperadaEvent e);
	void melhorMaxMinSuperada(RedeSuperadaEvent e);
	
	
}
