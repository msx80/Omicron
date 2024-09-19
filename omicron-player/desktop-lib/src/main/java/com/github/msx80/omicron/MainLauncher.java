package com.github.msx80.omicron;

import java.io.IOException;
import java.util.Properties;

import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.fantasyconsole.cartridges.ClasspathCartridge;

public class MainLauncher {

	public static void main(String[] args) throws IOException {
		java.io.InputStream is = MainLauncher.class.getClassLoader().getResourceAsStream("omicron.properties");
		if(is == null) throw new RuntimeException("Unable to find omicron.properties where expected.");
		java.util.Properties p = new Properties();
		p.load(is);
		String gameClass = p.getProperty("omicron.pkg")+"."+p.getProperty("omicron.main");
		
		int n = gameClass.lastIndexOf('.');
		String pkg = gameClass.substring(0,  n);
		String main = gameClass.substring(n+1);
	
		Cartridge c = new ClasspathCartridge("Gamex", pkg, main);
		DesktopLauncher.launch(c, false, args);

	}

}
