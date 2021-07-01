package com.github.msx80.omicron.basicutils.anim;

import java.util.function.Consumer;

public class Animation 
{
	
	Easing easing;
	
	int ttl; // in frames
	int frame; // current frame
	public double position; // position [0,1] , post easing
	public double percentage; // percentage of completition [0,1], pre easing 
	
	Consumer<? super Animation> onEnd;
	Consumer<? super Animation> onUpdate; // called each frame

	public Animation(Easing easing, int ttl, Consumer<? super Animation> onEnd, Consumer<? super Animation> onUpdate) {
		this.easing = easing;
		this.ttl = ttl;
		this.frame = 0;
		this.position = 0f;
		this.percentage = 0f;
		this.onEnd = onEnd;
		this.onUpdate = onUpdate;
	}


	public boolean finished()
	{
		return frame >= ttl;
	}
	
	
	public boolean advance()
	{
		if (onUpdate!=null) onUpdate.accept(this);
		
		this.frame++;
		percentage = (double)frame / (double)ttl;
		position = easing.fun.apply(percentage);
		
		// update stuff
		
		if (finished())
		{
			if(onEnd!=null) onEnd.accept(this);
			return true;
		}
		else
		{
			return false;
		}
	}
}
