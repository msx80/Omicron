package com.github.msx80.omicron.plugins.builtin;

import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;

public class StatePlugin implements HardwarePlugin
{
		
	Runnable onResume = null;
	Runnable onPause = null;
	
	@Override
	public void init(HardwareInterface hw)
	{
		
	}

    @Override
	public void onResume()
	{
		if(onResume != null)
		{
			onResume.run();
		}
	}
	
	@Override
	public void onPause()
	{
		if(onPause != null)
		{
			onPause.run();
			
		}
		
	}

	@Override
	public Object exec(String command, Object params) 
	{
		if(command.equals("ON_PAUSE"))
		{
			onPause = (Runnable) params;
			return true;
		}
		else if(command.equals("ON_RESUME"))
		{
			onResume = (Runnable) params;
			return true;
		}

			
		return false;
	}

}
