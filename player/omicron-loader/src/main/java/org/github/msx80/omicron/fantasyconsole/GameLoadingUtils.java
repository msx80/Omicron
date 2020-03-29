package org.github.msx80.omicron.fantasyconsole;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Properties;

import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.fantasyconsole.crazyclassloader.DynamicClassLoader;
import org.github.msx80.omicron.fantasyconsole.crazyclassloader.bytesloader.BytesLoader;
import org.github.msx80.omicron.fantasyconsole.crazyclassloader.otf.CompilationError;
import org.github.msx80.omicron.fantasyconsole.crazyclassloader.otf.CompilingLoader;

public class GameLoadingUtils {

	public static Game compileGameOnTheFly(String className, Map<String, String> sources, Map<String, byte[]> files) {
		
		
		try {
			CompilingLoader cl = new CompilingLoader(sources, files);
			Class<?> userClass = new DynamicClassLoader(cl).loadClass(className);
			
			return (Game) userClass.newInstance();
		
		} catch (CompilationError e) {
			throw e;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	
	/**
	 * Load a Game from a jar. need to specify the main Game class.
	 * The classloader will make sure that the correct resources in the jar are used. And will attempt to load all referenced classes from this jar
	 * @param className
	 * @param jarFile
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
