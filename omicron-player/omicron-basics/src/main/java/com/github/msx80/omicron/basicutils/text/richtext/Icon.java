package com.github.msx80.omicron.basicutils.text.richtext;

public class Icon implements RichtextItem {

	int sheetNum;
	int srcx;
	int srcy;
	int w;
	int h;
	
	public Icon(int sheetNum, int srcx, int srcy, int w, int h) {
		super();
		this.sheetNum = sheetNum;
		this.srcx = srcx;
		this.srcy = srcy;
		this.w = w;
		this.h = h;
	}

	@Override
	public int width(RichtextDrawingContext ctx) {
		return w;
	}

	@Override
	public int draw(int sx, int sy, RichtextDrawingContext ctx) {
		ctx.getSys().draw(sheetNum, sx, sy, srcx, srcy, w, h, 0, 0);
		return w;
	}

	@Override
	public int height(RichtextDrawingContext ctx) {
		return h;
	}

}
