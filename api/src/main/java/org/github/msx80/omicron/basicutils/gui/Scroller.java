package org.github.msx80.omicron.basicutils.gui;

import java.util.ArrayList;
import java.util.List;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.ShapeDrawer;
import org.github.msx80.omicron.basicutils.palette.Tic80;

public class Scroller extends ParentWidget {
	
	static class ScrollBar {
		int scroll;
		int curPos;
		int curLength;
		
		public ScrollBar() {
			scroll =0; curPos = 0; curLength = 0;
		}
		
		public void calc(int clientSize, int childSize)
		{
			// calculate curPos and curLength given the size of the two widgets (and scroll);
			int maxScroll = childSize-clientSize;
			if(scroll> maxScroll)
			{
				scroll = maxScroll;
			}
			if(scroll<0)
			{
				scroll = 0;
			}
			
			if(childSize<=clientSize)
			{
				// scroller contains the whole child, show full scrollbars
				curPos = 0;
				curLength = clientSize;
			}
			else
			{
				// scroller is only showing a portion				
				
				float visibleArea = clientSize / (float)childSize;
				curLength = (int) (visibleArea * clientSize);
				float pos = scroll/(float)(maxScroll);
				curPos = (int) (pos * (clientSize-curLength));
			}
			
			
		}
		
		public void ensureVisible(int clientArea, int x)
		{
			if (x<scroll) {
				scroll -= (scroll-x);
			}
			if(x >= (scroll+clientArea)-1)
			{
				scroll += x-(scroll+clientArea-1);
			}
			
		}
	}
	
	public static int SCROLL_BAR_WIDTH = 8;
	final List<Widget> children = new ArrayList<Widget>();
	final Widget child;
	
	ScrollBar scrollX = new ScrollBar();
	ScrollBar scrollY = new ScrollBar();
	
	public Scroller(Sys sys, int x, int y, int w, int h, Widget child) 
	{
		super(sys, x, y, w, h, Padding.of(0, SCROLL_BAR_WIDTH, 0, SCROLL_BAR_WIDTH));
		this.child = child;
		children.add(child);
		child.setParent(this);
		childInvalidated(child);
	}

	@Override
	public void childInvalidated(Widget widget) {
		// child changed size, recalc scrollbars
		scrollX.calc(w-SCROLL_BAR_WIDTH, child.w);
		scrollY.calc(h-SCROLL_BAR_WIDTH, child.h);
	}

	
	
	@Override
	public void draw() {
		sys.clip(this.getAbsoluteX(), this.getAbsoluteY(), this.w-SCROLL_BAR_WIDTH, this.h-SCROLL_BAR_WIDTH);
		
		sys.offset(-scrollX.scroll, -scrollY.scroll);
		
		child.draw();
		sys.clip(0,0,0,0);
		
		sys.offset(scrollX.scroll, scrollY.scroll);

		drawVerticalScrollbar(w-SCROLL_BAR_WIDTH, 0, SCROLL_BAR_WIDTH,  h-SCROLL_BAR_WIDTH, scrollY.curPos, scrollY.curLength);

		drawHorizontalScrollbar(0, h-SCROLL_BAR_WIDTH, w-SCROLL_BAR_WIDTH, SCROLL_BAR_WIDTH, scrollX.curPos, scrollX.curLength);
		
		
	}

	private void drawHorizontalScrollbar(int sx, int sy, int sw, int sh, int curPos, int curLen) {
		sys.fill(0, sx+curPos, sy, curLen, sh, Tic80.RED);
		ShapeDrawer.outline(sys, sx, sy,sw, sh, 0, Tic80.DARK_RED);
	}

	private void drawVerticalScrollbar(int sx, int sy, int sw, int sh, int curPos, int curLen) {
		sys.fill(0, sx, sy+curPos, sw, curLen, Tic80.RED);
		ShapeDrawer.outline(sys,sx, sy, sw, sh, 0, Tic80.DARK_RED);
	}

	@Override
	protected void click(int px, int py) {
		
	}

	@Override
	public List<Widget> children() {
		return children;
	}

	
	
	@Override
	public void ensureVisible(Widget child, int x, int y, int w, int h) {
		// first ensure the lower-right corner
		scrollX.ensureVisible(this.w-SCROLL_BAR_WIDTH, x+w);
		scrollY.ensureVisible(this.h-SCROLL_BAR_WIDTH, y+h);
		// then ensure the top-left corner
		scrollX.ensureVisible(this.w-SCROLL_BAR_WIDTH, x);
		scrollY.ensureVisible(this.h-SCROLL_BAR_WIDTH, y);
		
		scrollX.calc(this.w-SCROLL_BAR_WIDTH, child.w);
		scrollY.calc(this.h-SCROLL_BAR_WIDTH, child.h);
	}


}
