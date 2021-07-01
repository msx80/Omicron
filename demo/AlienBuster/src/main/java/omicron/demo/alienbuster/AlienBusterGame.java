package omicron.demo.alienbuster;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.Pointer;
import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.api.SysConfig;
import com.github.msx80.omicron.api.SysConfig.VirtualScreenMode;
import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.text.TextDrawerFixed;

public class AlienBusterGame implements Game {
	
	enum Status {INTRO, GAME, GAMEOVER};
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 136;
	
	private Status status = Status.INTRO;
	
	private Sys sys;
	private int timeToNext = 100;
	private TextDrawerFixed td = null;
	
	int shots;
	int lives;
	int score;
	
	List<Alien> aliens;
	
	Random r;
	
	Pointer m;
	
    public void init(final Sys sys) 
    {
        this.sys = sys;
        r = new Random(sys.millis());
        td = new TextDrawerFixed(sys, 1, 6, 6, 6);
        m = sys.pointers()[0];
    }

    public void render() 
    {
    	// background
    	sys.draw(3, 0,0, 0,0, 240,136, 0, 0);
		switch (status) {
		case INTRO:
			sys.draw(4, 56, 0, 0, 0, 128, 64, 0, 0);
			textWithBorder("TAP TO START", 90,50);
			break;
		case GAME:
			renderGame();
			break;
		case GAMEOVER:
			sys.draw(4, 56, 0, 0, 64, 128, 64, 0, 0);
			textWithBorder("Defeated !!", 90,50);
			break;

		}
    	
    }

	private void textWithBorder(String text, int tx, int ty) {
		sys.color(Colors.BLACK);
		td.print(text, tx-1, ty);
		td.print(text, tx+1, ty);
		td.print(text, tx, ty+1);
		td.print(text, tx, ty-1);
		sys.color(Colors.GREEN);
		td.print(text, tx, ty);
		sys.color(Colors.WHITE);
	}

	private void renderGame() {
		sys.color(Colors.BLACK);
        td.print("Score: "+score, 1, 1);
        td.print("FPS:"+sys.fps(), 100, 130);
        sys.color(Colors.WHITE);
        
        
        
        for (Alien a : aliens) {
			sys.draw(2, (int)a.x-16,(int)a.y-16,0,0,32,32, 0, 0);
		}
        
        // shots left
        for (int i = 0; i < shots; i++) {
        	sys.draw(2, i*16,136-16,32,0,16,16, 0, 0);
		}
        // lives left
        for (int i = 0; i < lives; i++) {
        	sys.draw(2, 224-i*16,136-16,32,16,16,16, 0, 0);
		}
        
        // reload icon
        if (shots == 0)
        {
        	sys.draw(2, 240-32,0,32+16,0,32,32, 0, 0);
        }
        
        // crossair
        sys.draw(2, m.x()-8,m.y()-8,80,0,16,16, 0, 0);
	}


    public boolean loop() {
    	
			
    	switch (status) {
		case INTRO:
			if(m.btn(0))
			{
				initgame();
				status = Status.GAME; 
			}
			break;
		case GAME:
			updateGame(m);
			
			break;
		case GAMEOVER:
			if(m.btnp(0))
			{
				status = Status.INTRO; 
			}
			break;
		}
    	        
    	render();
        return true;
    }

	private void initgame() {
		shots = 5;
		lives = 5;
		score = 0;
		
		aliens = new ArrayList<Alien>();		
	}

	private void updateGame(Pointer m) {
		timeToNext --;
        
        if (timeToNext == 0) {
        	// new alien
        	float vx = r.nextFloat()*4f-2f;
        	float vy = 4f+r.nextFloat()*2f;
        	float x = r.nextInt(120) + (vx<0 ? 120 : 0);
        	aliens.add(new Alien(x,136,vx,-vy));
        	
        	timeToNext = 80+r.nextInt(40);
		}
        
        for (int i = aliens.size()-1; i>=0; i--) {
			Alien a = aliens.get(i);
			if (a.y>150) {
				aliens.remove(i);
				lives--;
				if (lives == 0)
				{
					status = Status.GAMEOVER;
					return;
				}
				
			}
		}
        
        if(m.btnp(0))
        {
        	sys.sound(1,1f,1f);
        	if(shots>0)
        	{
	        	shots--;
	        	checkAlien(m.x(),m.y());
        	}
        	else
        	{
        		if (m.x()>208 && m.y()<32) {
					shots = 5;
				}
        	}
        }
     
        for (Alien a : aliens) {
			a.update();
        }
	}

	private void checkAlien(int mx, int my) {
		
		  for (int i = aliens.size()-1; i>=0; i--) {
				Alien a = aliens.get(i);
				if (Math.abs(my-a.y)<16 && Math.abs(mx-a.x)<16) 
				{
					aliens.remove(i);
					score+=1;
				}
			}
	}

	@Override
	public SysConfig sysConfig() {
		return new SysConfig(WIDTH, HEIGHT, VirtualScreenMode.SCALED,"Alien Buster", "alienbuster");
	}
    
}
