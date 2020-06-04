package org.github.msx80.omicron.fantasyconsole.cartridges;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.adv.Cartridge;
import org.github.msx80.omicron.fantasyconsole.utils.FileUtil;

public class ClasspathCartridge implements Cartridge {

	private Properties props;
	private Game game;
	
	public ClasspathCartridge(String name, String pkg, String main)
	{
		props = new Properties();
		props.put("name", name);
		props.put("pkg", pkg);
		props.put("main", main);
	}
	public ClasspathCartridge(Properties omicronProperties) {
		this.props = omicronProperties;
	}

	@Override
	public Game getGameObject(){
		try {
			String className = props.getProperty("pkg")+"."+props.getProperty("main");
			
			game = (Game) Class.forName(className).newInstance();
			return game;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] loadFile(String name) {
		try {
			InputStream is = game.getClass().getResourceAsStream(name);
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
		
		return props;
	}

	@Override
	public void close() {
		
		
	}


}
