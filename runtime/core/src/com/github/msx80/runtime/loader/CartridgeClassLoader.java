package com.github.msx80.runtime.loader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import com.github.msx80.omicron.Game;


public class CartridgeClassLoader extends URLClassLoader {
	
	
	
    public CartridgeClassLoader(File file) throws Exception  {
        super(new URL[] {file.toURI().toURL()});
        
    }
    
    public Game getGame(String entryClass) throws Exception
    {
    	Class<?> classToLoad = Class.forName(entryClass, true, this);
		
		Game instance = (Game)classToLoad.newInstance();
		return instance;
    }
    
   
}