package com.github.msx80.omicron.fantasyconsole;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.api.SysConfig;
import com.github.msx80.omicron.api.SysConfig.VirtualScreenMode;
import com.github.msx80.omicron.api.adv.AdvancedSys;
import com.github.msx80.omicron.api.adv.AdvancedSys.KeyboardListener;
import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.gui.Scroller;
import com.github.msx80.omicron.basicutils.gui.WidgetManager;
import com.github.msx80.omicron.basicutils.gui.Windimation;
import com.github.msx80.omicron.basicutils.gui.drawers.NoScrollbarDrawer;
import com.github.msx80.omicron.basicutils.gui.drawers.ScrollbarDrawer;
import com.github.msx80.omicron.basicutils.gui.drawers.StandardScrollbarDrawer;
import com.github.msx80.omicron.basicutils.palette.Tic80;
import com.github.msx80.omicron.basicutils.text.TextDrawer.Align;
import com.github.msx80.omicron.basicutils.text.TextDrawerFixed;
import com.github.msx80.omicron.fantasyconsole.cartridges.FolderLoader;
import com.github.msx80.omicron.fantasyconsole.cartridges.SourceCartridge;

public class OmicronPlayer implements Game {
	
	private static final int HEIGHT = 162;

	private static final int WIDTH = 288;

	public static Random r = new Random(System.nanoTime());
		
	private Sys sys;
	private TextDrawerFixed font = null;
	private TextDrawerFixed font2 = null;
	
	Cartridge running = null;
	
	// private FileList fileList;
	
	WidgetManager wm = null;
	Scroller s = null;
	Windimation<?> l = null;
	
	public OmicronPlayer() {
		/*
		if(args.length == 1)
		{
			this.jarToLaunch  = args[0];
		}*/
	}


	public void init(final Sys sys) 
    {		
        this.sys = sys;
        font = new TextDrawerFixed(sys, 1, 6, 6,6);   
        font2 = new TextDrawerFixed(sys, 4, 4, 6,4);
        
        ((AdvancedSys)sys).activateKeyboardInput(new KeyboardListener() {
			
			@Override
			public boolean keyUp(int keycode) {
				return false;
			}
			
			@Override
			public boolean keyTyped(char character) {
				return false;
			}
			
			@Override
			public boolean keyDown(int keycode) {
				if (keycode == 131) {
					if (running != null) {
						((AdvancedSys)sys).quit("Killed");
						return true;
					}
				}
				return false;
			}
		});
       
        getIntroWidgets(sys);
        
       
        String[] args = (String[]) sys.hardware("com.github.msx80.omicron.plugins.builtin.ArgsPlugin", "GET", null);
        // if(args!=null)
        {
	        if(args.length==1)
	        {
	        	runJar(args[0]);
	        }
        }
    }


	private void getIntroWidgets(final Sys sys) {
		wm = new WidgetManager(sys);
        l = new Windimation<String>(
        		Arrays.asList("Load a cart", 
        				"Create new cart", 
        				"Options",
        				"Credits",
        				"Quit"), this::selectIntro
        		, sys, font, 120);
        wm.add(l, 80, 50);
	}

	private void selectIntro(int idx, String name)
	{
		System.out.println(name);
		switch (name) {
		case "Quit": ((AdvancedSys)sys).quit("ok");
			break;
		case "Create new cart":
			// ProjectCreatorWindow.main(null);
			activateEditorWidget(sys, font);
			break;
		case "Load a cart":
			getCartSelectWidgets(sys, Paths.get("").toAbsolutePath());
			break;
		case "Credits":
//			try {
//				runCartridge(new SourceCartridge(new FolderLoader(Paths.get("C:\\Users\\niclugat\\dev\\Omicron\\omicron\\demo\\Retrodrawing\\RetroDrawerSrc"))));
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
			break;
		default:
			break;
		}
	}
	private void activateEditorWidget(Sys sys2, TextDrawerFixed font3) {
		/*
		wm = new WidgetManager(sys);
		EditorWidget ew = new EditorWidget(sys2, font3, 300, 500);
   //     l = new ListWithSelection<>(fileList.files, font2, 0, 0, 100);
        
      
        //TestArea ta = new TestArea(sys, 0, 0, 300, 300);
        ScrollbarDrawer sv = new StandardScrollbarDrawer(3, Tic80.RED, Tic80.DARK_RED);
        ScrollbarDrawer sh = new StandardScrollbarDrawer(3, Tic80.RED, Tic80.DARK_RED);
        s = new Scroller(sys, 200+sv.getThickness(), 100, ew, sv, sh);
        wm.add(s, 40, 30);
		*/
	}


