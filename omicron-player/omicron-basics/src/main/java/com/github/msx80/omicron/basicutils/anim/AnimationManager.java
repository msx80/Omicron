package com.github.msx80.omicron.basicutils.anim;

import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class AnimationManager {
	private LinkedList<Animation> animations = new LinkedList<Animation>();
	
	
	public void update()
	{
		if ( animations.isEmpty() ) return;
		boolean finished = animations.getFirst().advance();
		if(finished)
		{
			animations.removeFirst();
		}
		
	}
	
	public Animation add(Easing easing, int ttl, Consumer<? super Animation> onEnd,	Consumer<? super Animation> onUpdate)
	{
		return this.add(new Animation(easing, ttl, onEnd, onUpdate));
	}
	
	public Animation add(Easing easing, int ttl, Consumer<? super Animation> onEnd,	int start, int end, IntConsumer onUpdate )
	{
		return this.add(new Animation(easing, ttl, onEnd, a -> {
			double f = ((double)end)*a.position + ((double)start)*(1d -a.position);
			onUpdate.accept(  (int) f ); 
		}));
	}
	public Animation add(Animation a)
	{
		animations.add(a);
		return a;
	}
	public boolean isRunning() {
		
		return !animations.isEmpty();
	}
}
