package com.github.msx80.omicron.plugins.builtin;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.github.msx80.omicron.GdxOmicron;
import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;
import com.github.msx80.omicron.api.Omicron;
import com.github.msx80.omicron.api.Sys;

public class SurfacePlugin implements HardwarePlugin {

	@Override
	public void init(HardwareInterface hw) {

	}

	@Override
	public Object exec(String command, Object params) throws Exception {
		if(command.equals("LOAD"))
		{
			if(params instanceof InputStream)
			{
				InputStream is = (InputStream) params;
				
				return pngToNewSurface(is);
			}
			else if (params instanceof String) {
				try(FileInputStream fis = new FileInputStream((String)params))
				{
					return pngToNewSurface(fis);
				}
			}
		}
		else if(command.equals("LOAD_TO_SURF"))
		{
			Object[] p = (Object[]) params;
			Integer surfNum = (Integer) p[0];
			Object dest = p[1];
			if(dest instanceof InputStream)
			{
				InputStream is = (InputStream) dest;
				
				pngToSurface(surfNum, is);
			}
			else if (dest instanceof String) {
				try(FileInputStream fis = new FileInputStream((String)dest))
				{
					pngToSurface(surfNum, fis);
				}
			}
		}
		else if(command.equals("SAVE"))
		{
			Object[] p = (Object[]) params;
			Integer surfNum = (Integer) p[0];
			Object dest = p[1];
			if(dest instanceof OutputStream)
			{
				OutputStream os = (OutputStream) dest;
				
				return surfaceToPng(surfNum, os);
			}
			else if (dest instanceof String) {
				try(FileOutputStream fos = new FileOutputStream((String)dest))
				{
					return surfaceToPng(surfNum, fos);
				}
			}
		}
		else if(command.equals("SIZE"))
		{
			return ((GdxOmicron)Omicron.sys()).surfSizes((Integer)params);
			
		}
		throw new RuntimeException("Unknown command");
	}

	private int pngToNewSurface(InputStream is) throws IOException {

		BufferedImage bi = ImageIO.read(is);
		
		int w = bi.getWidth();
		int h = bi.getHeight();
		
		int nSurf = ((GdxOmicron)Omicron.sys()).newSurface(w, h);
				
		
		for (int x = 0; x < w; x++) {
        	for (int y = 0; y < h; y++) {
        		int colot = (bi.getRGB(x, y) << 8) + 255;
        		Sys.fill(nSurf, x, y, 1, 1, colot);
			}
		}

		return nSurf;
		
	}

	private Object surfaceToPng(int surfNum, OutputStream fos) throws IOException {
		int[] size = ((GdxOmicron)Omicron.sys()).surfSizes((Integer)surfNum);
		
		int w = size[0];
		int h = size[1];
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < w; x++) {
        	for (int y = 0; y < h; y++) {
        		int color = Sys.getPix(surfNum, x, y) >> 8;
        		img.setRGB(x, y, color);
			}
		}

		ImageIO.write(img, "PNG", fos);
		return true;
	}

	private Object pngToSurface(int surfNum, InputStream is) throws IOException {
		
		BufferedImage bi = ImageIO.read(is);
		int[] size = ((GdxOmicron)Omicron.sys()).surfSizes((Integer)surfNum);
		
		int w = size[0];
		int h = size[1];
		
		if(bi.getWidth() != w || bi.getHeight() != h)
		{
			BufferedImage resized = new BufferedImage(w, h, bi.getType());
			Graphics2D g = resized.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(bi, 0, 0, w, h, 0, 0, bi.getWidth(),
			    bi.getHeight(), null);
			g.dispose();
			bi = resized;
		}
		
		for (int x = 0; x < w; x++) {
        	for (int y = 0; y < h; y++) {
        		int colot = (bi.getRGB(x, y) << 8) + 255;
        		Sys.fill(surfNum, x, y, 1, 1, colot);
			}
		}

		return true;
	}
	
	public static byte[] read(InputStream is) throws IOException
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}

		return buffer.toByteArray();
	}

}
