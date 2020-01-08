package org.github.msx80.omicron.basicutils;

import org.github.msx80.omicron.api.Sys;

public class Zoomer {
	
	/**
	 * Copy a portion of image from srcSurf to destSurf scaling it by scale factor. Each src pixel will be a scale*scale block on dest.
	 */
	public static void zoom(Sys sys, int srcSurf, int destSurf, int srcx, int srcy, int destx, int desty, int w, int h, int scale)
	{
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int c = sys.getPix(srcSurf, x + srcx, y + srcy);
				sys.fill(destSurf, x*scale + destx, y*scale + desty, scale, scale, c);
			}
		}
	}
}
