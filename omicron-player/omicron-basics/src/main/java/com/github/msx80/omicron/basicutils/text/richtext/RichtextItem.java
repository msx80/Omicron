package com.github.msx80.omicron.basicutils.text.richtext;

public interface RichtextItem 
{
	int width(RichtextDrawingContext ctx);
	int height(RichtextDrawingContext ctx);
	int draw(int sx, int sy, RichtextDrawingContext ctx);
}
