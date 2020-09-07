package org.github.msx80.omicron.player;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.github.msx80.omicron.DesktopLauncher;
import org.github.msx80.omicron.api.adv.Cartridge;
import org.github.msx80.omicron.fantasyconsole.cartridges.ClasspathCartridge;

public class OmicronPlayerMain {

	
	public static void main(String[] args)
	{
		
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);

		Cartridge c = new ClasspathCartridge("Player", "org.github.msx80.omicron.fantasyconsole", "OmicronPlayer");
		
		DesktopLauncher.launch(c, false, args);
	}
}
