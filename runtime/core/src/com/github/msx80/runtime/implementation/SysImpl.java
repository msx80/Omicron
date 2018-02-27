package com.github.msx80.runtime.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.github.msx80.omicron.Game;
import com.github.msx80.omicron.Image;
import com.github.msx80.omicron.Mouse;
import com.github.msx80.omicron.Sys;
import com.github.msx80.runtime.FpsLimiter;
import com.github.msx80.runtime.OmicronException;
import com.github.msx80.runtime.loader.Cartridge;
import com.github.msx80.runtime.loader.ChildClasspathFileHandle;

public final class SysImpl extends ApplicationAdapter implements Sys{
	
	FPSLogger fps = new FPSLogger();
	SpriteBatch batch;
  OrthographicCamera cam;

	FpsLimiter fpsLimiter;
	
	// all available controllers, should be all joystick + keyboard controller
	List<com.github.msx80.omicron.Controller> ctrls = new ArrayList<com.github.msx80.omicron.Controller>();
	private KeyboardController kc;	
	
	MyControllerMapping controllerMappings;
	
	Image[][] defaultFont;
	
	// is a stack becouse a game (or the system) could possibly launch a sub-Game (like the controller configuration menu). In this case it push a new Game on top of the stack, that will be the one receiving updates and renders.
	Stack<Game> game = new Stack<Game>(); 
	Set<Game> gamesToInit = new HashSet<Game>(); // keep tracks of which Games have yet to be initialized
	
	
	private Cartridge cartridge;
	private MouseImpl mouse;
	
	public SysImpl(Cartridge cartridge) {
		super();
		this.cartridge = cartridge;
		pushGame(cartridge.getGame());
	}

	private void pushGame(Game game) {
		this.game.push( game);
		gamesToInit.add(game);
	}


	@Override
	public void create () {

		initControllers();
		
		defaultFont = loadSpritesheet("defaultFont.png",8,8);
		
		batch = new SpriteBatch();
				
		cam = new OrthographicCamera();//768/2,384/2);
		cam.setToOrtho(true, SCREEN_WIDTH, SCREEN_HEIGHT);
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		
		Gdx.input.setInputProcessor(new MyInputProcessor());
		if(cartridge.needMouse())
		{
			mouse = new MouseImpl();
		}
		
		Cursor cursor = Gdx.graphics.newCursor(new Pixmap(1, 1, Format.RGBA8888),0,0);
		Gdx.graphics.setCursor(cursor);
		
		ensureInited();
		
		fpsLimiter = new FpsLimiter();
	}




	private void initControllers() {
		Runnable q = new Runnable() {
			
			@Override
			public void run() {
				Gdx.app.exit();
				
			}
		};

	
		// keep all controllers by "type" (two controllers with same name = single configuration)
		int num = 1;
		Map<String, MyControllerMapping> configByType = new HashMap<String, MyControllerMapping>();
		for (Controller controller : Controllers.getControllers()) 
		{
		    Gdx.app.log("Controllers", controller.getName());
		    
		    // check if this kind of controller is already configured/being configured
		    MyControllerMapping mappingsForThisJoystic = configByType.get(controller.getName());
		    if(mappingsForThisJoystic==null)
		    {
		    	mappingsForThisJoystic = new MyControllerMapping(controller.getName()); // load configuration from storage
		    	if(!mappingsForThisJoystic.isConfigured()) 
		    	{
		    		// not configured yet! add a dialog to config the controller on the stack
		    		pushGame(new ConfigController( controller, mappingsForThisJoystic));
		    	}
		    	configByType.put(controller.getName(), mappingsForThisJoystic);
		    }
		    
		    ControllerImpl lis = new ControllerImpl("Joy"+(num++), controller,mappingsForThisJoystic,q);
		    ctrls.add(lis);
		    controller.addListener(lis);
		    
		}
		// always add the keyboard controller
		kc = new KeyboardController("Key1", q);
		ctrls.add(kc);

	}

		

	
	@Override
	public void render () {
      fpsLimiter.syncTo(30);
		  fps.log();
		
			game.peek().update(); // update topmost game on the stack
			
			// update controllers
			for (com.github.msx80.omicron.Controller c : ctrls) {
				((Updateable)c).update();
			}
			if(mouse != null)
			{
				mouse.resetButtons();
			}
			
			// render:
			cam.update();
			batch.setTransformMatrix(new Matrix4());
			batch.setProjectionMatrix(cam.combined);
	
			this.color(1, 1, 1, 1);
			
			batch.begin();
			game.peek().render();
			batch.end();
	}
	
	


	@Override
	public void dispose() {
		batch.dispose();
		for (Game g : game) {
			
			g.close();
		}
		// TODO dispose all text and sounds
	}




	@Override
	public Image loadImage(String filename) {
		TextureRegion img = new TextureRegion(new Texture(new ChildClasspathFileHandle(game.peek().getClass(), filename)));
		img.flip(false, true);
		return new ImageImpl(img);
	}

	@Override
	public Image[][] loadSpritesheet(String filename, int sizex, int sizey)
	{
		Texture t = new Texture(new ChildClasspathFileHandle(game.peek().getClass(), filename));
		
		int tx = t.getWidth() / sizex;
		int ty = t.getHeight() / sizey;
		Image[][] res = new Image[tx][];
		for (int i = 0; i < tx; i++) {
			res[i] = new Image[ty];
			for (int j = 0; j < ty; j++) {
				TextureRegion ttr = new TextureRegion(t, i*sizex, j*sizey, sizex, sizey);
				ttr.flip(false, true);
				res[i][j] = new ImageImpl(ttr);
			}
		}
		return res;
	}



