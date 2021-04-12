package com.github.msx80.omicron.basicutils.gui;

import java.util.Arrays;
import java.util.List;

import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.api.adv.AdvancedSys.KeyboardListener;

import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.ShapeDrawer;
import com.github.msx80.omicron.basicutils.TextDrawerFixed;

public class TextArea extends Widget implements KeyboardListener {

	class TextAreaCache {
		List<String> items;
		int cursorColumn, cursorRow;
		char cursorChar;
		int cursorX;
		int cursorY;
		
		public TextAreaCache(List<String> items, int cursorColumn, int cursorRow, char cursorChar, int cursorX, int cursorY) {
			super();
			this.items = items;
			this.cursorColumn = cursorColumn;
			this.cursorRow = cursorRow;
			this.cursorChar = cursorChar;
			this.cursorX = cursorX;
			this.cursorY = cursorY;
		}
	}

	private StringBuffer sb = new StringBuffer();
	private int cursorPosition;
	
	private TextAreaCache cache;
	private Sys sys;
	private TextDrawerFixed td;
	
	public TextArea(Sys sys, TextDrawerFixed td) {
		super(Integer.MAX_VALUE, Integer.MIN_VALUE);
		this.sys = sys;
		this.td = td;
		updateState();
	}

	
	private void updateState()
	{
		
		int cx = 0;
		int cy = 0;
		char cc = ' ';
		String[] arr = (sb.toString()).split("\n");
		for (int i = 0; i < arr.length; i++) {
			arr[i] = arr[i]+"\n";
		}
		List<String> items = Arrays.asList(arr); // extra space to show the cursor

		int oldw = this.w;
		int oldh = this.h;
		
		this.h = items.size()*td.height();
		this.w = 0;
		
		int findCursor = cursorPosition;
		
		for (int i = 0; i < items.size(); i++) {
			String l = items.get(i);
			int lineW = td.width(l);
			this.w = Math.max(lineW, this.w);
					
			if(findCursor>=0)
			{
				if(findCursor < l.length())
				{
					// cursor is in this line
					cc = (l).charAt(findCursor);
					cx = findCursor;
					cy = i;
					findCursor = -1;
				}
				else
				{
					findCursor-=l.length(); 
				}
			}
		}
		System.out.println(findCursor);
		cache = new TextAreaCache(items, cx, cy, cc, cx*td.getStepping(), cy*td.getCharHeight());
				
		if (oldw != w || oldh != h)
		{
			invalidate();
		}
		if(parent!=null)
		parent.ensureVisible(this, cache.cursorX, cache.cursorY, 1, td.getCharHeight());
	}
	
	
	@Override
	public void draw() {

		int dy = 0;

		for (String s : cache.items) {
			td.print(s, 0, dy);
			dy+=td.height();
		}

		ShapeDrawer.line(sys, cache.cursorX, cache.cursorY, cache.cursorX, cache.cursorY+td.getCharHeight(), 0, Colors.RED);
		
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == 21)
		{
			if(cursorPosition>0) cursorPosition--;
			
		}
		else if(keycode == 22)
		{
			if(cursorPosition<sb.length()) cursorPosition++;
			
		}
		
		else if(keycode == 132)
		{
			setCursorPosClosestTo(Integer.MAX_VALUE, cache.cursorRow);
			
		}
		else if(keycode == 3)
		{
			setCursorPosClosestTo(0, cache.cursorRow);
			
		}
		else if(keycode == 19)
		{
			setCursorPosClosestTo(cache.cursorColumn, cache.cursorRow-1);
			
		}
		else if(keycode ==20)
		{
			setCursorPosClosestTo(cache.cursorColumn, cache.cursorRow+1);
			
		}
		else if (keycode == 67)
		{
			// backspace
			if(sb.length()>0)
			{
				sb.delete(cursorPosition-1, cursorPosition);
				cursorPosition--;
			}
		}
		else if ( keycode == 112)
		{
			// backspace
			if(cursorPosition< sb.length())
			{
				sb.delete(cursorPosition, cursorPosition+1);
			}
		}		
		else
		{
			System.out.println("Keydown "+keycode);
			return false;
		}
		updateState();
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char c) {
		
		if(c == (char)0)
		{
			return true;
			
		}
		else if(c == (char)13)
		{
			sb.insert(cursorPosition, '\n');
			cursorPosition++;
		}

		else if (c >= 32 && c < 127)
		{
			
			sb.insert(cursorPosition, c);
			cursorPosition++;
		}
		updateState();
		return true;
	}
	
	private void setCursorPosClosestTo(int x, int y) {
		int oldcursorPosition = cursorPosition;
		if(y<0) y = 0;
		if(x<0) x = 0;
		if(y>=cache.items.size()) y = cache.items.size()-1;
		
		String l = cache.items.get(y);
		if(x>l.length()-1) x = l.length()-1;
		
		int tot = x;
		for (int i = 0; i < y; i++) {
			tot += cache.items.get(i).length();
		}
		
		this.cursorPosition = tot;
		if(oldcursorPosition != cursorPosition)
		{
			updateState();
		}
	}


	public void setText(String string) {
		this.sb.setLength(0);
		this.sb.append(string);
		updateState();
		
	}


	public String getText() {
		
		return sb.toString();
	}
	

}
