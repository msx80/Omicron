package com.github.msx80.omicron;

public interface Game {

	void init(Sys sys);
	void update();
	void render();
	void close();

}
