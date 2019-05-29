package org.github.msx80.omicron;

import org.github.msx80.omicron.api.SysConfig;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class ScreenInfo {
	
	public SysConfig requiredSysConfig;
	public int times;
	public int dx, dy;
	
	
	
	public void applyGlClipping()
	{
		Gdx.gl.glScissor(dx/2,dy/2,requiredSysConfig.width * times,requiredSysConfig.height * times);
	}
	
	public void handleResize(int winwidth, int winheight, OrthographicCamera cam)
	{
		int nx = winwidth / requiredSysConfig.width;
		int ny = winheight / requiredSysConfig.height;
		
		times = Math.min(nx, ny); // number of times the virtual screen fits nicely on the window
		
		dx = winwidth - (requiredSysConfig.width * times);
		dy = winheight - (requiredSysConfig.height * times);
		
		cam.setToOrtho(true,
				requiredSysConfig.width + (float)dx/(float)times,
				requiredSysConfig.height + (float)dy/(float)times
				);
		
		cam.position.set(requiredSysConfig.width / 2f, requiredSysConfig.height / 2f, 0); // center on screen
		cam.update();
	}
	
}
