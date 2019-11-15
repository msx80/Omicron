package org.github.msx80.experimental;

import java.util.ArrayList;
import java.util.List;

import org.github.msx80.omicron.api.Mouse;

public abstract class Widget<T> {

	List<T> lines = new ArrayList<>();
	
	boolean cleanEnter = false;
	boolean down = false;
	int downYPlusScroll = 0;
	int downY = 0;
	boolean hasMoved = false;
	
	int scroll = 0;
	private int x;
	private int y;	
	protected int w;
	protected int h;
	private int itemsHeight;
	
	public Widget(int x, int y, int w, int h, int itemsHeight) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.itemsHeight = itemsHeight;
	}
	
	public abstract void drawBackground(int x,int y);
	public abstract void drawForeground(int x,int y);
	protected abstract boolean selected(int idx, T line);
	protected abstract boolean clickedOutside(int x, int y);
	
	protected void invalidateAndReload() {
		this.lines = null; // clean up so it will reload
	}
		

	
	public void draw()
	{
		drawBackground(x, y);

		int sy = y-scroll;
		for (int i = 0; i < lines.size(); i++) {
		
			if ( (sy-y>-itemsHeight-1) && (sy-y<h)) 
			{
				drawItem(x, sy, i, lines.get(i));
				
			}
			sy+=itemsHeight;
		}
		
		drawForeground(x, y);
		
	}
	
	
	public abstract void drawItem(int ax, int ay, int idx, T line);

	public boolean update(Mouse m) {
		// wait for the button to raise before start working, otherwise
		// the click that open the widget will be already captured as click on the widget
		if (!cleanEnter) {
			if (m.btn[0]) {
				return true;
			}
			else
			{
				cleanEnter = true;
			}
		}
		
		boolean res = true;
		
		//if(m.btn[0] && !oldBtn) scroll++;
		//oldBtn = m.btn[0]; 
		if (down) {
			if(!m.btn[0])
			{
				down = false;
				if(!hasMoved)
				{
					hasMoved = false;
					res =  doclicked(m.x, m.y);
				}
			}
			else
			{
				// dragging
				scroll = downYPlusScroll-m.y;
				if(m.y!=downY) hasMoved = true;
			}
		}
		else
		{
			if(m.btn[0])
			{
				down = true;
				downY = m.y;
				downYPlusScroll = m.y + scroll; // add old scroll value to make it continuous
				hasMoved = false;
			}
			else
			{
				// null
			}
		}
		
		
			int maxScroll = (lines.size()*itemsHeight)-h-1; // -1 is the separator (guessing it, otherwise i would need an itemHeight and a separatorHeight)
			if(scroll > maxScroll)
			{
				scroll = maxScroll;
			}
			if(scroll<0) scroll = 0;
		
		return res;
		//return m.btn[0]==false;
	}
	private boolean doclicked(int x2, int y2) {
		if ((x2>=x) && (x2<x+w) && (y2>=y) && (y2<y+h))
		{
			y2 -= y;
			y2 += scroll;
			y2 += 1;
			int idx = y2/itemsHeight;
			if(idx >=0 && idx < lines.size())
			{
				T line = lines.get(idx);
				return selected(idx, line);
				// System.out.println(Arrays.toString(line.tokens));
			}
		}
		else
		{
			return clickedOutside(x2, y2);
		}
		return true;
	}

	public List<T> getLines() {
		return lines;
	}

	public void setLines(List<T> lines) {
		this.lines = lines;
	}

	
	
}