	private void selectCartridge(int idx, FileItem p)
	{
		if(p.isDirectory)
		{
			getCartSelectWidgets(sys, p.path);
		}
		else
		{
			System.out.println(p.name);
			runJar(p.path.toAbsolutePath().toString());
		}
	}
	
	private void getCartSelectWidgets(final Sys sys, Path p) {
		System.out.println("Path is: "+p);
		FileList fileList;
		 try {
				fileList = new FileList(p);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
	        
		wm = new WidgetManager(sys);
        l = new Windimation<FileItem>(
        		fileList.files
        		, this::selectCartridge
        		, sys,(t,x,y) -> {
                	FileItem pp = ((FileItem) t);
                	if (pp.isDirectory) {
                		sys.draw(2, x+6, y, 6,0,6,6,0,0);
        			}
                	else
                	{
                		sys.draw(2, x+6, y, 12,0,6,6,0,0);
                	}
                	
                	font.print(pp.name, x+12, y);
                } ,(x,y) -> {
                	sys.draw(2, x, y, 0,0,6,6,0,0);
                }, 7, 200);
   //     l = new ListWithSelection<>(fileList.files, font2, 0, 0, 100);
        
      
        //TestArea ta = new TestArea(sys, 0, 0, 300, 300);
        ScrollbarDrawer sv = new StandardScrollbarDrawer(3, Tic80.RED, Tic80.DARK_RED);
        ScrollbarDrawer sh = new NoScrollbarDrawer(); //new ScrollbarDrawer(8, Tic80.BLUE_GRAY, Tic80.DARK_BLUE);
        s = new Scroller(sys, 200+sv.getThickness(), 100, l, sv, sh);
        wm.add(s, 40, 30);
	}


	
	
    public boolean loop() 
    {
    	
        wm.update();
        
        
        
    	sys.clear(Colors.BLACK) ; //from(20, 80, 50, 255));
    	sys.color(Colors.GREEN);
    	font.print("OMICRON v1.0", WIDTH/2, 10, Align.CENTER);
    	sys.color(Colors.from(100, 100, 100));
    	font2.print("Z:select   X:back   Cursors:move   ALT-Enter:fullscreen", 10, HEIGHT-6);
    	sys.color(Colors.WHITE);

    	font.print("What do you want to do?", WIDTH/2, 20, Align.CENTER);
    	/*font.print("Click to start a cartridge!: ", 10, 20);
        font2.print(Paths.get(".").toAbsolutePath().toString() , 10, 30);
        int y = 40;
        for (Path p : fileList.files) {
        	font.print(p.getFileName().toString() , 10, y);
        	y+=10;
		}
		*/
    	if(sys.controllers()[0].btnp(1))
    	{
    		getIntroWidgets(sys);
    	}
    	
    	wm.draw();
        
    	return true;
    }


	public void runJar(String jarToLaunch2)
    {
    
		Cartridge gg =  CartridgeLoadingUtils.fromOmicronFile(new File(jarToLaunch2), false);		
		runCartridge(gg);
    }
	public void runCartridge(Cartridge gg)
    {
    	//Game gg =  GameLoadingUtils.loadGameFromJar(new File("C:\\Users\\niclugat\\dev\\LibretroOmicron\\LibretroOmicron\\DoorsOfDoom.omicron"));
    	//Game gg =  GameLoadingUtils.loadGameFromJar(new File("C:\\Users\\niclugat\\dev\\LibretroOmicron\\LibretroOmicron\\doors-of-doom-malvagio.jar"));
		
		//Cartridge gg =  CartridgeLoadingUtils.fromOmicronFile(new File(jarToLaunch2));		
		running = gg;
    	((AdvancedSys)sys).execute(gg, s -> {
    		running.close();
    		System.out.println("Result: "+s);
    		
    		running = null;
    	}, e -> {
    		running.close();
    		System.out.println("Exception: "+e);
    		e.printStackTrace();
    		
    		running = null;
    		
    	});
    }
    
	@Override
	public SysConfig sysConfig() {
		return new SysConfig(WIDTH, HEIGHT, VirtualScreenMode.STRETCH_FULL, "Omicron Computer", "experiments");
	}
	
  
}
