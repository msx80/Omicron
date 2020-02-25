package org.github.msx80.omicron.basicutils.gui;

import java.util.ArrayList;
import java.util.List;

import org.github.msx80.omicron.api.Sys;

/**
 * An abstract parent that manages the "list" of children and the add method.  
 *
 */
public abstract class ManagedParentWidget extends ParentWidget {

	public ManagedParentWidget(Sys sys, int x, int y, int w, int h, Padding padding) {
		super(sys, x, y, w, h, padding);
	}

	List<Widget> children = new ArrayList<Widget>();
	
	@Override
	public List<Widget> children() {
		return children;
	}

	public void add(Widget w)
	{
		children.add(w);
		w.setParent(this);
		w.invalidate();
	}


}
