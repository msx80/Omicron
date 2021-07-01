package com.github.msx80.omicron.basicutils.text.richtext;

import com.github.msx80.omicron.basicutils.text.TextDrawer.Align;

public class Richprint {
	
	public static void print(Richtext[] lines, int x, int y, int lineSpacing, RichtextDrawingContext ctx, Align align)
	{
		for (int i = 0; i < lines.length; i++) {
			
			Richprint.print(lines[i], x, y, ctx, align);
			y+=lines[i].height(ctx)+lineSpacing;
		}
	}
	
	/**
	 * Print a given Richtext. Color is undefined at the end of the print.
	 * @param line
	 * @param sx
	 * @param y
	 * @param sys
	 * @param font
	 * @param align
	 */
	public static void print(Richtext line, int x, int y, RichtextDrawingContext ctx, Align align)
	{
	    	if(align == Align.LEFT)
	    	{
		    	for (RichtextItem cs : line) {
					
					int w = cs.draw(x, y, ctx);
				
					x=x+w;
				}
	    	}
	    	else
	    	{
	    		int total = line.width(ctx);
	    		if (align == Align.CENTER) {
	    			print(line, x-(total/2), y, ctx, Align.LEFT);
				}
	    		else
	    		{
	    			print(line, x-total, y, ctx, Align.LEFT);
	    		}
	    	}
		  
	    }




}
