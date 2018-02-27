package com.github.msx80.runtime.loader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.badlogic.gdx.Gdx;
import com.github.msx80.omicron.Game;
import com.github.msx80.runtime.OmicronException;

public class CartridgeLoader {

	public static Cartridge load(File cartridgeFile) {
		try {
			Properties pp = new Properties();
			byte[] cover  = findCartridgePropertiesAndCover(cartridgeFile, pp);
			CartridgeClassLoader c = new CartridgeClassLoader(cartridgeFile);
			Game g = c.getGame(pp.getProperty("entry"));
			return new Cartridge(cartridgeFile, g, pp, cover);
		} catch (Exception e) {
			throw new OmicronException("Unable to load cartridge", e);
		}
	}

	private static byte[] findCartridgePropertiesAndCover(File file, Properties prop) throws Exception {
		InputStream input;
		Properties p = null;
		byte[] cover = null;
		input = new FileInputStream(file);
		try {
			ZipInputStream zip = new ZipInputStream(input);
			ZipEntry entry = zip.getNextEntry();

			while (entry != null) {

				if ("omicron.properties".equals(entry.getName())) {
					p = new Properties();
					p.load(zip);
					System.out.println("Properties: "+p);
				}
				if ("omicron.png".equals(entry.getName())) {
					DataInputStream dis = new DataInputStream(zip);
					cover = new byte[(int) entry.getSize()];
					dis.readFully(cover);
					System.out.println("Cover found.");
				}
				
				entry = zip.getNextEntry();

			}
		} finally {
			input.close();
		}
		if(p==null)	throw new IOException("No omicron.properties found on cartridge.");
		if(cover==null)	throw new IOException("No omicron.png found on cartridge.");	
		prop.putAll(p);
		return cover;
	}
}
