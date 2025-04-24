package com.github.msx80.omicron.fantasyconsole;

import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.api.adv.AdvancedSys.KeyboardListener;
import com.github.msx80.omicron.basicutils.gui.BaseWidget;
import com.github.msx80.omicron.basicutils.text.TextDrawerFixed;


public class EditorWidget extends BaseWidget implements KeyboardListener {

	public EditorWidget(TextDrawerFixed font, int w, int h) {
		super(w, h);
	}
	/*
	int charHeight = 6;
	int charWidth = 4;
	public EditorWidget(int w, int h) {
		super(w, h);
		
	}

	private Editor tb = new Editor();
	private TextDrawerFixed td;
	private Sys sys;
	
	final EnumSet<Modifiers> mod = EnumSet.noneOf(Modifiers.class);

	
	public EditorWidget(Sys sys, TextDrawerFixed font, int w, int h) {
		super(w, h);
		this.td = font;
		this.sys = sys;

		
		tb.appendLine("package omicron.demo.helloworld;");
		tb.appendLine("");
		tb.appendLine("import com.github.msx80.omicron.api.Game;");
		tb.appendLine("import com.github.msx80.omicron.api.Sys;");
		tb.appendLine("import com.github.msx80.omicron.api.SysConfig;");
		tb.appendLine("import com.github.msx80.omicron.api.SysConfig.VirtualScreenMode;");
		tb.appendLine("");
		tb.appendLine("public class HelloWorld implements Game ");
		tb.appendLine("{");
		tb.appendLine("	");
		tb.appendLine("	private Sys sys;");
		tb.appendLine("	");
		tb.appendLine("    public void init(final Sys sys) ");
		tb.appendLine("    {");
		tb.appendLine("        this.sys = sys;");
		tb.appendLine("    }");
		tb.appendLine("");
		tb.appendLine("	public boolean loop() ");
		tb.appendLine("	{");
		tb.appendLine("    	sys.clear(0xFF); // RGBA black");
		tb.appendLine("    	sys.draw(1, 70,20,0, 0, 100, 50, 0, 0);");
		tb.appendLine("        return true;");
		tb.appendLine("    }");
		tb.appendLine("");
		tb.appendLine("	@Override");
		tb.appendLine("	public SysConfig sysConfig() ");
		tb.appendLine("	{");
		tb.appendLine("		return new SysConfig(240, 136, VirtualScreenMode.SCALED, \"Hello World!\", \"helloworld\");");
		tb.appendLine("	}");
		tb.appendLine("  ");
		tb.appendLine("}");
		tb.appendLine("");
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
				sys.fill(0, 0, y+i*charHeight-2, tb.lines().get(i).length()*charWidth, charHeight, Colors.GREEN);
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
				sys.fill(0, start.pos*charWidth, y+start.line*charHeight-2, length*charWidth, charHeight, Colors.GREEN);
			}
			
			// end
			if(end.line != start.line)
			{
				sys.fill(0, 0, y+end.line*charHeight-2, end.pos*charWidth, charHeight, Colors.GREEN);
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
					td.print("|", tb.cursor.pos*charWidth-2, y-1);
					sys.color(Colors.WHITE);
			}
			y+=charHeight;
		}
	}
	



	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		
		case Input.Keys.DOWN: 
			tb.processSpecialKey(SpecialKeys.DOWN, mod);
			break;
		case Input.Keys.UP: 
			tb.processSpecialKey(SpecialKeys.UP, mod);
			break;
		case Input.Keys.LEFT: 
			tb.processSpecialKey(SpecialKeys.LEFT, mod);
			break;
		case Input.Keys.RIGHT: 
			tb.processSpecialKey(SpecialKeys.RIGHT, mod);
			break;
		case Input.Keys.SHIFT_LEFT:
		case Input.Keys.SHIFT_RIGHT:
			mod.add(Modifiers.SHIFT);
			break;
		default: return false;
		} 
			
		parent.ensureVisible(this, tb.cursor.pos * td.getStepping(), tb.cursor.line * 8, 4, 8);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		
		case Input.Keys.SHIFT_LEFT:
		case Input.Keys.SHIFT_RIGHT:
			mod.remove(Modifiers.SHIFT);
			break;
		default: return false;
		} 
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		if(character == 10)
		{
			tb.processSpecialKey(SpecialKeys.ENTER, mod);
		}
		else
		{
			tb.processKey(character, mod);
		}
		return true;
	}
*/

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}
}
