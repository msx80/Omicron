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

import javax.lang.model.SourceVersion;

import java.util.Objects;

import org.github.msx80.omicron.fantasyconsole.utils.FileUtil;
import org.github.msx80.omicron.fantasyconsole.utils.ReplacingInputStream;

public class ProjectCreator {

	static Charset cs = Charset.forName("ASCII");
	public static Path createProject(Path dest, String name, String pkg, String folderAndClassName) throws IOException
	{
		Objects.requireNonNull(dest, "dest is null");
		Objects.requireNonNull(name, "name is null");
		Objects.requireNonNull(pkg, "pkg is null");
		Objects.requireNonNull(folderAndClassName, "main is null");
		
		if(name.equals("")) throw new RuntimeException("Name is empty");
		if(folderAndClassName.equals("")) throw new RuntimeException("main is empty");
		if(pkg.equals("")) throw new RuntimeException("Package is empty");
		if(!pkg.contains(".")) throw new RuntimeException("Package should have at least two portions (ie: com.something)");
		
		if(!folderAndClassName.matches("[A-Za-z0-9]+")) throw new RuntimeException("Class name is not alphanumeric");
		
		if(!dest.isAbsolute())
		{
			throw new RuntimeException("Dest is not an absolute path");
		}
		
		if(SourceVersion.isIdentifier(folderAndClassName) && !SourceVersion.isKeyword(folderAndClassName))
		{
			// ok
		}
		else
		{
			throw new RuntimeException("Class name is invalid.");
		}
		
		Path f = dest.resolve(folderAndClassName);
		if(Files.exists(f))
		{
			throw new RuntimeException("Path already exists.");
		}
		
		Map<byte[], byte[]> subs = new HashMap<>();
		subs.put("%%NAME%%".getBytes(cs), name.getBytes(cs));
		subs.put("%%PKG%%".getBytes(cs), pkg.getBytes(cs));
		subs.put("%%MAIN%%".getBytes(cs), folderAndClassName.getBytes(cs));
		
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
		copyFile(src, "HelloWorld.jav", subs, folderAndClassName+".java");
		
		Path resources = f.resolve("src/main/resources").resolve(pkg.replace('.', '/'));
		Files.createDirectories(resources);
		copyFile(resources, "sheet1.png", Collections.EMPTY_MAP, null);
		copyFile(resources, "sheet2.png", Collections.EMPTY_MAP, null);
		
		return f;
	
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
		createProject(Paths.get("c:\\nicola"), "Creator", "org.github.msx80.airplane", "Creator");
	}
}
