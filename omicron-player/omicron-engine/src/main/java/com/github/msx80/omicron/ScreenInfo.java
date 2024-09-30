package com.github.msx80.omicron;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.github.msx80.omicron.api.SysConfig;
import com.github.msx80.omicron.api.SysConfig.VirtualScreenMode;

public class ScreenInfo {
	
	public SysConfig requiredSysConfig;
	public int times;
	public int dx, dy;
	private boolean renderToTexture;
	
	
	
	public ScreenInfo(boolean renderToTexture) {
		this.renderToTexture = renderToTexture;
	}

	public void applyGlClipping()
	{
		// if we render to texture, there's no need to do any clipping as the texture will always be the right size.
		if(!renderToTexture)
		{
			if(requiredSysConfig.mode == VirtualScreenMode.SCALED)
			{
				Gdx.gl.glScissor(dx/2,dy/2,requiredSysConfig.width * times,requiredSysConfig.height * times);
			}
			else
			{
				// no scissor
				Gdx.gl.glScissor(0,0,dx, dy);
			}
		}
	}
	
	public void handleResize(int winwidth, int winheight, OrthographicCamera cam)
	{
		if(renderToTexture)
		{
			cam.setToOrtho(true,
					requiredSysConfig.width,
					requiredSysConfig.height
					);
			
			cam.position.set(requiredSysConfig.width / 2f, requiredSysConfig.height / 2f, 0); // center on screen
			cam.update();
		}
		else
		{
			if(requiredSysConfig.mode == VirtualScreenMode.SCALED)
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
			else if (requiredSysConfig.mode == VirtualScreenMode.FILL_SIDE) {
				
				dx = winwidth;
				dy = winheight;
				
				float tx = (float)winwidth/(float)requiredSysConfig.width;
				float ty = (float)winheight/(float)requiredSysConfig.height;
				
				if(tx > ty)
				{
					cam.setToOrtho(true,
							(float)winwidth/ty,
							requiredSysConfig.height
							);
				}
				else
				{
					cam.setToOrtho(true,
							requiredSysConfig.width,
							(float)winheight/tx
							);
					
				}
				cam.position.set(requiredSysConfig.width / 2f, requiredSysConfig.height / 2f, 0); // center on screen
				cam.update();
			}
			else if (requiredSysConfig.mode == VirtualScreenMode.STRETCH_FULL) {
				
				dx = winwidth;
				dy = winheight;
						
				cam.setToOrtho(true,
						requiredSysConfig.width,
						requiredSysConfig.height
						);
				
				cam.position.set(requiredSysConfig.width / 2f, requiredSysConfig.height / 2f, 0); // center on screen
				cam.update();
			}
			else
			{
				throw new RuntimeException("VirtualScreenMode "+requiredSysConfig.mode+" is not implemented yet.");
			}
		}
	}
	
}
