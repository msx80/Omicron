package org.github.msx80.omicron.basicutils;

import java.nio.charset.Charset;

import org.github.msx80.omicron.api.Sys;

/**
 * TextDrawer that automatically calculates bounding boxes to implement a variable-width font.
 * It will check the alpha channel to decide transparency, if it's 0 it's considered empty.
 * You can specify a space in pixel for the space character (and other whitespaces), while
 * the spacing between letters is set to 1.
 *
 */
public class TextDrawerVariable implements TextDrawer {
	
	
	private static final Charset charset = Charset.forName("Cp437");
	
	private final int sheetNum;
	private final int charWidth;
	private final int charHeight;
	private final int whitespaceWidth;
	private final Sys sys;
	private int[] offStart = new int[256];
	private int[] offEnd = new int[256];

	
	public TextDrawerVariable(Sys sys, int sheetNum, int charWidth, int charHeight, int whitespaceWidth) {
		super();
		this.sys = sys;
		this.sheetNum = sheetNum;
		this.charWidth = charWidth;
		this.charHeight = charHeight;
		this.whitespaceWidth = whitespaceWidth;
		calculateSizes();
	}
	private void calculateSizes() {
		for (int i = 0; i < 256; i++) {
			calculateChar(i);
		}
		
	}
	private void calculateChar(int c) {
		int dx = c % 16;
		int dy = c / 16;
		offStart[c] = 0;
		offEnd[c] = charWidth;
		
		for (int i = 0; i < charWidth; i++) {
			if(isColumnBlank((dx*charWidth)+i,(dy*charHeight),charHeight))
			{
				offStart[c]++;
			}
			else
			{
				break;
			}
		}
		
		for (int i = 0; i < charWidth; i++) {
			if(isColumnBlank((dx*charWidth)+charWidth-i-1,(dy*charHeight),charHeight))
			{
				offEnd[c]--;
			}
			else
			{
				break;
			}
		}
		
		if(offStart[c] >= offEnd[c])
		{
			offStart[c] = 0;
			offEnd[c] = whitespaceWidth;
		}
		
	}
	private boolean isColumnBlank(int dx, int dy, int charHeight2) {
		for (int y = dy; y < dy+charHeight2; y++) {
			int n = sys.getPix(sheetNum, dx, y);
			if(Colors.alpha(n) > 0)
			{
				return false;
			}
		}
		return true;
	}
	public void print(String text, int x, int y, Align align)
	{
		int dx = 0;
		if(align == Align.CENTER)
		{
			dx = -width(text)/2;
		}
		else if(align == Align.RIGHT)
		{
			dx = -width(text);
		}
		
		print(text, x+dx, y);
	}
	
	public void print(String text, int x, int y)
	{
	
		byte[] b = text.getBytes(charset);
		int pos = 0;
		for (int i = 0; i < b.length; i++) {
			
			int c = b[i];
			int dx = c % 16;
			int dy = c / 16;
			
			int os = offStart[c];
			int oe = offEnd[c];
			int rw = oe-os;
			
			sys.draw(sheetNum, x+pos, y, dx*charWidth + os, dy*charHeight, rw , charHeight, 0, 0);
			
			pos = pos + rw +1;
		
		}
	}
	public int width(String text)
	{
		byte[] b = text.getBytes(charset);

		int w =  (b.length) * 1;
		
		for (int i = 0; i < b.length; i++) {
			
			int c = b[i];
			
			int os = offStart[c];
			int oe = offEnd[c];
			int rw = oe-os;
			
			w += rw;
	
		}
		return w;
	}
}
