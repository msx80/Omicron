package com.github.msx80.omicron.basicutils.text.richtext;

import com.github.msx80.omicron.basicutils.text.TextDrawer.Align;

public class ColoredString implements RichtextItem {
	public final int color;
	public final String text;
	
	public ColoredString(int color, String text) {
		this.color = color;
		this.text = text;
	}

	@Override
	public String toString() {
		return "["+color + "]" + text;
	}

	@Override
	public int width(RichtextDrawingContext ctx) 
	{
		return ctx.getDefaultTextDrawer().width(this.text);
	}

	@Override
	public int draw(int sx, int sy, RichtextDrawingContext ctx) {
		int w = 0;
		ctx.getSys().color(this.color);
		
		w = ctx.getDefaultTextDrawer().width(this.text);
		ctx.getDefaultTextDrawer().print(this.text,sx,sy, Align.LEFT);
		return w;
	}

	@Override
	public int height(RichtextDrawingContext ctx) {
		return ctx.getDefaultTextDrawer().height();
	}
	
	
}
