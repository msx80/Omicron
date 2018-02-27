package com.github.msx80.runtime.implementation;

import com.badlogic.gdx.Input;
import com.github.msx80.omicron.Controller;
public final class KeyboardController implements Controller, Updateable {

	private boolean up;
	private boolean down;
	private boolean right;
	private boolean left;
	private Controller.Direction dir = Direction.center;
	boolean[] pressed = new boolean[4];
	boolean[] isDown = new boolean[4];
	boolean[] released = new boolean[4];
	private Runnable onquit;
	private String name;

	
	
	
	public KeyboardController(String name, Runnable onquit) {
		super();
		this.name = name;
		this.onquit = onquit;
		for (int i = 0; i < isDown.length; i++) {
			isDown[i] = false;
			
		}
	}

	private void updDir()
	{
		if(left)
		{
			if(up)
				dir = Direction.northWest;
			else if (down)
				dir = Direction.southWest;
			else
				dir = Direction.west;
		}
		else if (right)
		{
			if(up)
				dir = Direction.northEast;
			else if (down)
				dir = Direction.southEast;
			else
				dir = Direction.east;
			
		}
		else
		{
			if(up)
				dir = Direction.north;
			else if (down)
				dir = Direction.south;
			else
				dir = Direction.center;

		}
	}
	
	@Override
	public Direction dir() {
		return dir;
	}


	@Override
	public boolean up() {
		
		return up;
	}

	@Override
	public boolean down() {
		
		return down;
	}

	@Override
	public boolean left() {
		return left;
	}

	@Override
	public boolean right() {
		
		return right;
	}


	@Override
	public boolean wasPressed(int btn) {
		return pressed[btn];
	}

	@Override
	public boolean wasReleased(int btn) {
		return released[btn];
	}
	
	private int btnIdx(int keycode)
	{
		switch (keycode) {
		case Input.Keys.Z: return 0;
		case Input.Keys.X: return 1;
		case Input.Keys.C: return 1;
		case Input.Keys.ESCAPE: return 2;

		default:
			return -1;
		}
	}
	
	public void update()
	{
		for (int i = 0; i < 4; i++) {
			pressed[i] = false;
			released[i] = false;
		}
		
	}

	public void keyDown(int keycode) {
		if(keycode == Input.Keys.UP)
		{
			up = true;
			updDir();
		} else if(keycode == Input.Keys.DOWN)
		{
			down = true;
			updDir();
		}
		if(keycode == Input.Keys.LEFT)
		{
			left = true;
			updDir();
		} else if(keycode == Input.Keys.RIGHT)
		{
			right = true;
			updDir();
		}
		
		int idx = btnIdx(keycode);
		if(idx >=0 )
		{
			isDown[idx] = true;
			pressed[idx] = true;
		}
		
	}

	public void keyUp(int keycode) {
		if(keycode == Input.Keys.ESCAPE)
		{
			onquit.run();
			return;
		}
		
		if(keycode == Input.Keys.UP)
		{
			up = false;
			updDir();
		} else if(keycode == Input.Keys.DOWN)
		{
			down = false;
			updDir();
		}
		if(keycode == Input.Keys.LEFT)
		{
			left = false;
			updDir();
		} else if(keycode == Input.Keys.RIGHT)
		{
			right = false;
			updDir();
		}
		
		int idx = btnIdx(keycode);
		if(idx >=0 )
		{
			isDown[idx] = false;
			
			released[idx] = true;
		}
		
		
	}

	@Override
	public boolean isPressed(int button)
	{
		return isDown[button];
				
	}

	@Override
	public String name() {
		return name;
	}
}
