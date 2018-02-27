package com.github.msx80.runtime.implementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import de.golfgl.gdx.controllers.mapping.ConfiguredInput;
import de.golfgl.gdx.controllers.mapping.ControllerMappings;

public final class MyControllerMapping extends ControllerMappings {

	public static final int BUTTON_FIRE = 0;
	public static final int BUTTON_JUMP = 1;
	public static final int BUTTON_MENU = 2;
	public static final int BUTTON_QUIT = 3;
	public static final int AXIS_VERTICAL = 4;
	public static final int AXIS_HORIZONTAL = 5;
	
	static final String[] STEP = new String[]{
			"Press Button 1 (fire/confirm)",
			"Press Button 2 (jump/cancel)",
			"Press Button 3 (menu/alt fire)",
			"Press Quit button",
			"Move joystick on vertical axis",
			"Move joystick on horizontal axis",
			"Configured!"
	};
	
	int current = BUTTON_FIRE;
	
	Preferences prefs = Gdx.app.getPreferences("omicron.controllers");
	private String controllerId;
	
	public MyControllerMapping(String controllerId) {
		   super();
		   this.controllerId = controllerId;
		   addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_FIRE));
		    addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_JUMP));
		    addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_MENU));
		    addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_QUIT));
		    addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.axisDigital, AXIS_VERTICAL));
		    addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.axisDigital, AXIS_HORIZONTAL));

		    commitConfig();
		    
		    String json = prefs.getString(controllerId, null);
		    if(json!=null)
		    {
		    	Gdx.app.log("Controllers", "reading controller setup from config: " +json);
		    	JsonValue j = new JsonReader().parse(json);
		    	this.fillFromJson(j);
		    	current = AXIS_HORIZONTAL+1;
		    }
	}

	public String getStep()
	{
		return STEP[current];
	}
	
	public void checkRecord(Controller controller)
	{
		RecordResult res = this.recordMapping(controller, current);
		if(res == RecordResult.recorded)
		{
			Gdx.app.log("Controllers", "Recorded: "+res);
			current++;
			checkFinished();
		}
		else
		{
			if(res != RecordResult.nothing_done)
				Gdx.app.log("Controllers", "Not recorded: "+res);
		}
	}
	
	private void checkFinished() {
		if(isConfigured())
		{
			
			String maps = this.toJson().toJson(OutputType.minimal);
			Gdx.app.log("Controllers", "Save config: "+maps);
			prefs.putString(controllerId, maps);
			prefs.flush();
		}
		
	}

	public boolean isConfigured()
	{
		return current > AXIS_HORIZONTAL;
	}
	

}