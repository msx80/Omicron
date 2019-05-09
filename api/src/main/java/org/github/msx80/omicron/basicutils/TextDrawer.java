package org.github.msx80.omicron.basicutils;

import java.nio.charset.Charset;

import org.github.msx80.omicron.api.Sys;

public class TextDrawer {
	
	private static final Charset charset = Charset.forName("Cp437");
	
	private final int sheetNum;
	private final int charWidth;
	private final int charHeight;
	private final int stepping;
	private final Sys sys;
	
	public TextDrawer(Sys sys, int sheetNum, int charWidth, int charHeight, int stepping) {
		super();
		this.sys = sys;
		this.sheetNum = sheetNum;
		this.charWidth = charWidth;
		this.charHeight = charHeight;
		this.stepping = stepping;
	}

	public void print(String text, int x, int y)
	{
	
		byte[] b = text.getBytes(charset);
		for (int i = 0; i < b.length; i++) {
			int c = b[i];
			int dx = c % 16;
			int dy = c / 16;
			
			sys.draw(sheetNum, x+i*stepping, y, dx*charWidth, dy*charHeight, charWidth, charHeight);
		
		}
	}
	
}
