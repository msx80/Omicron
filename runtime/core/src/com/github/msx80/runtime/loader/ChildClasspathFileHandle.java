package com.github.msx80.runtime.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/***
 * Custom FileHandle to open file inside the cartridge.
 */
public class ChildClasspathFileHandle extends FileHandle {

	private Class<?> clsContext;
	
	public ChildClasspathFileHandle(Class<?> clsContext, String fileName) {
		this.file = new File(fileName);
		this.clsContext = clsContext;
	}
	
	public InputStream read () {
		System.out.println("Loading resourse from cartridge: "+file);
			InputStream input =  clsContext.getResourceAsStream("/" + file.getPath().replace('\\', '/'));
			if (input == null) throw new GdxRuntimeException("File not found: " + file + " (" + type + ")");
			return input;
	}

	@Override
	public long length() {
		
		try {
			InputStream is = clsContext.getResourceAsStream("/" + file.getPath().replace('\\', '/'));
			long res= is.available();
			is.close();
			return res;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	
}
