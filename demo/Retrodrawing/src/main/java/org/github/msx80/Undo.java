package org.github.msx80;

public class Undo implements ClickyTool {

	@Override
	public void execute(Ctx ctx) {
		ctx.undo();
		
	}

}
