package com.mygdx.game;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.APIUtil;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

@Deprecated
public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		long addrglGenBuffers = APIUtil.apiGetFunctionAddress(GL.getFunctionProvider(), "glGenBuffers");
		System.out.println(addrglGenBuffers
				);
		batch = new SpriteBatch();
		img = new Texture("C:\\Users\\niclugat\\dev\\libgdx\\backends\\gdx-backend-libretro\\src\\assets\\badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
