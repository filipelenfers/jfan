package jfan.fan.temperas.eventos;

import java.util.EventListener;

public interface TrocaValorTemperaListener extends EventListener {
	public void trocouValorTempera(TrocaValorTemperaEvent event);
}
