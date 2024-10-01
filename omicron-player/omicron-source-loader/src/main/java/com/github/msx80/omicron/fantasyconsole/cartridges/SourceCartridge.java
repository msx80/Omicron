package com.github.msx80.omicron.fantasyconsole.cartridges;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.wiseloader.BytesLoader;
import com.github.msx80.wiseloader.loaders.JarLoader;
import com.github.msx80.wiseloader.loaders.MultiBytesLoader;
import com.github.msx80.wiseloader.loaders.compiler.CompilingLoader;
import com.github.msx80.wiseloader.loaders.compiler.JarFolder;

import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.depmanagement.JkRepo;
import dev.jeka.core.api.depmanagement.resolution.JkDependencyResolver;

public class SourceCartridge extends AbstractJarCartridge {

	JkDependencyResolver<Void> resolver;
	
	public SourceCartridge(Loader file) throws Exception 
	{
		super(file);
		resolver = JkDependencyResolver.of()
					.addRepos(JkRepo.of("https://jitpack.io"))
					.addRepos(JkRepo.ofMavenCentral())
					;
	}

	private Game game = null;
	
	@Override
	public synchronized Game getGameObject() {
		if (game == null) {
			game = loadGameObject();
		}
		return game;
	}

	private Game loadGameObject() {
		try {
			String className = properties.getProperty(PROP_PKG) + "." + properties.getProperty(PROP_MAIN);
			String[] depss = properties.getProperty("dep", "").split(";");
			
			
			JarFolder[] jarFolders = getBuildDependencies(depss);
			
			Map<String, String> sources = findSourceFiles(this.loader);
			System.out.println("Sources found: "+sources.keySet());
			
			// this is the Loader that will compile classes from Java files in archive
			CompilingLoader cl = new CompilingLoader(sources, new HashMap<>(), jarFolders);
			
			// these are the loaders for all dependency jar
			List<BytesLoader> all = getRuntimeDependencies(depss);
			
			// put all loaders together
			MultiBytesLoader mb = new MultiBytesLoader(all);
			mb.add(cl);
			
			// pass throu the whitelist
			ClassLoader c = SecureJarCartridge.getWhitelistClassLoader(mb);
			
			Class<?> userClass = c.loadClass(className);
			return (Game) userClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to instantiate game object", e);
		}
	}

	private static Map<String, String> findSourceFiles(Loader loader) throws IOException {
		Map<String, String> sources = new HashMap<>();
		loader.forEachFile(entry -> {
				if(entry.startsWith("src/main/java/") && entry.endsWith(".java"))
				{
					// java file!
					String name = entry.substring("src/main/java/".length());
					String clsName = sourceFilepathToClassname(name);
					sources.put(clsName, new String(loader.loadFile(entry)));
				}
			
		});
		return sources;
	}

	private List<BytesLoader> getRuntimeDependencies(String[] depss) {
		// scarico dipendenze:
			JkDependencySet deps = JkDependencySet.of().andGlobalExclusion("com.github.msx80:omicron-api");
					// .and("com.github.msx80:omicron-api:0.0.1"); this is provided by runtime
			for (String string : depss) {
				deps = deps.and(string);
			}
			List<Path> libs = resolver.resolve(deps).getFiles().getEntries();
			System.out.println(libs);
			
			// these are the build dependencies.
			return libs.stream().map(s -> new JarLoader(s.toFile())).collect(Collectors.toList());
	}

	private JarFolder[] getBuildDependencies(String[] depss) {
		
		// download dependencies,  omicron-api is always needed:
		JkDependencySet deps = JkDependencySet.of().and("com.github.msx80:omicron-api:0.0.1");

		// add any custom dependency
		for (String string : depss) {
			deps = deps.and(string);
		}
		        
		List<Path> libs = resolver.resolve(deps).getFiles().getEntries();
		System.out.println(libs);
		
		// these are the build dependencies.
		JarFolder[] jarFolders = libs.stream().map(s -> new JarFolder(s.toFile())).toArray(n -> new JarFolder[n]);
		return jarFolders;
	}
	
	public static String sourceFilepathToClassname(String filename) {
		filename = filename.substring(0, filename.length()-5); // .java
		return filename.replaceAll("/", ".");
	}
	
}
