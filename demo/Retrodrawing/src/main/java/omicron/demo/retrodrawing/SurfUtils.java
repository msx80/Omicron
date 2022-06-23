package omicron.demo.retrodrawing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.github.msx80.omicron.api.Sys;

public class SurfUtils {

	
	// note: for some reason GZip streams are dead slow on android
	public static final void bufferToSurfaceGZip(byte[] buffer, Sys sys, int surface, int sx, int sy, int w, int h)
	{
		try {
			InputStream bais = new GZIPInputStream(new ByteArrayInputStream(buffer));
			try {
				for (int y = sy; y < h; y++) {
					for (int x = sx; x < w; x++) {
						int c = bais.read();
						if(c==-1) throw new RuntimeException("End of stream reached prematurely");
						sys.fill(surface, x, y, 1, 1, Palette.P[c]);
					}
				}
			} finally {
				bais.close();
			} 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static final byte[] surfaceToBufferGZip(Sys sys, int surface, int sx, int sy, int w, int h)
	{
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(w*h*4);
			OutputStream o = new GZIPOutputStream(baos);
			try
			{
			
					for (int y = sy; y < h; y++) {
						for (int x = sx; x < w; x++) {
							int c = sys.getPix(surface, x, y);
							int b = idx(c);
							o.write(b);
						}
					}
				o.close();
				baos.close();
				return baos.toByteArray();
			}
			finally
			{
				o.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static final void bufferToSurface(byte[] buffer, Sys sys, int surface, int sx, int sy, int w, int h)
	{
		try {
			InputStream bais = new ByteArrayInputStream(buffer);
			try {
				for (int y = sy; y < h; y++) {
					for (int x = sx; x < w; x++) {
						int c = bais.read();
						if(c==-1) throw new RuntimeException("End of stream reached prematurely");
						sys.fill(surface, x, y, 1, 1, Palette.P[c]);
					}
				}
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
			
				for (int y = sy; y < h; y++) {
					for (int x = sx; x < w; x++) {
						int c = sys.getPix(surface, x, y);
						int b = idx(c);
						baos.write(b);
					}
				}
				
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

	private static int idx(int c) {
		for (int i = 0; i < Palette.P.length; i++) {
			if(c==Palette.P[i]) return i;
		}
		throw new RuntimeException("Color not found in palette! "+Integer.toHexString(c));
	}	
	
}
