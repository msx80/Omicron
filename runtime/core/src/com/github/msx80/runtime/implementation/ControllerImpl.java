package com.github.msx80.runtime.implementation;

import com.github.msx80.omicron.Controller;

import de.golfgl.gdx.controllers.mapping.ControllerMappings;
import de.golfgl.gdx.controllers.mapping.MappedControllerAdapter;

public final class ControllerImpl  extends MappedControllerAdapter implements Controller, Updateable {

	@SuppressWarnings("unused")
	private com.badlogic.gdx.controllers.Controller c;
	
	private boolean up;
	private boolean down;
	private boolean right;
	private boolean left;
	private Controller.Direction dir = Direction.center;
	
	boolean[] pressed = new boolean[4];
	boolean[] released = new boolean[4];
	boolean[] isDown = new boolean[4];
	private Runnable onquit;
	private ControllerMappings mapps;
	private String name;
	
	public ControllerImpl(String name, com.badlogic.gdx.controllers.Controller controller, ControllerMappings mappings, Runnable onquit) {
		super(mappings);
		this.name = name;
		this.mapps = mappings;
		this.c = controller;
		this.onquit = onquit;
		for (int i = 0; i < isDown.length; i++) {
			isDown[i] = false;
		}
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

	public void update()
	{
		for (int i = 0; i < 4; i++) {
			pressed[i] = false;
			released[i] = false;
		}
	}
	@Override
	public boolean wasPressed(int button) {
		
		return pressed[button];
	}

	@Override
	public boolean wasReleased(int button) {

		return released[button];
	}
	
	
	
	
	@Override
	public boolean configuredButtonDown(com.badlogic.gdx.controllers.Controller controller, int buttonId) {
		
		if(buttonId == MyControllerMapping.BUTTON_QUIT)
		{
			if(onquit!=null) onquit.run();
			
			return false;
		}
			
		
		pressed[buttonId] = true;
		isDown[buttonId] = true;
		return false;
	}

	@Override
	public boolean configuredButtonUp(com.badlogic.gdx.controllers.Controller controller, int buttonId) {
		released[buttonId] = true;
		isDown[buttonId] = false;
		return false;
	}

	@Override
	 public boolean configuredAxisMoved(com.badlogic.gdx.controllers.Controller controller, int axisId, float value) {
		boolean ud=(axisId == MyControllerMapping.AXIS_VERTICAL);
		if(ud)
		{
			if(value > 0.5f)
			{
				up=false;
				down=true;
			} else
			if(value < -0.5f)
			{
				up=true;
				down=false;
			}else
			{
				up=false;
				down=false;
			} 

				
		}
		else
		{
			if(value > 0.5f)
			{
				right=true;
				left=false;
			} else
			if(value < -0.5f)
			{
				right=false;
				left=true;
			}else
			{
				right=false;
				left=false;
			} 
		}

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
		return false;
	}

	@Override
	public Direction dir() {

		return dir;
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
