package omicron.demo.retrodrawing;

import com.github.msx80.omicron.api.Pointer;

public interface Tool extends BaseTool {
	public void update(Ctx ctx, Pointer m);
	public boolean isBusy();
}
