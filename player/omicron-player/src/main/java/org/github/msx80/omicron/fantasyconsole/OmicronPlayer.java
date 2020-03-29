package org.github.msx80.omicron.fantasyconsole;

import java.io.File;
import java.nio.file.Paths;
import java.util.Random;

import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Mouse;
import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.api.SysConfig;
import org.github.msx80.omicron.api.SysConfig.VirtualScreenMode;
import org.github.msx80.omicron.api.adv.AdvancedSys;
import org.github.msx80.omicron.api.adv.AdvancedSys.KeyboardListener;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.TextDrawerFixed;

import javafx.scene.shape.Path;

public class OmicronPlayer implements Game {
	
	private static final int HEIGHT = 162;

	private static final int WIDTH = 288;

	public static Random r = new Random(System.nanoTime());
		
	private Sys sys;
	private TextDrawerFixed font = null;
	private TextDrawerFixed font2 = null;
	Mouse m;
	
	Game running = null;

	private String jarToLaunch = null;
	
	public OmicronPlayer(String[] args) {
		if(args.length == 1)
		{
			this.jarToLaunch  = args[0];
		}
	}


	public void init(final Sys sys) 
    {
        this.sys = sys;
        font = new TextDrawerFixed(sys, 1, 6, 6,6);   
        //font2 = new TextDrawerFixed(sys, 3, 5, 6,5);
        font2 = new TextDrawerFixed(sys, 4, 4, 6,4);
        //font = new TextDrawerFixed(sys, 2, 4, 6,4);
        
  /*      ((AdvancedSys)sys).activateKeyboardInput(new KeyboardListener() {
			
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
     
        */
        if(jarToLaunch!=null)
        {
        	runJar(jarToLaunch);
        }
    }


	
	
    public void render() 
    {
    	sys.clear(Colors.from(20, 80, 50, 255));
    	
    	font.print("OMICRON", 10, 10);
        font.print("Click to start a cartridge!: ", 10, 20);
        font2.print(Paths.get(".").toAbsolutePath().toString() , 10, 30);
        
    }


	public void runJar(String jarToLaunch2)
    {
    	//Game gg =  GameLoadingUtils.loadGameFromJar(new File("C:\\Users\\niclugat\\dev\\LibretroOmicron\\LibretroOmicron\\DoorsOfDoom.omicron"));
    	//Game gg =  GameLoadingUtils.loadGameFromJar(new File("C:\\Users\\niclugat\\dev\\LibretroOmicron\\LibretroOmicron\\doors-of-doom-malvagio.jar"));
		Game gg =  GameLoadingUtils.loadGameFromJar(new File(jarToLaunch2));		
		running = new SubGame(gg);
    	((AdvancedSys)sys).execute(running, s -> {
    		System.out.println("Result: "+s);
    		running = null;
    	});
    }
    
 	public boolean update() {
		
        m = sys.mouse();
        
        if(m.btn[0])
        {
        	runJar("C:\\nicola\\AwesomeGameMain\\AwesomeGameMain.omicron");
        }
        
        return true;
    }

	@Override
	public SysConfig sysConfig() {
		return new SysConfig(WIDTH, HEIGHT, VirtualScreenMode.STRETCH_FULL, "Omicron Computer", "experiments");
	}
	
  
}
