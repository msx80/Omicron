package org.github.msx80.experimental;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.github.msx80.omicron.DesktopLauncher;

public class MainLauncher {

	
	public static void main(String[] args)
	{
		
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);
		
		DesktopLauncher.launch(new Main(), false);

	}
}
