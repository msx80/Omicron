package com.mygdx.game;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.jemalloc.JEmalloc;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import experimental.LibretroApplication;

@Deprecated
public class TestRegular {

	

	public static void main(String[] args) {
		Configuration.DEBUG.set(true);
		System.out.println(JEmalloc.class); 
		GL.createCapabilities();
		//new Lwjgl3Application(new MyGdxGame(), new Lwjgl3ApplicationConfiguration() );
		
		new LibretroApplication(new MyGdxGame());
		

	}

}
