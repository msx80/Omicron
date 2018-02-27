package black;

import com.github.msx80.omicron.*;

public class BlackGame implements Game {

	private Sys sys;

	public void init(Sys sys) {
		this.sys = sys;
	}

	public void update() {

	}

	public void render() {
		sys.clearScreen(0, 0, 0);
		sys.color(0, (float) (0.9+(Math.random()/10f)), 0, 1);
		sys.write(null, 0, 0, "Cyberdyne computers (c) 1982");
		sys.write(null, 0, 8, "System online 123k free mem");
		sys.write(null, 0, 24, "login:");
	}

	public void close() {
		
	}

}
