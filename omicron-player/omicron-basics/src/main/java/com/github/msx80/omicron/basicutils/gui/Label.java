package com.github.msx80.omicron.basicutils.gui;

import com.github.msx80.omicron.api.Sys;

import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.text.TextDrawer;

public class Label extends BaseWidget {

	private String text;
	private TextDrawer font;
	private Sys sys;	
	private int color;

	public Label(String text, int color, TextDrawer font, Sys sys)
	{
		super(font.width(text), font.height());
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
