package omicron.teavm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.FileUtils;
import org.teavm.tooling.ConsoleTeaVMToolLog;
import org.teavm.tooling.TeaVMTargetType;
import org.teavm.tooling.TeaVMTool;
import org.teavm.vm.TeaVMOptimizationLevel;

import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.fantasyconsole.cartridges.InsecureJarCartridge;
import com.github.msx80.omicron.fantasyconsole.cartridges.JarLoader;
import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;
import com.github.xpenatan.gdx.backends.teavm.config.plugins.TeaReflectionSupplier;
import com.squareup.tools.maven.resolution.Artifact;
import com.squareup.tools.maven.resolution.ArtifactResolver;
import com.squareup.tools.maven.resolution.FetchStatus.RepositoryFetchStatus.SUCCESSFUL;
import com.squareup.tools.maven.resolution.ResolutionResult;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

public class TeaBuild {


	public static void main(String[] args) throws Exception 
	{
		String artId = System.getProperty("cartridge.artifactId");
		String grpId = System.getProperty("cartridge.groupId");
		String version = System.getProperty("cartridge.version");
		
		if(grpId == null || artId ==  null || version == null)
		{
			System.out.println("No cartridge specified. Please pass the artifact coordinates like this:");
			System.out.println(" -Dcartridge.artifactId=snake -Dcartridge.groupId=com.github.msx80.omicron -Dcartridge.version=0.0.3");
			System.exit(1);
		}
		
		String coord = grpId+":"+artId+":"+version;
		
		System.out.println("Generating web cartridge for: "+coord);
		
		Set<Path> paths = new HashSet<>();
		Path jar = getArtifact(coord, paths);
		
		System.out.println("All artifacts retrieved, cartridge jar found at: "+jar);
		
		try(JarLoader j = new JarLoader(jar.toFile())) {
		
			InsecureJarCartridge cart = new InsecureJarCartridge(j);
			
			Properties p = cart.getOmicronProperties();
			System.out.println("Omicron properties: "+p);
			String pkg = p.getProperty("omicron.pkg");
			String main = p.getProperty("omicron.main");
			String name = p.getProperty("omicron.name");
			
			String fqn = pkg+"."+main;
			System.out.println("Main class fqn: "+fqn);
			Path emitter = generateEmitter(fqn);
			
			System.out.println("Cart: "+name);
			System.out.println("Main class: "+fqn);
			FileUtils.deleteDirectory(new File("build/dist/assets"));
			
			Path assets = loadAssets(cart);
			
			loadOmicronAssets(assets);
			
	        TeaReflectionSupplier.addReflectionClass(fqn);
	        TeaReflectionSupplier.addReflectionClass("emitter.Emitter");
	        TeaReflectionSupplier.addReflectionClass("com.github.msx80.omicron.plugins.builtin.ArgsPlugin");
	        TeaReflectionSupplier.addReflectionClass("com.github.msx80.omicron.plugins.builtin.PlatformPlugin");
	        TeaReflectionSupplier.addReflectionClass("com.github.msx80.omicron.plugins.builtin.SaveFilePlugin");
	        TeaReflectionSupplier.addReflectionClass("com.github.msx80.omicron.plugins.builtin.StatePlugin");
	        // this one doesn't work as BufferedImage is not in TeaVM
	        //TeaReflectionSupplier.addReflectionClass("com.github.msx80.omicron.plugins.builtin.SurfacePlugin");
	        TeaReflectionSupplier.addReflectionClass("com.github.msx80.omicron.plugins.builtin.UrlOpenerPlugin");
 
	        TeaBuildConfiguration teaBuildConfiguration = new TeaBuildConfiguration();
	        teaBuildConfiguration.classesToPreserve.add(fqn);
	        teaBuildConfiguration.classesToPreserve.add("emitter.Emitter");
	        teaBuildConfiguration.additionalClasspath.add(emitter.toUri().toURL());
	        for(Path deps : paths)
	        {
	        	teaBuildConfiguration.additionalClasspath.add(deps.toUri().toURL());
	        }
	        
	        teaBuildConfiguration.targetType = TeaVMTargetType.WEBASSEMBLY_GC;
	        teaBuildConfiguration.assetsPath.add(new AssetFileHandle(assets.toString()));
	        teaBuildConfiguration.webappPath = new File("build/dist").getCanonicalPath();
	        TeaBuilder.config(teaBuildConfiguration);

	        TeaVMTool tool = new TeaVMTool();
	        tool.getClassesToPreserve().add("emitter.Emitter");
	        tool.getClassesToPreserve().add(fqn);
	        tool.setObfuscated(true);
	        tool.setOptimizationLevel(TeaVMOptimizationLevel.ADVANCED);
	        tool.setMainClass(TeaLauncher.class.getName());
	        //tool.setDebugInformationGenerated(true);
	        tool.setLog(new ConsoleTeaVMToolLog(true));
	        int size = 64 * (1 << 20);
	        tool.setMaxDirectBuffersSize(size);
	        
	        // System.out.println(tool.getClassesToPreserve());
	        TeaBuilder.build(tool, true);

	        FileUtils.deleteDirectory(assets.toFile());
	        FileUtils.delete(emitter.toFile());
		}
	}
	
