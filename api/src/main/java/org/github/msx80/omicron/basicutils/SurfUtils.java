package org.github.msx80.omicron.basicutils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.github.msx80.omicron.api.Sys;

public class SurfUtils {

	
	
	public static final void bufferToSurface(byte[] buffer, Sys sys, int surface, int sx, int sy, int w, int h)
	{
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
			try {
				
				loadSurface(sys, surface, sx, sy, w, h, bais);
			
			} finally {
				bais.close();
			} 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static final byte[] surfaceToBuffer(Sys sys, int surface, int sx, int sy, int w, int h)
	{
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(w*h*4);
			try
			{
				saveSurface(sys, surface, sx, sy, w, h, baos);
				return baos.toByteArray();
			}
			finally
			{
				baos.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
    private static final int readInt(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

	
	 private static final void writeInt(OutputStream out, int v) throws IOException {
	        out.write((v >>> 24) & 0xFF);
	        out.write((v >>> 16) & 0xFF);
	        out.write((v >>>  8) & 0xFF);
	        out.write((v >>>  0) & 0xFF);
	    }

		public static void loadSurface(Sys sys, int surface, int sx, int sy, int w, int h, ByteArrayInputStream inp) throws IOException {
			for (int y = sy; y < h; y++) {
				for (int x = sx; x < w; x++) {
					int c = readInt(inp);
					sys.fill(surface, x, y, 1, 1, c);
				}
			}
		}


	public static void saveSurface(Sys sys, int surface, int sx, int sy, int w, int h, OutputStream out) throws IOException {
		for (int y = sy; y < h; y++) {
			for (int x = sx; x < w; x++) {
				int c = sys.getPix(surface, x, y);
				writeInt(out, c);
			}
			
		}
	}	
	
	
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
