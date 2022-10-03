package com.github.msx80.omicron.fantasyconsole;

import java.io.File;
import java.util.Map;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.fantasyconsole.cartridges.InsecureJarCartridge;
import com.github.msx80.omicron.fantasyconsole.cartridges.JarLoader;
import com.github.msx80.omicron.fantasyconsole.cartridges.SecureJarCartridge;

public class CartridgeLoadingUtils {

	public static Game compileGameOnTheFly(String className, Map<String, String> sources, Map<String, byte[]> files) {
		

		// this works, just need refactoring
		/*
		try {
			CompilingLoader cl = new CompilingLoader(sources, files);
			Class<?> userClass = new DynamicClassLoader(cl).loadClass(className);
			
			return (Game) userClass.newInstance();
		
		} catch (CompilationError e) {
			throw e;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		*/
		return null;
	}
	

	
	/**
	 * Load a Game from a jar. need to specify the main Game class.
	 * The classloader will make sure that the correct resources in the jar are used. And will attempt to load all referenced classes from this jar
	 * @param className
	 * @param jarFile
	 * @param safe 
	 * @return
	 */
	/*
public static Game loadGameFromJar(File jarFile) {
		
		try {
			System.out.println("Jar loading 1");
			BytesLoader bl = BytesLoader.fileLoader(jarFile);
			byte[] prop = bl.loadFile("omicron.properties");
			bl.close();
			System.out.println("Jar loading 2");
			if(prop== null) throw new RuntimeException("Zip does not contains omicron.properties file.");
			Properties p = new Properties();
			p.load(new ByteArrayInputStream(prop));
			
			System.out.println("Jar loading 3");
			String className = p.getProperty("pkg")+"."+p.getProperty("main");
			
			System.out.println("Jar loading 4: "+className);
			URL fileURL = jarFile.toURI().toURL();
			String jarURL = "jar:" + fileURL + "!/";
			URL urls [] = { new URL(jarURL) };
			URLClassLoader ucl = new URLClassLoader(urls);
			System.out.println("Jar loading 5");
			Class<?> aa  = ucl.loadClass(className);
			//Class<?> aa = Class.forName(className, true,   ucl);
			System.out.println("Jar loading 6");
			System.out.println("Jar loading "+aa.getCanonicalName());
			return (Game)aa.newInstance();
			
		
		} catch (Exception e) {
			throw new RuntimeException("Unable to load Game from jar: "+e.getMessage(), e);
		}
	}
*/


	public static Cartridge fromOmicronFile(File jarFile, boolean safe) {
		
		try {
			if (safe)
			{
				return new SecureJarCartridge(new JarLoader( jarFile));
			}
			else
			{
				return new InsecureJarCartridge(new JarLoader( jarFile));
			}
		
		} catch (Exception e) {
			throw new RuntimeException("Unable to load Game from jar: "+e.getMessage(), e);
		}
	}
	
	/*
	 *  
	  VersioneconAggressive che funziona
	public static Game loadGameFromJar(File jarFile) {
	
	try {
		BytesLoader bl = BytesLoader.fileLoader(jarFile);
		byte[] prop = bl.loadFile("omicron.properties");
		if(prop== null) throw new RuntimeException("Zip does not contains omicron.properties file.");
		Properties p = new Properties();
		p.load(new ByteArrayInputStream(prop));
		
		
		String className = p.getProperty("pkg")+"."+p.getProperty("main");
		Class<?> userClass = new DynamicClassLoader(bl).loadClass(className);
		return (Game) userClass.newInstance();
	
	} catch (Exception e) {
		throw new RuntimeException("Unable to load Game from jar: "+e.getMessage(), e);
	}
	
}*/

}
