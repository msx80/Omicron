package org.github.msx80.omicron.fantasyconsole;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.github.msx80.omicron.fantasyconsole.utils.FileUtil;
import org.github.msx80.omicron.fantasyconsole.utils.ReplacingInputStream;

public class ProjectCreator {

	static Charset cs = Charset.forName("ASCII");
	public static void createProject(Path dest, String name, String pkg, String folderAndClassName) throws IOException
	{
		Map<byte[], byte[]> subs = new HashMap<>();
		subs.put("%%NAME%%".getBytes(cs), name.getBytes(cs));
		subs.put("%%PKG%%".getBytes(cs), pkg.getBytes(cs));
		subs.put("%%MAIN%%".getBytes(cs), folderAndClassName.getBytes(cs));
		
		Path f = dest.resolve(folderAndClassName);
		Files.createDirectories(f);
		copyFile(f, "build.gradle", Collections.EMPTY_MAP, null);
		copyFile(f, "gradlew", Collections.EMPTY_MAP, null);
		copyFile(f, "gradlew.bat", Collections.EMPTY_MAP, null);
		copyFile(f, "omicron.properties", subs, null);
		
		Path gw = f.resolve("gradle/wrapper");
		Files.createDirectories(gw);
		
		copyFile(gw, "gradle-wrapper.jar", Collections.EMPTY_MAP, null);
		copyFile(gw, "gradle-wrapper.properties", Collections.EMPTY_MAP, null);

		Path src = f.resolve("src/main/java").resolve(pkg.replace('.', '/'));
		Files.createDirectories(src);
		copyFile(src, "HelloWorld.java", subs, folderAndClassName+".java");
		
		Path resources = f.resolve("src/main/resources");
		Files.createDirectories(resources);
		copyFile(resources, "sheet1.png", Collections.EMPTY_MAP, null);
		
		
	
	}

	private static Path copyFile(Path f, String name, Map<byte[], byte[]> subs, String overrideDestName) throws IOException 
	{
		byte[] bytes = FileUtil.readDataNice(replace(ProjectCreator.class.getResourceAsStream("/omicrontemplate/"+name), subs));
		Path dest = f.resolve(overrideDestName == null ? name : overrideDestName );
		
		Files.write(dest, bytes);
		return dest;
	}
	private static InputStream replace(InputStream is,  Map<byte[], byte[]> subs) {
		for(Entry<byte[], byte[]> e : subs.entrySet())
		{
			is = new ReplacingInputStream(is, e.getKey(), e.getValue());
		}
		return is;
	}

	/*
	private static void copyFile(Path f, String name) throws IOException 
	{
		//byte[] bytes = FileUtil.readDataNice(inputStream)
		Path dest = f.resolve(name);
		try (InputStream is = ProjectCreator.class.getResourceAsStream("/omicrontemplate/"+name);
				OutputStream os = Files.newOutputStream(dest);)
		{
			FileUtil.copy(is, os);
		}
		
	}
	*/
	public static void main(String[] args) throws IOException
	{
		createProject(Paths.get("c:\\nicola"), "Test Game", "bla.ciao.prova", "AwesomeGameMain");
	}
}
