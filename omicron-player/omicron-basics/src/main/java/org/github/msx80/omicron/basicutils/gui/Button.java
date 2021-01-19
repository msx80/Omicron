package org.github.msx80.omicron.basicutils.gui;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.ShapeDrawer;
import org.github.msx80.omicron.basicutils.TextDrawer;

public class Button extends BasicButton {

	private int borderColor = Colors.GREEN;
	private int backgroundColor = Colors.BLUE;
	private String text;
	private TextDrawer font;
	private Sys sys;

	public Button(Sys sys, String text, TextDrawer font, Event onClick)
	{
		super(font.width(text)+4, font.height()+4, onClick);
		this.text = text;
		this.font = font;
		this.sys = sys;
	}
	
	@Override
	public void draw() {
		sys.fill(0, 0, 0, w, h, backgroundColor);
		ShapeDrawer.outline(sys, 0, 0, w, h, 0, borderColor);
		font.print(text, 2,2);
	}


	

}
