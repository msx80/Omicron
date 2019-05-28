package org.github.msx80.omicron;

import org.github.msx80.omicron.api.ScreenConfig;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class ScreenInfo {
	
	public ScreenConfig requiredScreenConfig;
	public int times;
	public int dx, dy;
	
	
	
	public void applyGlClipping()
	{
		Gdx.gl.glScissor(dx/2,dy/2,requiredScreenConfig.width * times,requiredScreenConfig.height * times);
	}
	
	public void handleResize(int winwidth, int winheight, OrthographicCamera cam)
	{
		int nx = winwidth / requiredScreenConfig.width;
		int ny = winheight / requiredScreenConfig.height;
		
		times = Math.min(nx, ny); // number of times the virtual screen fits nicely on the window
		
		dx = winwidth - (requiredScreenConfig.width * times);
		dy = winheight - (requiredScreenConfig.height * times);
		
		cam.setToOrtho(true,
				requiredScreenConfig.width + (float)dx/(float)times,
				requiredScreenConfig.height + (float)dy/(float)times
				);
		
		cam.position.set(requiredScreenConfig.width / 2f, requiredScreenConfig.height / 2f, 0); // center on screen
		cam.update();
	}
	
}
