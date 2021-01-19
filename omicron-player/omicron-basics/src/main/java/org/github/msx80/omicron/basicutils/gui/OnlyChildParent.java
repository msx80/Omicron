package org.github.msx80.omicron.basicutils.gui;

import java.util.Arrays;
import java.util.List;

import org.github.msx80.omicron.api.Sys;

public abstract class OnlyChildParent extends ParentWidget {

	final Widget child;
	final private List<Widget> children;

	public OnlyChildParent(Sys sys, Widget child, int w, int h) 
	{
		super(sys, w, h);
		this.child = child;
		this.children = Arrays.asList(child);
		this.child.parent = this;
	}
	
	@Override
	public List<Widget> children() {
		
		return children;
	}
	
	public Widget getChild()
	{
		return child;
	}

}
