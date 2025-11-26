package com.github.msx80.omicron.fantasyconsole.cartridges;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.adv.Cartridge;

import com.github.msx80.omicron.fantasyconsole.utils.FileUtil;

public class ClassCartridge implements Cartridge {

	
	private Game game;
	private Class<? extends Game> gameClass;
	
	public ClassCartridge(String name, Class<? extends Game> gameClass)
	{
		this.gameClass = gameClass;
	}
	
	@Override
	public Game getGameObject(){
		try {
			game = gameClass.getDeclaredConstructor().newInstance();
			return game;
		} catch (Exception e) {
			throw new RuntimeException("Unable to instantiate class: "+e.getMessage(), e);
		}
	}

	@Override
	public byte[] loadFile(String name) {
		try {
			//String path = '/'+props.getProperty(PROP_PKG).replace('.', '/')+'/';
			String path = '/'+gameClass.getPackage().getName().replace('.', '/')+'/';
			InputStream is = game.getClass().getResourceAsStream(path+name);
			if(is == null) return null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try
			{
				FileUtil.copy(is, baos);
			}
			finally
			{
				is.close();
			}
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Unable to load file from cartridge",e);
		}
	}

	@Override
	public Properties getOmicronProperties() {
		
		return null;
	}

	@Override
	public void close() {
		
		
	}


}
