package omicron.demo.retrodrawing;

public class Undo implements ClickyTool {

	@Override
	public void execute(Ctx ctx) {
		ctx.undo();
		
	}

}
