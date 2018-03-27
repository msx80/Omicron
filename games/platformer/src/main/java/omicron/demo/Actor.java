package omicron.demo;

public class Actor {
	public int x, y;
	private int jumping = 0;

	public Actor(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public void update()
	{
		if(jumping>0) jumping --;
	}
	
	public boolean isJumping()
	{
		return jumping > 0;
	}
	
	public void startJump()
	{
		if(jumping == 0) jumping = 5;
	}

	public void stopJump() {
		jumping = 0;
		
	}
}
