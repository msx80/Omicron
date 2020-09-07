package org.github.msx80.omicron.basicutils.gui;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.TextDrawer;

public class Label extends Widget {

	private String text;
	private TextDrawer font;
	private Sys sys;	
	private int color;

	public Label(String text, int x, int y, int color, TextDrawer font, Sys sys)
	{
		super(x, y, font.width(text), font.height());
		this.text = text;
		this.font = font;
		this.sys = sys;
		this.color = color;
	}
	
	@Override
	public void draw() {
		sys.color(this.color);
		font.print(text, 0,0);
		sys.color(Colors.WHITE);
	}

	public void setText(String string) {
		this.text = string;
		
	}

}
