package org.github.msx80.omicron.basicutils;

import org.github.msx80.omicron.api.Sys;

public class LineDrawer {
	 // function for line generation 
    public static void bresenham(Sys sys, int sheetNum, int color, int x1, int y1, int width, int height) 
    { 
        int x2 = x1 + width;
        int y2 = y1 + height;

        int deltax = Math.abs(x2 - x1);
        int deltay = Math.abs(y2 - y1);
        int error = 0;
        int y = y1;
        for( int x=x1; x<x2; x++) {
            sys.setPix(sheetNum, x, y, color);
            error = error + deltay;
            if( 2*error >= deltax ) {
                y = y + 1;
                error=error - deltax;
            }
        }
    }
}
