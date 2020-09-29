package org.github.msx80.snake;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.github.msx80.omicron.api.Controller;
import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.api.SysConfig;
import org.github.msx80.omicron.api.SysConfig.VirtualScreenMode;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.MapDrawer;
import org.github.msx80.omicron.basicutils.MapDrawer.MapDataArray;
import org.github.msx80.omicron.basicutils.TextDrawer.Align;
import org.github.msx80.omicron.basicutils.ShapeDrawer;
import org.github.msx80.omicron.basicutils.TextDrawerFixed;


class Point {
	public int x;
	public int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	};
}


public class SnakeMain implements Game 
{
	
	
	private static final int SHEET_FONT = 1; // sheet with font
	private static final int SHEET_MAP = 2;  // sheet with map
	
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	public static final int WIDTH = 240;  // screen dimensions
	public static final int HEIGHT = 136;
	
	public static final int GAME_WIDTH = WIDTH; // actual game area dimension
	public static final int GAME_HEIGHT = HEIGHT - 8;  // save 8 pixel for bottom text

	public static final int TILE_SIZE = 16; // game tiles size
	
	public static final int MAP_WIDTH = GAME_WIDTH / TILE_SIZE; // game size in tiles
	public static final int MAP_HEIGHT = GAME_HEIGHT / TILE_SIZE;
	
	public static final String[] WELCOME = {"Welcome to SNAKE!","Press Z to start!"};
	
	public static final String[] FINISH = {"WOW you finished the game!!","CONGRATS!", "", "You should stop eating now"};
	
	private Sys sys;
	private TextDrawerFixed td = null;
	private MapDataArray map = new MapDataArray(MAP_WIDTH, MAP_HEIGHT);
	private MapDrawer mapDrawer;
	private Controller controller; 
			
	private Random r = new Random(System.currentTimeMillis());
	
	private int score;		// game score
	private int timeToTic; 	// loops to next snake advancement
	private Point head;		// coordinates where the head is
	private Point tail;		// coordinates where the tail is
	private int dir;        // direction of next movement
	private int growing;    // how many unit the snake is growing
	
	private Runnable gameState; // this is the current "phase" of the game, there are 4: welcome (the intro), actual game, death screen and finish screen
	
    public void init(final Sys sys) 
    {
        this.sys = sys;
        td = new TextDrawerFixed(sys, SHEET_FONT, 6, 6, 6);
        mapDrawer = new MapDrawer(sys, TILE_SIZE, TILE_SIZE, 8, map);
        controller = sys.controllers()[0];
        
        gameState = this::welcomeLoop;
        resetGame();
        sys.sound(3, 1, 1);
    }


	public void resetGame() {
		// reset all game variables
		score = 0;
		dir = 1;
		timeToTic = 100;
		growing = 0;
		
		// reset map to empty
		for (int x = 0; x < MAP_WIDTH; x++) {
			for (int y = 0; y < MAP_HEIGHT; y++) {
				map.setTile(x, y, 0);
			}			
		}
		
		// manually set up the snake
        map.setTile(4, 5, 49);
        map.setTile(5, 5, 25);
        map.setTile(6, 5, 25);
        map.setTile(7, 5, 25);
        map.setTile(8, 5, 25);
        map.setTile(9, 5, 9);
        
        head = new Point(9,5);
        tail = new Point(4,5);
	}


	public boolean resetApple() {
		// not the fastest algorithm but it's run only when
		// an apple is taken so..
		List<Point> free = new ArrayList<Point>();
		for (int x = 0; x < MAP_WIDTH; x++) {
			for (int y = 0; y < MAP_HEIGHT; y++) {
				if (map.getTile(x, y) == 0) {
					free.add(new Point(x,y));
				}
			}			
		}
		
		if(free.size()>0)
		{
			Point p = free.get(r.nextInt(free.size()));
			map.setTile(p.x, p.y, 1);
			return true;
		}
		else
		{
			// no more space for apple!! :D
			return false;
		}
		
	}


	private void render() {
		
		ShapeDrawer.rect(sys, 0, GAME_HEIGHT, WIDTH, HEIGHT-GAME_HEIGHT, 0, Colors.from(80, 40, 20)); // bottom box
		sys.fill(0, 0, GAME_HEIGHT, WIDTH, 1, Colors.WHITE);	// bottom white line
        mapDrawer.draw(SHEET_MAP, 0, 0, 0, 0, MAP_WIDTH, MAP_HEIGHT);  // actual map (the game)
               
	}


    public boolean loop() {
    	
    	gameState.run(); // run whichever is the current "loop"
    	
        return true;
    }


