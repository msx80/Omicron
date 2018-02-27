package com.github.msx80.omicron.runtime;

import java.io.File;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jogamp.JoglNewtApplication;
import com.badlogic.gdx.backends.jogamp.JoglNewtApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.msx80.runtime.implementation.SysImpl;
import com.github.msx80.runtime.loader.Cartridge;
import com.github.msx80.runtime.loader.CartridgeLoader;
import com.github.msx80.runtime.loader.SandboxSecurityPolicy;

public class CartridgeLauncher {
	
	
	 @Option(name="-fs",usage="run in fullscreen")
   private boolean fullscreen = false;
	
   @Argument
   private List<String> arguments = new ArrayList<String>();
	
	
	public static void main (String[] arg) {
		Policy.setPolicy(new SandboxSecurityPolicy());
		System.setSecurityManager(new SecurityManager()); 

		new CartridgeLauncher(arg);
		
	}
	public CartridgeLauncher(String[] arg)
	{
		 CmdLineParser parser = new CmdLineParser(this);
		 
		 try {
	            // parse the arguments.
	            parser.parseArgument(arg);

	            // you can parse additional arguments if you want.
	            // parser.parseArgument("more","args");

	            // after parsing arguments, you should check
	            // if enough arguments are given.
	            if( arguments.isEmpty() )
	                throw new CmdLineException(parser,"No cartridge file provided.");
	            if( arguments.size() >1 )
	                throw new CmdLineException(parser,"Please provide a single cartridge file.");

	        } catch( CmdLineException e ) {
	            // if there's a problem in the command line,
	            // you'll get this exception. this will report
	            // an error message.
	            System.err.println(e.getMessage());
	            System.err.println("java -jar omicron.jar [options...] cartridgefile");
	            // print the list of available options
	            parser.printUsage(System.err);
	            System.err.println();

	            return;
	        }
		 
		
		Cartridge c;
		try {
			
			File jar = new File(arguments.get(0));
			System.out.println("Loading cartridge "+jar);
			c = CartridgeLoader.load(jar);
		} catch (Exception e) {
		
			e.printStackTrace();
			System.exit(42);
			return;
		}
		
		
		String arch = System.getProperty("os.arch");
		System.out.println("Architecture: "+arch);
		if(arch.toLowerCase().startsWith("arm"))
		{
			System.out.println("Starting ARM version with JoglNewt.");
		    JoglNewtApplicationConfiguration config = new JoglNewtApplicationConfiguration();

		    config.width=384 ; config.height=192;
		    
			config.foregroundFPS = 30;
			config.backgroundFPS = 30;
			config.fullscreen = true;

			config.title = c.getProperties().getProperty("name")+" by "+c.getProperties().getProperty("author")+" ("+c.getProperties().getProperty("year")+") - Omicron";
			
		    new JoglNewtApplication(new SysImpl(c), config);

		}
		else
		{
			System.out.println("Starting x86/64 version with Lwjgl.");
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.title = c.getProperties().getProperty("name")+" by "+c.getProperties().getProperty("author")+" ("+c.getProperties().getProperty("year")+") - Omicron";
			config.width=1366 ; //320*2;
			config.height=768;//192*2;
			config.fullscreen=true;
			
			//config.width=640;config.height=480;
			if(!fullscreen)
			{
				config.width=384*2 ; config.height=192*2;	config.fullscreen=false;
			}
			
			new LwjglApplication(new SysImpl(c), config);
		}
	}

	
}
