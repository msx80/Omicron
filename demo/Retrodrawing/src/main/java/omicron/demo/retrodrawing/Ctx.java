package omicron.demo.retrodrawing;

import com.github.msx80.omicron.api.Sys;

public interface Ctx {
	int currentColor();
	Sys getSys();
	void recordUndo();
	void undo();
	int getSurface();
}
