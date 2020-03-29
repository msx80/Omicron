package org.github.msx80;

import org.github.msx80.omicron.api.Mouse;

public interface Tool extends BaseTool {
	public void update(Ctx ctx, Mouse m);
	public boolean isBusy();
}
