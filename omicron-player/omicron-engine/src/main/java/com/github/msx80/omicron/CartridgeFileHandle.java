package com.github.msx80.omicron;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.badlogic.gdx.files.FileHandleStream;
import com.github.msx80.omicron.api.adv.Cartridge;

public class CartridgeFileHandle extends FileHandleStream {

	private byte[] data;
	
	public CartridgeFileHandle(Cartridge cartridge, String resourceName) 
	{
		super(resourceName);
		this.data = cartridge.loadFile(resourceName);
	}


	public InputStream read () {
		return data == null ? null : new ByteArrayInputStream(data);
	}


	@Override
	public boolean exists() {
		return read() != null;
	}

	
	

}
