package org.github.msx80.omicron.fantasyconsole;

import java.io.InputStream;

import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.api.SysConfig;
import org.github.msx80.omicron.api.adv.AdvancedSys;
import org.github.msx80.omicron.api.adv.CustomResourceGame;

public class SubGame implements Game, CustomResourceGame {

	private Game src;
	private Sys sys;
	
	
	public SubGame(Game src) {
		this.src = src;
	}

	@Override
	public SysConfig sysConfig() {
		return src.sysConfig();
	}

	@Override
	public void init(Sys sys) {
		this.sys = sys;
		src.init(sys);

	}

	@Override
	public boolean update() {
		return src.update();
	}

	@Override
	public void render() {
		src.render();
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		// load resources from the original file class, so that it looks on the right classloader
		return src.getClass().getResourceAsStream(name);
	}


	
}
