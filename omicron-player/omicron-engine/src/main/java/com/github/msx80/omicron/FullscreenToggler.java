package com.github.msx80.omicron;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

public class FullscreenToggler {

	private static int dispw=0;
	private static int disph=0;
	
	public static void toggleFullscreen() {
		Boolean fullScreen = Gdx.graphics.isFullscreen();
		if (fullScreen == true)
		{
			System.out.println(dispw+" x "+disph);
		    Gdx.graphics.setWindowedMode(dispw, disph);
		}
		else
		{
			Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
			// remember last size
			dispw = Gdx.graphics.getWidth();
			disph = Gdx.graphics.getHeight();
			Gdx.graphics.setFullscreenMode(currentMode);
			
		}
	}
}
