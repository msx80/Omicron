package com.github.msx80.omicron.basicutils.gui;

public abstract class AbstractList<T> extends Widget implements PartialDrawable {

	
	
	private int itemsHeight;
	
	public AbstractList(int w) {
		super(w, 10);
	}

	protected void init()
	{
		this.itemsHeight = itemsHeight();
		this.setSize(w, itemsCount() * itemsHeight);
	}
	
	@Override
	public void draw() {
		drawItems(0, Integer.MAX_VALUE);
	}
	
	
	@Override
	public void draw(int fromx, int fromy, int width, int height) {
		int startIdx = fromy / itemsHeight;
		int endIdx = startIdx + height/itemsHeight + 2; // 1 becouse end is inclusive, 1 to draw last partial item
		drawItems(startIdx, endIdx);
	}

	private void drawItems(int startIdx, int endIdx) {
		int siz = itemsCount();
		if(siz>endIdx)
		{
			siz = endIdx;
		}
		int s = Math.max(0, startIdx);
		boolean odd = startIdx % 2 == 0;
		for (int i = s; i < siz; i++) {
			
			drawItem(i, 0, i*itemsHeight, odd);
			odd = !odd;
		}
	}

	public abstract void drawItem(int idx, int x, int y, boolean odd);
	
	public abstract int itemsHeight();
	public abstract int itemsCount();
	
	
}
