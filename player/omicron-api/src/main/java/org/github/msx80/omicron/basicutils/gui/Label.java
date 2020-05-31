package org.github.msx80.omicron.basicutils.gui;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.TextDrawer;
import org.github.msx80.omicron.basicutils.gui.drawers.Style;
import org.github.msx80.omicron.basicutils.gui.drawers.WidgetDrawer;

public class Label extends Widget {

	private String text;
	private TextDrawer font;
	private Sys sys;	
	private int color;

	public Label(String text, int x, int y, int color, Style drawer)
	{
		super(x, y, drawer.td.width(text), drawer.td.height(), Padding.ZERO);
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
