package jfan.fan.eventos;

import jfan.fan.MonitorFAN;
import jfan.fan.RedeFAN;

public class RedeSuperadaEvent  {
	
	protected MonitorFAN monitorFAN;
	protected RedeFAN rede;

	public RedeFAN getRede() {
		return rede;
	}

	public RedeSuperadaEvent(MonitorFAN monitorFAN, RedeFAN rede) {
		this.monitorFAN = monitorFAN;
		this.rede = rede;
	}

	public MonitorFAN getMonitorFAN() {
		return monitorFAN;
	}

	
	

}
