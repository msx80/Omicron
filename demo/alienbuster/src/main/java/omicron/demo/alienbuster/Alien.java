package omicron.demo.alienbuster;

public class Alien extends Entity {
	float vx, vy;
	
	
	public Alien(float x, float y, float vx, float vy) {
		super(x, y);
		this.vx = vx;
		this.vy = vy;
	}

	public void update()
	{
		x += vx;
		
		y += vy;
		
		vy+=0.1;
	}
}
