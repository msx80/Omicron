package org.github.msx80.omicron;

import java.io.InputStream;

import org.github.msx80.omicron.api.adv.CustomResourceGame;

import com.badlogic.gdx.files.FileHandleStream;

public class ResourceFileHandle extends FileHandleStream {

	private Class<?> cls;
	private String resourceName;
	private CustomResourceGame game;


	public ResourceFileHandle(String resourceName, CustomResourceGame game) {
		super(resourceName);
		this.cls = null;
		this.resourceName = resourceName;
		this.game = game;
	}
	

	public ResourceFileHandle(String resourceName, Class<?> cls) {
		super(resourceName);
		this.cls = cls;
		this.resourceName = resourceName;
		this.game = null;
	}
	
	
	public InputStream read () {
		return cls == null ? game.getResourceAsStream(resourceName) : cls.getResourceAsStream(resourceName);
	}


	@Override
	public boolean exists() {
		return read() != null;
	}

	
	

}