    public void gameLoop()
    {
		// if(c.btn(0)) growing=10;
		
		// this is the current direction of the head
		int currentDir = getTileHeading(head);
		// accept any new direction that is not opposite to current
		if (controller.left() && currentDir!=RIGHT) dir = LEFT;
		if (controller.right() && currentDir!=LEFT) dir = RIGHT;
		if (controller.up() && currentDir!=DOWN) dir = UP;
		if (controller.down() && currentDir!=UP) dir = DOWN;
		
		Runnable nextState = null;
    	timeToTic--;
    	if(timeToTic==0)
    	{
    		// a timestep is passed, time to move the snake 
    		timeToTic = 15;
    		nextState = advanceSnake(dir);
    	}
    	if(nextState == null)
    	{
    		// if game is still on, render it and the score
    		render();
    		td.print("Score: "+score, 3, 130);
    	}
    	else
    	{
    		switchState(nextState);
    				
    	}
	}
    public void deathLoop() 
    {
		
		if(controller.btnp(0)) 
		{
			resetGame();
			switchState(this::welcomeLoop);
		}
		else
		{
			render();
			td.print("YOU'RE DEAD! Score: "+score, WIDTH/2, 130, Align.CENTER);
		
		}
	}

    public void welcomeLoop() {
		if(controller.btnp(0)) 
		{
			// set the first apple
			resetApple();
			sys.sound(2, 1, 1);
			switchState(this::gameLoop);
		}
		else
		{
			render();
			for (int i = 0; i < WELCOME.length; i++) {
				
				td.print(WELCOME[i], WIDTH/2, 30+i*8, Align.CENTER);
			}
		}
	}

    public void finishedLoop() {
		if(controller.btnp(0)) 
		{
			resetGame();
			switchState(this::welcomeLoop);
		}
		else
		{
			render();
			for (int i = 0; i < FINISH.length; i++) {
				
				td.print(FINISH[i], WIDTH/2, 30+i*8, Align.CENTER);
			}
			td.print("Score: "+score, WIDTH/2, 130, Align.CENTER);
		}
	}
    
    private void switchState(Runnable newState)
    {
    	this.gameState = newState;
    	this.gameState.run(); // give a chance to draw the screen
    }

	private Runnable advanceSnake(int direction) {
		int oldDir = getTileHeading(head);
		int oldX = head.x;
		int oldY = head.y;
		
		advancePoint(head, direction);
		boolean finished = false;
		int hitWhat = map.getTile(head.x, head.y);
		if (hitWhat == 1) {
			// apple!
			sys.sound(1, 1, 1);
			score += 10;
			growing += 2;
			finished = !resetApple();
			
		}
		else if (hitWhat != 0) {
			sys.sound(4, 1, 1);
			return this::deathLoop;
		}
		// place new head tile
		map.setTile(head.x, head.y, getHeadTile(direction));
		
		// place new body segment in place of the old head,
		// connecting oldDir to new direction
		map.setTile(oldX, oldY, getSnakeTile(direction, oldDir));
		
		
		if( growing > 0 )
		{
			// still growing from last apple
			growing --;
		}
		else
		{
			// find tail direction and position
			oldDir = getTileHeading(tail);
			oldX = tail.x;
			oldY = tail.y;
			// advance tail by its direction
			advancePoint(tail, oldDir);
			
			// the new direction of the tail is whatever direction
			// the last body segment before the tail is
			int newTailDir = getTileHeading(tail);
			// overwrite last body segment with tail
			map.setTile(tail.x, tail.y, getTailTile(newTailDir));
			// clean old tail with blank
			map.setTile(oldX, oldY, 0);
		}
		
		if (finished) {
			
			return this::finishedLoop;
			
		}
		return null;
	}

	// these are based on how the tiles are laid out on the spritesheet
	private int getTailTile(int direction) {
		return 48+direction;
	}

	private int getSnakeTile(int direction, int oldHeadDir) {
		
		return 16+direction+(oldHeadDir*8);
	}

	private int getHeadTile(int direction) {
		return 8+direction;
	}
	
    private int getTileHeading(Point p) {
		int tileNum = map.getTile(p.x, p.y);
		return tileNum%8;
	}


	private void advancePoint(Point point, int direction) 
	{
		switch (direction) {
			case UP: point.y--; break;
			case DOWN: point.y++; break;
			case LEFT: point.x--; break;
			case RIGHT: point.x++; break;
	
			default:
				throw new RuntimeException("Unexpected direction?");
		}
		// make sure it's still in the map by wrapping
		if(point.y<0) point.y += map.getHeight();
		else if (point.y >= map.getHeight()) point.y -= map.getHeight();
		if(point.x<0) point.x += map.getWidth();
		else if (point.x >= map.getWidth()) point.x -= map.getWidth();
		
	}

	@Override
	public SysConfig sysConfig() {
		return new SysConfig(WIDTH, HEIGHT, VirtualScreenMode.SCALED,"Snake", "snake");
	}
    
}
