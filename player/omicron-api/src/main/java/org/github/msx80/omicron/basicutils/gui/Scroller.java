package org.github.msx80.omicron.basicutils.gui;

import java.util.ArrayList;
import java.util.List;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.ShapeDrawer;
import org.github.msx80.omicron.basicutils.gui.drawers.IScrollbarDrawer;
import org.github.msx80.omicron.basicutils.palette.Tic80;

public class Scroller extends ParentWidget {
	
	static class ScrollBar {
		int scroll;
		int curPos;
		int curLength;
		
		public ScrollBar() {
			scroll =0; curPos = 0; curLength = 0;
		}
		
		public void calc(int clientSize, int childSize, int border)
		{
			clientSize -= 2*border;
			
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
				
				double visibleArea = clientSize / (double)childSize;
				curLength = (int) (visibleArea * clientSize);
				double pos = scroll/(double)(maxScroll);
				curPos = (int) (pos * (clientSize-curLength));
			}
			
			curPos+=border;
			
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

		public void scroll(int i) {
			scroll += i;
			
		}
	}
	
	//public static int SCROLL_BAR_WIDTH = 8;
	final List<Widget> children = new ArrayList<Widget>(1);
	final Widget child;
	
	ScrollBar scrollX = new ScrollBar();
	ScrollBar scrollY = new ScrollBar();
	private IScrollbarDrawer drawerV;
	private IScrollbarDrawer drawerH;
	
	public Scroller(Sys sys, int x, int y, int w, int h, Widget child, IScrollbarDrawer drawerV, IScrollbarDrawer drawerH) 
	{
		super(sys, x, y, w, h, Padding.of(0, drawerH.getThickness(), 0, drawerV.getThickness()));
		this.drawerV = drawerV;
		this.drawerH = drawerH;
		this.child = child;
		children.add(child);
		child.setParent(this);
		childInvalidated(child);
	}

	@Override
	public void childInvalidated(Widget widget) {
		// child changed size, recalc scrollbars
		scrollX.calc(w-drawerV.getThickness(), child.w, drawerH.getBorder());
		scrollY.calc(h-drawerH.getThickness(), child.h, drawerV.getBorder());
	}

	
	
	@Override
	public void draw() {
		sys.clip(this.getAbsoluteX(), this.getAbsoluteY(), this.w-drawerV.getThickness(), this.h-drawerH.getThickness());
		
		sys.offset(-scrollX.scroll, -scrollY.scroll);
		
		child.draw();
		sys.clip(0,0,0,0);
		
		sys.offset(scrollX.scroll, scrollY.scroll);

		drawerV.drawVerticalScrollbar(sys, w-drawerV.getThickness(), 0,  h-drawerH.getThickness(), scrollY.curPos, scrollY.curLength);

		drawerH.drawHorizontalScrollbar(sys, 0, h-drawerH.getThickness(), w-drawerV.getThickness(), scrollX.curPos, scrollX.curLength);
		
		
	}
/*
	private void drawHorizontalScrollbar(int sx, int sy, int sw, int sh, int curPos, int curLen) {
		sys.fill(0, sx+curPos, sy, curLen, sh, Tic80.RED);
		ShapeDrawer.outline(sys, sx, sy,sw, sh, 0, Tic80.DARK_RED);
	}

	private void drawVerticalScrollbar(int sx, int sy, int sw, int sh, int curPos, int curLen) {
		sys.fill(0, sx, sy+curPos, sw, curLen, Tic80.RED);
		ShapeDrawer.outline(sys,sx, sy, sw, sh, 0, Tic80.DARK_RED);
	}
*/
	
	@Override
	public List<Widget> children() {
		return children;
	}

	
	
	@Override
	public void ensureVisible(Widget child, int x, int y, int w, int h) {
		// first ensure the lower-right corner
		scrollX.ensureVisible(this.w-drawerV.getThickness(), x+w);
		scrollY.ensureVisible(this.h-drawerH.getThickness(), y+h);
		// then ensure the top-left corner, which is more important
		scrollX.ensureVisible(this.w-drawerV.getThickness(), x);
		scrollY.ensureVisible(this.h-drawerH.getThickness(), y);
		
		scrollX.calc(this.w-drawerV.getThickness(), child.w, drawerH.getBorder());
		scrollY.calc(this.h-drawerH.getThickness(), child.h, drawerV.getBorder());
	}

	public void scrollVert(int i) {
		scrollY.scroll(i);
		scrollY.calc(this.h-drawerH.getThickness(), child.h, drawerV.getBorder());
	}
	public void scrollHoriz(int i) {
		scrollX.scroll(i);
		scrollX.calc(this.w-drawerV.getThickness(), child.w, drawerH.getBorder());
	}


}
