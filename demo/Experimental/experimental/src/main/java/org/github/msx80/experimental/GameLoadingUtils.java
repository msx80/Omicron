package org.github.msx80.experimental;

import java.io.File;
import java.util.Map;

import org.github.msx80.crazyclassloader.BytesLoader;
import org.github.msx80.crazyclassloader.DynamicClassLoader;
import org.github.msx80.crazyclassloader.otf.CompilingLoader;
import org.github.msx80.omicron.api.Game;

public class GameLoadingUtils {

	public static Game compileGameOnTheFly(String className, Map<String, String> sources) {
		
		
		try {
			CompilingLoader cl = new CompilingLoader(sources);
			Class<?> userClass = new DynamicClassLoader(cl).loadClass(className);
			
			return (Game) userClass.newInstance();
		
		} catch (Exception e) {
			throw new RuntimeException("Unable to compile Game: "+e.getMessage(), e);
		}
	}
	
	/**
	 * Load a Game from a jar. need to specify the main Game class.
	 * The classloader will make sure that the correct resources in the jar are used. And will attempt to load all referenced classes from this jar
	 * @param className
	 * @param jarFile
	 * @return
	 */
	public static Game loadGameFromJar(String className, File jarFile) {
		
		try {
			Class<?> userClass = new DynamicClassLoader(BytesLoader.fileLoader(jarFile)).loadClass(className);
			return (Game) userClass.newInstance();
		
		} catch (Exception e) {
			throw new RuntimeException("Unable to load Game from jar: "+e.getMessage(), e);
		}
	}
	
}