	@Override
	public com.github.msx80.omicron.Sound loadSound(String filename) {
		
		//InputStream input = game.getClass().getResourceAsStream("/" + filename.replace('\\', '/'));
		
		if(Gdx.audio == null) throw new OmicronException("No audio system available!");
 		Sound sound = Gdx.audio.newSound(new ChildClasspathFileHandle(game.peek().getClass(), filename));
		return new SoundImpl(sound);
	}




	@Override
	public void clearScreen(float red, float green, float blue) {
		Gdx.gl.glClearColor(red, green, blue, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}




	@Override
	public void draw(Image image, int x, int y) {
		batch.draw(((ImageImpl)image).img, x, y);
		
	}

	@Override
	public void draw(Image image, int x, int y, boolean flipX, boolean flipY) {
		int w = ((ImageImpl)image).img.getRegionWidth();
		int h = ((ImageImpl)image).img.getRegionHeight();
		
		batch.draw(((ImageImpl)image).img, x, y, w/2,h/2,w,h,
				flipX ? -1 : 1, flipY ? -1 : 1, 0);
	}

	@Override
	public void draw(Image image, int x, int y, boolean flipX, boolean flipY, int width, int height) {
		batch.draw(((ImageImpl)image).img, x, y, width/2, height/2,width, height,
				flipX ? -1 : 1, flipY ? -1 : 1, 0);		
	}
	
	@Override
	public void draw(Image image, int x, int y, boolean flipX, boolean flipY, int width, int height, int angle) {
		batch.draw(((ImageImpl)image).img, x, y, width/2, height/2,width, height,
				flipX ? -1 : 1, flipY ? -1 : 1, angle);		
	}
	@Override
	public void color(float red, float green, float blue, float alpha) {
		batch.setColor(red, green, blue, alpha);
	}

	@Override
	public void playSound(com.github.msx80.omicron.Sound sound) {
		((SoundImpl)sound).sound.play(1.0f);
		
	}

	@Override
	public void dispose(Image image)
	{
		((ImageImpl)image).img.getTexture().dispose();
	}
	@Override
	public void dispose(com.github.msx80.omicron.Sound sound)
	{
		((SoundImpl)sound).sound.dispose();
	}


	@Override
	public com.github.msx80.omicron.Controller controller(int index) {

		return ctrls.get(index);
	}




	@Override
	public int availableControllers() {
		return ctrls.size();
	}




	@Override
	public void offset(int x, int y) {
		batch.setTransformMatrix(batch.getTransformMatrix().translate(x, y, 0));
		
	}

	@Override
	public void write(Image[][] font, int x, int y, String text) {
		
		if (font == null) font = defaultFont;
		
		
		for (int i = 0; i < text.length(); i++) {
			int c = text.charAt(i);
			int dx = c % 16;
			int dy = c / 16;
			
			Image cc = font[dx][dy];
			TextureRegion tx = ((ImageImpl)cc).img;
			
			//tx.flip(false, true);
			batch.draw(tx, x+i*tx.getRegionWidth(), y);
			
		}
		//batch.draw(new TextureRegion(f,0,0,64,64), 0,0);
	}

	@Override
	public void exit(Object returnValue) {
		// return value still unused, but could be passed as a callback to the "parent" game.
		Game g = game.pop();
		g.close();
		if(game.isEmpty())
			Gdx.app.exit();
		else
		{
			ensureInited();
		}
		
	}

	private void ensureInited() {
		// ensure that the topmost Game is inited before anything else is called
		Game cu = game.peek();
		if(gamesToInit.contains(cu))
		{
			cu.init(this);
			gamesToInit.remove(this);
		}
	}

	public class MyInputProcessor implements InputProcessor {
		   public boolean keyDown (int keycode) {
			   kc.keyDown(keycode);
		      return true;
		   }

		   public boolean keyUp (int keycode) {
			   kc.keyUp(keycode);
			   return true;
		   }

		   public boolean keyTyped (char character) {
		      return true;
		   }

		   public boolean touchDown (int x, int y, int pointer, int button) {
			   if(mouse != null) mouse.btnDown(x,y,button);
		      return true;
		   }

		   public boolean touchUp (int x, int y, int pointer, int button) {
			   if(mouse != null) mouse.btnUp(x,y,button);
		      return true;
		   }

		   public boolean touchDragged (int x, int y, int pointer) {
			   if(mouse == null) return true;
			   proj.set(x,y,0);
			   cam.unproject(proj);
			   mouse.x = (int) proj.x;
			   mouse.y = (int) proj.y;
		      return true;
		   }

		   Vector3 proj = new Vector3();
		   public boolean mouseMoved (int x, int y) {
			   if(mouse == null) return true; 
			   proj.set(x,y,0);
			   cam.unproject(proj);
			   mouse.x = (int) proj.x;
			   mouse.y = (int) proj.y;
		      return true;
		   }

		   public boolean scrolled (int amount) {
		      return false;
		   }
		}


	@Override
	public Mouse mouse() {
		if(!cartridge.needMouse()) throw new OmicronException("Your cartridge has not requested mouse use.");
		return mouse;
	}

}
