package org.github.msx80.omicron.basicutils.gui;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.ShapeDrawer;
import org.github.msx80.omicron.basicutils.TextDrawer;

public class Button extends Widget implements Clickable {

	private String text;
	private TextDrawer font;
	private Sys sys;

	public Button(Sys sys, String text, TextDrawer font, int x, int y)
	{
		super(x, y, font.width(text), font.height());
		this.text = text;
		this.font = font;
		this.sys = sys;
	}
	
	@Override
	public void draw() {
		sys.fill(0, 0, 0, w, h, Colors.BLUE);
		ShapeDrawer.outline(sys, 0, 0, w, h, 0, Colors.GREEN);
		font.print(text, 0,0);
	}

	@Override
	public void click(int px, int py) {
		System.out.println("clicked button "+text+" at "+px+","+py);
		
	}
	
	

}
