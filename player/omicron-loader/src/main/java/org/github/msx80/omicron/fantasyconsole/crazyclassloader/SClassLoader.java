package org.github.msx80.omicron.fantasyconsole.crazyclassloader;

public class SClassLoader extends ClassLoader {
	public static String toFilePath(String name) {
		return name.replaceAll("\\.", "/") + ".class";
	}

}
