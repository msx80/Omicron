package org.github.msx80.omicron.basicutils.gui.drawers;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.TextDrawer;
import org.github.msx80.omicron.basicutils.gui.Widget;

public abstract class WidgetDrawerWithText<W extends Widget> implements WidgetDrawer<W> {

	protected final Sys sys;
	protected final TextDrawer td;

	public WidgetDrawerWithText(Sys sys, TextDrawer td) {
		this.td = td;
		this.sys = sys;
	}

	
	@Override
	public int textWidth(String t) {
		return td.width(t);
		
	}

	@Override
	public int textHeight() {
		return td.height();
		
	}

}
