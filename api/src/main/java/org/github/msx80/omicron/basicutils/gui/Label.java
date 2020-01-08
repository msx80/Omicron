package org.github.msx80.omicron.basicutils.gui;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.TextDrawer;

public class Label extends Widget {

	private String text;
	private TextDrawer font;
	private Sys sys;
	private int color;

	public Label(Sys sys, String text, TextDrawer font, int x, int y, int color)
	{
		super(x, y, font.width(text), font.height(), Padding.ZERO);
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

	@Override
	protected void click(int px, int py) {
		System.out.println("clicked label "+text+" at "+px+","+py);
		
	}

	public void setText(String string) {
		this.text = string;
		
	}

}
