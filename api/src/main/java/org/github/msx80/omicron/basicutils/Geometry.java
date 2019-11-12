package org.github.msx80.omicron.basicutils;

public final class Geometry {

	
	public static final boolean inRect(int px, int py, int rx, int ry, int rw, int rh)
	{
		return	( px>=rx) && ( px<rx+rw) &&
				( py>=ry) && ( py<ry+rh); 
	}
	
}
