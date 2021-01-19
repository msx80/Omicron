package org.github.msx80.omicron.basicutils.gui;

import java.util.ArrayList;
import java.util.List;

import org.github.msx80.omicron.api.Sys;

/**
 * An abstract parent that manages the "list" of children and the add method.  
 *
 */
public abstract class ManagedParentWidget extends ParentWidget {

	public ManagedParentWidget(Sys sys, int w, int h) {
		super(sys, w, h);
	}

	List<Widget> children = new ArrayList<Widget>();
	
	@Override
	public List<Widget> children() {
		return children;
	}

	/**
	 * Add the specified widget at the foremost, at the specified position
	 * @param <T>
	 * @param w
	 * @param x
	 * @param y
	 * @return
	 */
	public <T extends Widget> T add(T w, int x, int y)
	{
		children.add(w);
		w.setParent(this);
		w.x = x;
		w.y = y;
		w.invalidate();
		return w;
	}

	@Override
	public Widget remove(Widget w)
	{
		children.remove(w);
		w.setParent(null);
		return w;
	}
	
	public void zMove(Widget w, boolean up)
	{
		children.remove(w);
		if(up)
		{
			children.add(0, w);
		}
		else
		{
			children.add(w);
		}
	}
}
