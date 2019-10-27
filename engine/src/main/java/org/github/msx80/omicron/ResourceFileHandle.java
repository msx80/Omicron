package org.github.msx80.omicron;

import java.io.InputStream;

import com.badlogic.gdx.files.FileHandleStream;

public class ResourceFileHandle extends FileHandleStream {

	private Class<?> cls;
	private String resourceName;


	public ResourceFileHandle(String resourceName, Class<?> cls) {
		super(resourceName);
		this.cls = cls;
		this.resourceName = resourceName;
	}
	
	
	public InputStream read () {
		return cls.getResourceAsStream(resourceName);
	}


	@Override
	public boolean exists() {
		return cls.getResourceAsStream(resourceName) != null;
	}

	
	

}
