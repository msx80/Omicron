package org.github.msx80.omicron.basicutils;

import org.github.msx80.omicron.api.Mouse;

public class MomentaryMouse {

	private static final Mouse m = new Mouse();
	private static final boolean[] oldbtn = new boolean[] {false, false, false};
	
	public static final Mouse get(Mouse inp)
	{
		m.x = inp.x;
		m.y = inp.y;
		for (int i = 0; i < oldbtn.length; i++) {
			m.btn[i] = (!oldbtn[i]) && inp.btn[i];
			oldbtn[i] = inp.btn[i];
		}
		return m;
	}
}
