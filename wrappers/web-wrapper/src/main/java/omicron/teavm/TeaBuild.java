package omicron.teavm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.teavm.vm.TeaVMOptimizationLevel;

import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.builder.TeaBuilder;
import com.github.xpenatan.gdx.teavm.backends.web.config.backend.WebBackend;

public class TeaBuild {


	public static void main(String[] args) throws Exception 
	{
		
		var ss = TeaBuild.class.getResourceAsStream("/omicron.properties");
		Properties p = new Properties();
		p.load(ss);
			
		System.out.println("Omicron properties: "+p);
		String pkg = p.getProperty("omicron.pkg");
		String main = p.getProperty("omicron.main");
		String name = p.getProperty("omicron.name");
		
		String fqn = pkg+"."+main;
		System.out.println("Main class fqn: "+fqn);
		System.out.println("Cart: "+name);
		System.out.println("Main class: "+fqn);
		
		Path assets = loadAssets(pkg);
			
		loadOmicronAssets(assets);
			
		Set<String> reflections = new HashSet<String>();
			
		reflections.add(fqn);
		reflections.add("com.github.msx80.omicron.plugins.builtin.ArgsPlugin");
		reflections.add("com.github.msx80.omicron.plugins.builtin.PlatformPlugin");
		reflections.add("com.github.msx80.omicron.plugins.builtin.SaveFilePlugin");
		reflections.add("com.github.msx80.omicron.plugins.builtin.StatePlugin");
        // this one doesn't work as BufferedImage is not in TeaVM
        //reflections.add("com.github.msx80.omicron.plugins.builtin.SurfacePlugin");
		reflections.add("com.github.msx80.omicron.plugins.builtin.UrlOpenerPlugin");
 
			System.out.println("CLASSPATH: "+System.getProperty("java.class.path"));
		
        WebBackend wb = new WebBackend();
        //wb.setStartJettyAfterBuild(true);
        wb.setHtmlTitle(p.getProperty("omicron.name"));
        wb.setMainClassArgs("[\""+fqn+"\"]");
		var x = new TeaBuilder(wb)
        .addAssets(new AssetFileHandle(assets.toString()))
        .setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE)
        .setMainClass(TeaLauncher.class.getName())
        .setObfuscated(false);
        
        for (String c : reflections) {
        	x.addReflectionClass(c);
		}
        
        String destDir = "target/web/dist";
		x.build(new File(destDir));
		
		System.out.println("Build completed! Distribution is in: "+destDir);

        

        FileUtils.deleteDirectory(assets.toFile());
	
	}
	


	private static void loadOmicronAssets(Path assets) throws IOException {
		try(InputStream is = TeaBuild.class.getResourceAsStream("/omicrondefaultfont.png"))
		{
			byte[] arr = is.readAllBytes();
			Files.write(assets.resolve("omicrondefaultfont.png"), arr);
		}
//		
//		try(InputStream is = TeaBuild.class.getResourceAsStream("/omicron.properties"))
//		{
//			byte[] arr = is.readAllBytes();
//			Files.write(assets.resolve("omicron.properties"), arr);
//		}
//		
	}

	private static Path loadAssets(String c) throws Exception {
		Path tempDirWithPrefix = Files.createTempDirectory("omicronweb");
		System.out.println("Temp asset dir: "+tempDirWithPrefix);
		loadAssetType(c, tempDirWithPrefix, "sheet", ".png");
		loadAssetType(c, tempDirWithPrefix, "sound", ".wav");
		loadAssetType(c, tempDirWithPrefix, "music", ".mp3");
		loadAssetType(c, tempDirWithPrefix, "file", ".bin");
		return tempDirWithPrefix;
	}

	private static void loadAssetType(String pkg, Path tempDirWithPrefix, String prefix, String postfix)
			throws IOException {
		int i = 1;
		while(true)
		{
			String pkgs = pkg.replace('.', '/');
			String base = prefix+i+postfix;
			String name = "/"+pkgs+"/"+base;
			System.out.println("Writing "+name+" to "+ tempDirWithPrefix.resolve(base).toFile());
			InputStream em = TeaBuild.class.getResourceAsStream(name);
			
			if(em == null) break;
			try (
			         OutputStream out = new FileOutputStream(tempDirWithPrefix.resolve(base).toFile())) {
			        
			        em.transferTo(out);
			    }
			i++;
		}
	}
}
