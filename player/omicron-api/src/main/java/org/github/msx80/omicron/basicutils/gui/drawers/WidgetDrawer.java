package org.github.msx80.omicron.basicutils.gui.drawers;

import org.github.msx80.omicron.basicutils.gui.Widget;

public interface WidgetDrawer<W extends Widget> {

	public void draw(W widget);
	public int textWidth(String t);
	public int textHeight();
	
}
