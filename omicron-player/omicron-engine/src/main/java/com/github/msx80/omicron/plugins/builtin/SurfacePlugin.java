package com.github.msx80.omicron.plugins.builtin;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.graphics.Pixmap;
import com.github.msx80.omicron.GdxOmicron;
import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;
import com.github.msx80.omicron.api.Omicron;

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
				
				return pngToSurface(is);
			}
			else if (params instanceof String) {
				try(FileInputStream fis = new FileInputStream((String)params))
				{
					return pngToSurface(fis);
				}
			}
		}
		else if(command.equals("SIZE"))
		{
			return ((GdxOmicron)Omicron.sys()).surfSizes((Integer)params);
			
		}
		throw new RuntimeException("Unknown command");
	}

	private Object pngToSurface(InputStream is) throws IOException {
		byte[] arr = read(is);
		Pixmap px = new Pixmap(arr, 0, arr.length);
		
		return ((GdxOmicron)Omicron.sys()).newSurface(px);
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
