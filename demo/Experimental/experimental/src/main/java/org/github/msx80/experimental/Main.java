package org.github.msx80.experimental;


import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Mouse;
import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.api.SysConfig;
import org.github.msx80.omicron.api.SysConfig.VirtualScreenMode;
import org.github.msx80.omicron.api.adv.AdvancedSys;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.TextDrawer;
import org.github.msx80.omicron.basicutils.TextDrawer.Align;
import org.github.msx80.omicron.basicutils.TextDrawerVariable;

public class Main implements Game {
	
	public static Random r = new Random(System.nanoTime());
		
	private Sys sys;
	private TextDrawer font = null;
	Mouse m;
	private String result = null;
	BasicWidget w;
	
	public void init(final Sys sys) 
    {
        this.sys = sys;
        font = new TextDrawerVariable(sys, 1, 6, 6,3);       
        w = new BasicWidget(10, 10, 200, 100, font);
        w.setLines(Arrays.asList(
        		
        		Pair.of("Click here to start a game from a Jar", this::runJar),
        		Pair.of("Click here to compile and start a game", this::runCompile)
        		
        		));
    }
	
	
    public void render() 
    {
    	sys.clear(Colors.from(20, 40, 50, 255));
    	   	
    	w.draw();

    	if(result!=null)
    	font.print("Last result was: "+result, 120, 120, Align.CENTER);
    	
        
    }


    public void runJar()
    {
    
    	//Game gg = GameLoadingUtils.compileGameOnTheFly("so.much.dynamic.Stringed", sources);
    	Game gg =  GameLoadingUtils.loadGameFromJar("org.github.msx80.omicron.HelloWorld", new File("C:\\Users\\niclugat\\.m2\\repository\\org\\github\\msx80\\omicron\\omicron-demo\\0.0.1\\omicron-demo-0.0.1.jar"));
		gg = new ExitWithAClickGame(gg);
    	((AdvancedSys)sys).execute(gg, s -> {
    		this.result = s;
    	});


    }
    
    public void runCompile()
    {
    	String javaSource = "package so.much.dynamic;\r\n" + 
    			"\r\n" + 
    			"import org.github.msx80.omicron.api.Game;\r\n" + 
    			"import org.github.msx80.omicron.api.Sys;\r\n" + 
    			"import org.github.msx80.omicron.api.adv.AdvancedSys;\r\n" + 
    			"import org.github.msx80.omicron.api.SysConfig;\r\n" +
    			"import org.github.msx80.omicron.api.SysConfig.VirtualScreenMode;\r\n" + 
    			"\r\n" + 
    			"public class Stringed implements Game {\r\n" + 
    			"    class Prova{\r\n" + 
    			"    	public int n = 30;\r\n" + 
    			"    }\r\n" + 
    			"\r\n" + 
    			"	private AdvancedSys sys;\r\n" + 
    			"	int i = new Prova().n + new Other().n;\r\n" + 
    			"\r\n" + 
    			"	@Override\r\n" + 
    			"	public SysConfig sysConfig() {\r\n" + 
    			"		\r\n" + 
    			"		return new SysConfig(256, 192, VirtualScreenMode.SCALED, \"Child Game Compiled at: "+new Date()+" Click to exit\", \"pippo\");\r\n" + 
    			"	}\r\n" + 
    			"\r\n" + 
    			"	@Override\r\n" + 
    			"	public void init(Sys sys) {\r\n" + 
    			"		this.sys = (AdvancedSys) sys;\r\n" + 
    			"	}\r\n" + 
    			"\r\n" + 
    			"	@Override\r\n" + 
    			"	public boolean update() {\r\n" + 
    			"		if(sys.mouse().btn[0]) sys.quit(\"Closed child\");\r\n" + 
    			"		return false;\r\n" + 
    			"	}\r\n" + 
    			"\r\n" + 
    			"	@Override\r\n" + 
    			"	public void render() {\r\n" + 
    			"		i++;\r\n" + 
    			"		sys.clear((i << 8) | 0xFF );\r\n" + 
    			"	}\r\n" + 
    			"\r\n" + 
    			"	@Override\r\n" + 
    			"	protected void finalize() throws Throwable {\r\n" + 
    			"		System.out.println(\"ChildGame FINALIZED!!! ************\");\r\n" + 
    			"		super.finalize();\r\n" + 
    			"	}\r\n" + 
    			"\r\n" + 
    			"	\r\n" + 
    			"}\r\n" + 
    			"";
    	String other = "package so.much.dynamic;\r\n" + 
    			"\r\n" + 
    			"public class Other{\r\n" + 
    			"	public int n = Stringed.class.hashCode();\r\n" + // just to test that circular reference work.
    			"}";
    	Map<String, String> sources = new HashMap<>();
    	sources.put("so.much.dynamic.Stringed", javaSource);
    	sources.put("so.much.dynamic.Other", other);
    	Game gg = GameLoadingUtils.compileGameOnTheFly("so.much.dynamic.Stringed", sources);
    			
    	((AdvancedSys)sys).execute(gg, s -> {
    		this.result = s;
    	});
    }
    
	public boolean update() {
		
        m = sys.mouse();
        
        w.update(m);
        
        return true;
    }

	@Override
	public SysConfig sysConfig() {
		return new SysConfig(240, 136, VirtualScreenMode.STRETCH_FULL, "Experiments", "experiments");
	}
	
  
}
