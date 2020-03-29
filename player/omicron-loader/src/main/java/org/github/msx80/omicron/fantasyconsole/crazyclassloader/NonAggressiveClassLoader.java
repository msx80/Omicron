package org.github.msx80.omicron.fantasyconsole.crazyclassloader;

import java.security.Policy;
import java.util.HashSet;
import java.util.Set;

/**
 * Load all classes it can, leave the rest to the Parent ClassLoader
 */
public abstract class NonAggressiveClassLoader extends SClassLoader {

	Set<String> loadedClasses = new HashSet<>();
	Set<String> unavaiClasses = new HashSet<>();
    private ClassLoader parent = NonAggressiveClassLoader.class.getClassLoader();
	//private ProtectionDomain pd;

	public NonAggressiveClassLoader() {
		
		Policy.setPolicy(new SandboxSecurityPolicy());
		System.setSecurityManager(new SecurityManager());      
		  
	}
	
    @Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
    	Class<?> a;
		try {
			a = parent.loadClass(name);
		} catch (ClassNotFoundException e) {
			byte[] newClassData = loadNewClass(name);
			if (newClassData != null) {
				loadedClasses.add(name);
				return define(newClassData, name);
			} else {
				unavaiClasses.add(name);
				return parent.loadClass(name);
			}

		}
		return a;
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