	private static Path getArtifact(String artCoords, Set<Path> paths)
	{
		ArtifactResolver a = new ArtifactResolver();

		Artifact af = a.artifactFor(artCoords);
		
		ResolutionResult ra = a.resolve(af);
		if(!(ra.getStatus() instanceof SUCCESSFUL))
		{
			System.out.println("Unable to find artifact "+artCoords+" -> "+ra.getStatus().getClass().getSimpleName());
			System.exit(1);
		}
		var fs = a.downloadArtifact(ra.getArtifact());
		System.out.println("Download result: "+fs.getClass().getSimpleName());
		if(!(fs instanceof SUCCESSFUL))
		{
			System.out.println("Unable to download artifact "+artCoords+" -> "+ra.getStatus().getClass().getSimpleName());
			System.exit(1);
		}
		System.out.println("FILE: "+ra.getArtifact().getMain().getLocalFile());
		for( var dep :  ra.getArtifact().getModel().getDependencies())
		{
			if ("compile".equalsIgnoreCase(dep.getScope()))
			{
				System.out.println("Dep to include:"+dep);
				getArtifact(dep.getGroupId()+":"+dep.getArtifactId()+":"+dep.getVersion(), paths);
			}
		}
		Path p = ra.getArtifact().getMain().getLocalFile();
		paths.add( p );
		return p;
	}
		
	

	private static void loadOmicronAssets(Path assets) throws IOException {
		try(InputStream is = TeaBuild.class.getResourceAsStream("/omicrondefaultfont.png"))
		{
			byte[] arr = is.readAllBytes();
			Files.write(assets.resolve("omicrondefaultfont.png"), arr);
		}
		
	}

	private static Path generateEmitter(String fqn) throws IOException 
	{
		// to make a generic Launcher i need to somehow pass the class to launch.
		// i couldn't find a way to pass a parameter at startup so i ended up with this
		
		File jarFile = File.createTempFile("emitter", ".jar");
		System.out.println("Generating emitter");
		var k = new ByteBuddy()
		  .subclass(Object.class)
		  .name("emitter.Emitter")
		  .method(ElementMatchers.named("toString"))
		  .intercept(FixedValue.value(fqn))
		  .make();
		
		try ( JarOutputStream j = new JarOutputStream(new FileOutputStream(jarFile)) )
		{
			j.putNextEntry(new JarEntry("emitter/"));
			j.closeEntry();
			j.putNextEntry(new JarEntry("emitter/Emitter.class"));
			j.write(k.getBytes());
			j.closeEntry();
		}
		
		return jarFile.toPath();
	}

	private static Path loadAssets(Cartridge c) throws Exception {
		Path tempDirWithPrefix = Files.createTempDirectory("omicronweb");
		System.out.println("Temp asset dir: "+tempDirWithPrefix);
		loadAssetType(c, tempDirWithPrefix, "sheet", ".png");
		loadAssetType(c, tempDirWithPrefix, "sound", ".wav");
		loadAssetType(c, tempDirWithPrefix, "music", ".mp3");
		loadAssetType(c, tempDirWithPrefix, "file", ".bin");
		return tempDirWithPrefix;
	}

	private static void loadAssetType(Cartridge c, Path tempDirWithPrefix, String prefix, String postfix)
			throws IOException {
		int i = 1;
		while(true)
		{
			String name = prefix+i+postfix;
			byte[] data = c.loadFile(name);
			if(data == null) break;
			Files.write(tempDirWithPrefix.resolve(name), data);
			
			i++;
		}
	}
}
