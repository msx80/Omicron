package com.github.msx80.omicron.basicutils;

import java.nio.charset.Charset;

import com.github.msx80.omicron.api.Sys;

/**
 * TextDrawer that implements monospaced font. The stepping value controls how much to advance for each character. 
 *
 */
public class TextDrawerFixed implements TextDrawer {
		
	private static final Charset charset = Charset.forName("Cp437");
	
	private final int sheetNum;
	private final int charWidth;
	private final int charHeight;
	private final int stepping;
	private final Sys sys;
	
	public TextDrawerFixed(Sys sys, int sheetNum, int charWidth, int charHeight, int stepping) {
		super();
		this.sys = sys;
		this.sheetNum = sheetNum;
		this.charWidth = charWidth;
		this.charHeight = charHeight;
		this.stepping = stepping;
	}
	@Override
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
	
	@Override
	public void print(String text, int x, int y)
	{
	
		byte[] b = text.getBytes(charset);
		for (int i = 0; i < b.length; i++) {
			int c = b[i];
			int dx = c % 16;
			int dy = c / 16;
			
			sys.draw(sheetNum, x+i*stepping, y, dx*charWidth, dy*charHeight, charWidth, charHeight, 0, 0);
		
		}
	}
	@Override
	public int width(String text)
	{
		return text.length() * stepping;
	}
	@Override
	public int height() {
		return charHeight;
	}
	public int getCharHeight() {
		return charHeight;
	}
	public int getStepping() {
		return stepping;
	}
	
	
}
