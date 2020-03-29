package org.github.msx80.omicron.fantasyconsole.crazyclassloader;

import java.security.Policy;
import java.util.HashSet;
import java.util.Set;

/**
 * Load all classes it can, leave the rest to the Parent ClassLoader
 */
public abstract class AggressiveClassLoader extends SClassLoader {

	Set<String> loadedClasses = new HashSet<>();
	Set<String> unavaiClasses = new HashSet<>();
    private ClassLoader parent = AggressiveClassLoader.class.getClassLoader();
	//private ProtectionDomain pd;

	public AggressiveClassLoader() {
		
		Policy.setPolicy(new SandboxSecurityPolicy());
		System.setSecurityManager(new SecurityManager());      
		  
		  
	/*	try {
			pd = new ProtectionDomain(new CodeSource(new URL("memory", "", -1, "file", new URLStreamHandler() {
				
				@Override
				protected URLConnection openConnection(URL u) throws IOException {
					// TODO Auto-generated method stub
					return null;
				}
			}),new Certificate[0] ), new PermissionCollection() {
				
				@Override
				public boolean implies(Permission permission) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public Enumeration<Permission> elements() {
					// TODO Auto-generated method stub
					return new Vector().elements();
				}
				
				@Override
				public void add(Permission permission) {
					System.out.println("Added "+permission);
					
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		*/
	}
	
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