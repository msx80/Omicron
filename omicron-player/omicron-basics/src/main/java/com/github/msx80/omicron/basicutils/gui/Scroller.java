package com.github.msx80.omicron.basicutils.gui;

import com.github.msx80.omicron.api.Sys;

import com.github.msx80.omicron.basicutils.gui.drawers.ScrollbarDrawer;

/**
 * Parent of a single child that implements scrolling
 * Can have scrollbars visible or invisible
 * Works by moving child X and Y coordinates so that all
 * calculation on relative child position still works
 */
public class Scroller extends OnlyChildParent implements Scrollable {
	
	static class ScrollBar {
		int scroll;
		int curPos;
		int curLength;
		
		public ScrollBar() {
			scroll =0; curPos = 0; curLength = 0;
		}
		
		public void calc(int clientSize, int childSize, int border)
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
			int scrollBarCursorArea = clientSize-2*border;
			if(childSize<=clientSize)
			{
				// scroller contains the whole child, show full scrollbars
				curPos = border;
				curLength = scrollBarCursorArea;
			}
			else
			{
				// scroller is only showing a portion				
				
				double visibleArea = clientSize / (double)childSize;
				curLength = (int) (visibleArea * scrollBarCursorArea);
				double pos = scroll/(double)(maxScroll);
				curPos = border + (int) (pos * (scrollBarCursorArea-curLength));
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

		public void scroll(int i) {
			scroll += i;
			
		}
	}
		
	ScrollBar scrollX = new ScrollBar();
	ScrollBar scrollY = new ScrollBar();
	private ScrollbarDrawer drawerV;
	private ScrollbarDrawer drawerH;
	
	public Scroller(Sys sys, int w, int h, Widget child, ScrollbarDrawer drawerV, ScrollbarDrawer drawerH) 
	{
		super(sys, child, w, h);
		this.drawerV = drawerV;
		this.drawerH = drawerH;
		child.x = 0;
		child.y = 0;
		childInvalidated(child);
	}

	@Override
	public void childInvalidated(Widget widget) {
		recalcScrollX();
		recalcScrollY();
	}

	
	
	@Override
	public void draw() {
		int areaWidth = getClientAreaWidth();
		int areaHeight =  getClientAreaHeight();
		sys.clip(this.getAbsoluteX(), this.getAbsoluteY(), areaWidth, areaHeight);
		

		/*sys.offset(-scrollX.scroll, -scrollY.scroll);
		
		child.draw();
		
		sys.offset(scrollX.scroll, scrollY.scroll);
*/
		sys.offset(child.x, child.y);
		if(child instanceof PartialDrawable)
		{
			//throw new RuntimeException("Drawing of PartialDrawable not implemented yet");
			((PartialDrawable) child).draw(-child.x, -child.y, areaWidth, areaHeight);
		}
		else
		{
			child.draw();
		}
		sys.offset(-child.x, -child.y);

		
		
		sys.clip(0,0,0,0);
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

	protected int getClientAreaWidth()
	{
		return this.w-drawerV.getThickness();
	}
	
	@Override
	public void ensureVisible(Widget child, int x, int y, int w, int h) {
		// first ensure the lower-right corner
		if(w != 0) scrollX.ensureVisible(getClientAreaWidth(), x+w-1);
		if(h != 0) scrollY.ensureVisible(getClientAreaHeight(), y+h-1);
		
		// then ensure the top-left corner, which is more important
		scrollX.ensureVisible(getClientAreaWidth(), x);
		scrollY.ensureVisible(getClientAreaHeight(), y);
		
		// recalculate cursors
		recalcScrollX();
		recalcScrollY();
	}

	private int getClientAreaHeight() {
		return this.h-drawerH.getThickness();
	}

	public void scrollVertical(int i) {
		scrollY.scroll(i);
		recalcScrollY();
	}

	public void scrollHorizontal(int i) {
		scrollX.scroll(i);
		recalcScrollX();
	}

	private void recalcScrollY() {
		scrollY.calc(getClientAreaHeight(), child.h, drawerV.getBorder());
		child.y = - scrollY.scroll;
	}
	private void recalcScrollX() {
		scrollX.calc(getClientAreaWidth(), child.w, drawerH.getBorder());
		child.x = - scrollX.scroll;
	}

	@Override
	public void startScroll(int x, int y) {
		
	}

	@Override
	public void doScroll(int dx, int dy) {
		//System.out.println("Dragged "+dx+" "+dy);
		this.scrollHorizontal(-dx);
		this.scrollVertical(-dy);
	}

	@Override
	public void endScroll() {
		
	}
//	
//	public Widget find(int px, int py, boolean deep, Predicate<? super Widget> filter)
//	{
//		if( (!deep) && filter.test(this) ) return this;
//		
//		if(child.isInside(px, py))
//		{
//			
//			return child.find(px-child.x-scrollX.scroll, py-child.y-scrollY.scroll, deep, filter);
//			
//		}
//
//		
//		if( deep && filter.test(this) ) return this;
//		
//		return null;
//	}

	@Override
	public Widget remove(Widget w) {
		throw new RuntimeException("Can't remove child from scroller");
	}

}
