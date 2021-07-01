package com.github.msx80.omicron.basicutils.text.richtext;

import com.github.msx80.omicron.basicutils.text.TextDrawer.Align;

public class Richprinter {

	private RichtextDrawingContext ctx;
	private Align align;
	private int lineSpacing;
		
	public Richprinter(RichtextDrawingContext ctx, Align align, int lineSpacing) 
	{
		super();
		this.ctx = ctx;
		this.lineSpacing = lineSpacing;				
		this.align = align;
	}

	public void print(Richtext[] lines, int x, int y)
	{
		Richprint.print(lines, x, y, lineSpacing, ctx, align);
	}
	
	public void print(Richtext line, int x, int y)
	{
		Richprint.print(line, x, y, ctx, align);
	}
}
