package com.github.msx80.omicron.fantasyconsole;

import com.github.msx80.omicron.basicutils.gui.BaseWidget;


public class EditorWidget extends BaseWidget /*implements Controllable*/ {

	public EditorWidget(int w, int h) {
		super(w, h);
		
	}

	@Override
	public void draw() {
		
	}
	
	
/*
	private Editor tb = new Editor();
	private TextDrawerFixed td;
	private Sys sys;

	
	public EditorWidget(Sys sys, TextDrawerFixed font, int w, int h) {
		super(w, h);
		this.td = font;
		this.sys = sys;

		
		tb.appendLine("Hello");
		tb.appendLine("World");
		tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("Very long line - Very long line - Very long line - Very long line");
		tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("0");
		tb.appendLine("1");
		tb.appendLine("2");
		tb.appendLine("3");
		tb.appendLine("4");tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("Very long line - Very long line - Very long line - Very long line");
		tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("0");
		tb.appendLine("1");
		tb.appendLine("2");
		tb.appendLine("3");
		tb.appendLine("4");tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("Very long line - Very long line - Very long line - Very long line");
		tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("Lorem ipsum");
		tb.appendLine("Bla bla bla bla");
		tb.appendLine("0");
		tb.appendLine("1");
		tb.appendLine("2");
		tb.appendLine("3");
		tb.appendLine("4");
		tb.appendLine("5");
		tb.appendLine("6");
		tb.appendLine("7");
		tb.appendLine("8");
		tb.appendLine("9");
		tb.appendLine("End of file");
		
		
	}

	@Override
	public void control(Controller c) {
		if(c.up())
			tb.processSpecialKey(SpecialKeys.UP, null);
		if(c.down())
			tb.processSpecialKey(SpecialKeys.DOWN, null);
		if(c.left())
			tb.processSpecialKey(SpecialKeys.LEFT, null);
		if(c.right())
			tb.processSpecialKey(SpecialKeys.RIGHT, null);
	
		parent.ensureVisible(this, tb.cursor.pos * td.getStepping(), tb.cursor.line * 8, 4, 8);
		
		
	}

	@Override
	public void draw() {

		
		// s.drawImage(img, 10,10,0,0,100,100);
		int y=0;
		// sys.color(Colors.GREEN);
		if(tb.isSelecting())
		{
			Cursor start = tb.getSelectionStart();
			Cursor end = tb.getSelectionEnd();
			
			// fill middle lines
			for (int i = start.line+1; i < end.line; i++) 
			{
				sys.fill(0, 0, y+i*8-2, tb.lines().get(i).length()*6, 8, Colors.GREEN);
			}
			
			// beginning
			{
				int length;
				if(end.line == start.line)
				{
					length = end.pos-start.pos;
				}
				else
				{
					length = tb.lines().get(start.line).length() - start.pos;
				}
				sys.fill(0, start.pos*6, y+start.line*8-2, length*6, 8, Colors.GREEN);
			}
			
			// end
			if(end.line != start.line)
			{
				sys.fill(0, 0, y+end.line*8-2, end.pos*6, 8, Colors.GREEN);
			}
			
		}
		
		for (int i = 0; i < tb.lines().size(); i++) 
		{
			StringBuilder line = tb.lines().get(i);
			td.print(line.toString(), 0, y);
			if(i == tb.cursor.line)
			{
				sys.color(Colors.GREEN);
			//	if( ((System.currentTimeMillis()>>8) % 2) == 0)
					td.print("|", tb.cursor.pos*6-2, y-1);
					sys.color(Colors.WHITE);
			}
			y+=8;
		}
	}
*/
}
