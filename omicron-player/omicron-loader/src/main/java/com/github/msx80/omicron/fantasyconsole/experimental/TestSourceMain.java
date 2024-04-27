package com.github.msx80.omicron.fantasyconsole.experimental;

import java.io.File;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.fantasyconsole.cartridges.JarLoader;
import com.github.msx80.omicron.fantasyconsole.cartridges.SourceCartridge;

public class TestSourceMain {

	public static void main(String[] args) throws Exception {
//		JkLog.setDecorator(new JkIndentLogDecorator());
//		JkLog.setVerbosity(Verbosity.QUITE_VERBOSE);
//		JkDependencySet deps = JkDependencySet.of()
//                .and("org.apereo.cas:cas-server-core-authentication:6.5.0-RC4")
//                .and("com.github.msx80:omicron-api:0.0.1")
//                .and("com.threerings:tripleplay:1.4");
//		
//        JkDependencyResolver resolver = JkDependencyResolver.of()
//        		.addRepos(JkRepo.of("https://jitpack.io"))
//        		.addRepos(JkRepo.ofMavenCentral())
//        		;
//        List<Path> libs = resolver.resolve(deps).getFiles().getEntries();
		
//        for (Path path : libs) {
//			System.out.println(path);
//		}
//		
			SourceCartridge sc = new SourceCartridge(new JarLoader(new File("C:\\Users\\msx_8\\dev\\Omicron\\omicron\\demo\\Retrodrawing\\RetroDrawerSrc.omicron")));
			Game g = sc.getGameObject();
		
			System.out.println(g.sysConfig());
	}

}
