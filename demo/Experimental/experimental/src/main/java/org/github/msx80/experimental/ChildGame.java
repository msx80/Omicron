package org.github.msx80.experimental;

import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.api.SysConfig;
import org.github.msx80.omicron.api.SysConfig.VirtualScreenMode;
import org.github.msx80.omicron.api.adv.AdvancedSys;

public class ChildGame implements Game {

	private AdvancedSys sys;
	int i = 0;

	@Override
	public SysConfig sysConfig() {
		
		return new SysConfig(256, 192, VirtualScreenMode.SCALED, "Child Game", "pippo");
	}

	@Override
	public void init(Sys sys) {
		this.sys = (AdvancedSys) sys;
	}

	@Override
	public boolean update() {
		if(sys.mouse().x<5) sys.quit("Closed child");
		return false;
	}

	@Override
	public void render() {
		i++;
		sys.clear((i << 8) | 0xFF );
	}

	@Override
	protected void finalize() throws Throwable {
		System.out.println("ChildGame FINALIZED!!! ************");
		super.finalize();
	}

	
}
