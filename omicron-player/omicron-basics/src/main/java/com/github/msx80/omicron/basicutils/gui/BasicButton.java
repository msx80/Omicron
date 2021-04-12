package com.github.msx80.omicron.basicutils.gui;

public class BasicButton extends Widget implements Clickable {

	protected Event onClick;

	public BasicButton(int w, int h, Event onClick)
	{
		super(w, h);
		this.onClick = onClick;
	}
	
	@Override
	public void draw() {
	}

	@Override
	public void click(int px, int py) {
		if(onClick != null)
		{
			onClick.event(this);
		}
	}
	
	

}
