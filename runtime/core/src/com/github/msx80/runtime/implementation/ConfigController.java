package com.github.msx80.runtime.implementation;

import com.badlogic.gdx.controllers.Controller;
import com.github.msx80.omicron.Game;
import com.github.msx80.omicron.Sys;

public class ConfigController implements Game {

	private Controller controller;
	private MyControllerMapping mappings;
	private Sys sys;

	public ConfigController(Controller controller, MyControllerMapping mappings) {
		this.controller = controller;
		this.mappings = mappings;
	}

	@Override
	public void close() {
		

	}

	@Override
	public void init(Sys sys) {
		this.sys = sys;

	}

	@Override
	public void render() {
		sys.clearScreen(0, 0, 0);
		
		sys.write(null, 10,10,"Configuring new controller:");
		sys.color(1, 0, 0, 1);
		sys.write(null, 10,20,controller.getName());
		sys.color(1, 1, 1, 1);
		sys.write(null, 10,30,mappings.getStep());

	}

	@Override
	public void update() {
		if(!mappings.isConfigured())
		{
			mappings.checkRecord(controller);
		}
		else
		{
			// TODO after configuration, we could put a recap screen with "confirm/reconfigure" options
			sys.exit(null);
		}
	}

}
