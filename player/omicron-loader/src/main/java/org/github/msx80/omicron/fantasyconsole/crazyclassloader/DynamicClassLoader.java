package org.github.msx80.omicron.fantasyconsole.crazyclassloader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import org.github.msx80.omicron.fantasyconsole.crazyclassloader.bytesloader.BytesLoader;

public class DynamicClassLoader extends NonAggressiveClassLoader {
	BytesLoader[] loaders;
	

	public DynamicClassLoader(BytesLoader... loaders) {
		this.loaders = loaders;
	}

	@Override
	protected byte[] loadNewClass(String name) {
		System.out.println("Loading class " + name);
		
		for (BytesLoader loader : loaders) {
			byte[] data = loader.loadClass(name);
			if (data!= null) {
				return data;
			}
		}
		return null;
	}

	/*
	@Override
	protected void finalize() throws Throwable {
		System.out.println("Finalizing classloader!");
		super.finalize();
	}
	*/
	
	@Override
	public URL getResource(String name) {
		throw new UnsupportedOperationException("getResource can't return a meaningful URL, use getResourceAsStream");
	}

	public InputStream getResourceAsStream(String name) {
		  for (BytesLoader loader : loaders) {
				byte[] data = loader.loadFile(name);
				if (data!= null) {
					return new ByteArrayInputStream(data);
				}
			}
			return null;
	    }
	
	
}
