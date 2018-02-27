package net.lugato.fc.generic.demo;

import com.github.msx80.omicron.*;

public abstract class Item {

	Vector2 pos = new Vector2();
	Image text;
	boolean flip = false;
	public abstract void update();
}
