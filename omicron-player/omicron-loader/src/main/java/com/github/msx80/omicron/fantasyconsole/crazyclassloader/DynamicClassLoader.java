package com.github.msx80.omicron.fantasyconsole.crazyclassloader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import com.github.msx80.omicron.fantasyconsole.cartridges.BytesLoader2;

public class DynamicClassLoader extends NonAggressiveClassLoader {
	BytesLoader2[] loaders;
	

	public DynamicClassLoader(BytesLoader2... loaders) {
		this.loaders = loaders;
	}

	public byte[] loadClassBytes(BytesLoader2 b, String className) throws Exception
	{
		String filePath = SClassLoader.toFilePath(className);
		return b.loadFile(filePath);
	}

	
	@Override
	protected byte[] loadNewClass(String name) throws Exception {
		System.out.println("Loading class " + name);
		
		for (BytesLoader2 loader : loaders) {
			byte[] data = loadClassBytes(loader, name);
			if (data!= null) {
				return data;
			}
		}
		return null;
	}

	
	@Override
	protected void finalize() throws Throwable {
		System.out.println("Finalizing classloader!");
		super.finalize();
	}
	
	
	@Override
	public URL getResource(String name) {
		throw new UnsupportedOperationException("getResource can't return a meaningful URL, use getResourceAsStream");
	}

	public InputStream getResourceAsStream(String name) {
		  for (BytesLoader2 loader : loaders) {
				byte[] data;
				try {
					data = loader.loadFile(name);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if (data!= null) {
					return new ByteArrayInputStream(data);
				}
			}
			return null;
	    }
	
	
}
