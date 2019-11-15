package org.github.msx80.crazyclassloader;

import java.util.HashSet;
import java.util.Set;

/**
 * Load all classes it can, leave the rest to the Parent ClassLoader
 */
public abstract class AggressiveClassLoader extends ClassLoader {

	Set<String> loadedClasses = new HashSet<>();
	Set<String> unavaiClasses = new HashSet<>();
    private ClassLoader parent = AggressiveClassLoader.class.getClassLoader();

    @Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (loadedClasses.contains(name) || unavaiClasses.contains(name)) {
			return super.loadClass(name); // Use default CL cache
		}

		byte[] newClassData = loadNewClass(name);
		if (newClassData != null) {
			loadedClasses.add(name);
			return define(newClassData, name);
		} else {
			unavaiClasses.add(name);
			return parent.loadClass(name);
		}
	}


	protected abstract byte[] loadNewClass(String name);

	private Class<?> define(byte[] classData, String name) {
		Class<?> clazz = defineClass(name, classData, 0, classData.length);
		if (clazz != null) {
			if (clazz.getPackage() == null) {
				definePackage(name.replaceAll("\\.\\w+$", ""), null, null, null, null, null, null, null);
			}
			resolveClass(clazz);
		}
		return clazz;
	}

	public static String toFilePath(String name) {
		return name.replaceAll("\\.", "/") + ".class";
	}
	
	
}