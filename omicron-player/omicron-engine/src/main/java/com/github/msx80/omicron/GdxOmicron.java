package com.github.msx80.omicron;


//import java.security.AccessController;
//import java.security.PrivilegedAction;
import java.util.Stack;
import java.util.function.Consumer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NonBleedingSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.msx80.omicron.api.Controller;
import com.github.msx80.omicron.api.Pointer;
import com.github.msx80.omicron.api.adv.AdvancedSys;
import com.github.msx80.omicron.api.adv.Cartridge;


public final class GdxOmicron extends ApplicationAdapter implements AdvancedSys {
	
	private static final int MAX_SUPPORTED_TOUCHES = 3;
	FPSLogger fps = new FPSLogger();
	NonBleedingSpriteBatch batch;
	OrthographicCamera cam=new OrthographicCamera();
	
	// a stack of all currently running Game s
	Stack<GameRun> gameStack = new Stack<GameRun>();
	
	public GameRun current; // top of the stack
	
	Pointer[] pointers;
	Controller[] controllers;
	
	
	private int ox = 0;
	private int oy = 0;
	
	Texture pixel;
	private int lastPixel;
		
	Rectangle scissors = new Rectangle();
	Rectangle clipBounds = new Rectangle();
	
	Music currentMusic = null;
	private HardwareInterface hw;
	private GdxOmicronOptions options;
	
	private Runnable afterLoop;
	
	private int[] currentScreenCache = null; // cache for getPix, cleared any time a modification is made.
	
	public GdxOmicron(Cartridge cartridge, HardwareInterface hw, GdxOmicronOptions options) {
		super();
		this.hw = hw;
		this.options = options;
		GameRun gr = new GameRun(cartridge, new ScreenInfo(options.getRenderingToTexture()), null, null);
		this.gameStack.push(gr);
		current = gr;
	}
	
	public GdxOmicron(Cartridge cartridge, HardwareInterface hw) {
		this(cartridge, hw, new GdxOmicronOptions());
	}
	
	Vector3 proj = new Vector3();
	private Preferences prefs = null;
	private KeyboardListener keyboardListener = null;

	@Override
	public void create () {
		System.out.println("calling cr");
		
		contextReset();
		//batch.setBlendFunctionSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA,GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA);
		//batch.enableBlending();
		// batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_DST_ALPHA);	
	
		controllers = new Controller[] {new ControllerImpl()}; // first is keyboard, TODO use joypad etc.
		
		pointers = new Pointer[Math.max(1, Math.min(Gdx.input.getMaxPointers(),MAX_SUPPORTED_TOUCHES))];
		for (int i = 0; i < pointers.length; i++) {
			pointers[i] = new MouseImpl();
			
		}
		Gdx.input.setInputProcessor(new MyInputProcessor());
		
		//Cursor cursor = Gdx.graphics.newCursor(new Pixmap(1, 1, Format.RGBA8888),0,0);
		//Gdx.graphics.setCursor(cursor);
		System.out.println("Init or resuming game");
		
		
		initOrResumeGameRun(current);
		
		System.out.println("Done!");
		
	}


	private void initOrResumeGameRun(GameRun r) {
		boolean isResume = r.screenInfo.requiredSysConfig != null;
		if(!isResume) r.screenInfo.requiredSysConfig = r.game.sysConfig();

		
		// for some reason, sounds are not played the first time on android, possibly becouse of asyncronous loading
		// and sounds not ready yet. As a super sketchy patch, we preload some sounds here (work sequentially, if there's an hole, whatever)
		if(Gdx.app.getType() == ApplicationType.Android) {
			for (int i = 1; true; i++) {
				Sound x = r.getSound(i);
				if(x==null)break;
			}
		}
		if(!isResume) 
		{
			for (HardwarePlugin hwp : r.plugins.values()) {
				hwp.init(this, hw);
			}
			r.game.init(this); // TODO handle throws!
		}
		if(r.screenInfo.requiredSysConfig.title!=null) Gdx.graphics.setTitle(r.screenInfo.requiredSysConfig.title);
		setUpCam(r, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
	}

	private void setUpCam(GameRun r, int winwidth, int winheight) 
	{
		System.out.println("Resize to "+winwidth+" "+winheight);
		if(options.getRenderingToTexture())
		{
			//System.out.println("Ignoring, rendering to texture");
			// even if the window resized, the texture we are rendering to is unchanged.
			r.screenInfo.handleResize(r.screenInfo.requiredSysConfig.width, r.screenInfo.requiredSysConfig.height, cam);
		}
		else
		{
			r.screenInfo.handleResize(winwidth, winheight, cam);
		}
		batch.setProjectionMatrix(cam.combined);
	}

	
	
	@Override
	public void resize(int width, int height) {
		
		setUpCam(current,width, height);
		
	}

	public void colorf(float red, float green, float blue, float alpha) {
		batch.setColor(red, green, blue, alpha);
	}

	@Override
	public void clip(int x, int y, int w, int h)
	{
		batch.flush(); // important, otherwise things stay in the buffer and get clipped at the wrong time
		if(w == 0 && h == 0 && x==0 && y ==0)
		{
			clipBounds.set(0,0,current.screenInfo.requiredSysConfig.width, current.screenInfo.requiredSysConfig.height);
		}
		else
		{
			clipBounds.set(x, y ,w,h);
		}
		
		ScissorStack.calculateScissors(cam, batch.getTransformMatrix(), clipBounds, scissors);
		if (!ScissorStack.setScissors(scissors)) {
		    throw new RuntimeException("No scissors, check your clip() dimensions");
		}
	}
	
	
	@Override
	public void render () {
		
		fps.log();
		offset(0,0); // reset offset

		// reset current color to white
		this.colorf(1, 1, 1, 1);

		// clear the screen to black, including portions outside the scissor area
		if(!options.getRenderingToTexture())
		{
			Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
			this.clear(ColorsCopy.BLACK);
			Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		}	
		current.screenInfo.applyGlClipping();
		batch.setProjectionMatrix(cam.combined);
		
		Throwable t = null;
		// batch.begin(); // TODO make it begin lazily at first draw so well behaved games will do the updates before
		boolean continuous = true;
		try
		{
			try {
				continuous = current.game.loop();
			} catch (Throwable e) {
				e.printStackTrace(System.out);
				t = e;
			}
		}
		finally
		{
			if(batch.isDrawing()) batch.end();
		}
		for (Controller controller : controllers) {
			ControllerImpl c = (ControllerImpl) controller;
			c.copyOld();
		}
		for (Pointer p : pointers) {
			MouseImpl mouse = (MouseImpl) p;
			mouse.copyOld();
			mouse.resetScroll();
		}
		if(t != null)
		{
			// game has launched an exception
			continuous = true; // reset to continuous rendering
			afterLoop = null; // remove any quit or executed requested
			handleQuitOrException(null, t);
		}
		if(afterLoop != null)
		{
			continuous = true; // reset to continuous rendering
			Runnable r = afterLoop;
			afterLoop = null;
			r.run(); // TODO problem: if this throws, (ie loading a subcartridge) we have no way of telling it to the currently running cart
		}

	}



	
	
	@Override
	public void dispose () {
		batch.dispose();
		while(!gameStack.isEmpty())
		{
			gameStack.pop().dispose();
		}
	}

	@Override
	public void clear(int value) {
		
		// todo cache the last clear() color to avoid recalculating all every time
		// it's usually jsut the same color every type.
		float r = ((value & 0xff000000) >>> 24) / 255f;
		float g = ((value & 0x00ff0000) >>> 16) / 255f;
		float b = ((value & 0x0000ff00) >>> 8) / 255f;

		Gdx.gl.glClearColor(r, g, b, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}


	private void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h)
	{
		// uploadDirtyTexture();
		TextureRegion r = current.getSheet(sheetNum);
		if(r == null) throw new RuntimeException("Trying to draw sheet "+sheetNum+" but was not found");
		r.setRegion(srcx, srcy, w, h);
		r.flip(false, true);
		if(!batch.isDrawing()) batch.begin();
		batch.draw(r, x+ox, y+oy);
		// batch.draw(r.getTexture(), x,y,srcx,srcy,w,h);
	}
	
	@Override
	public void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h, int rotate, int flip)
	{
		screenChanged();
		current.uploadDirtyTexture();
		if(rotate == 0 && flip == 0)
		{
			draw(sheetNum, x, y, srcx, srcy, w, h);
		}
		else
		{
			/* 0 = No Flip
					1 = Flip horizontally
					2 = Flip vertically
					3 = Flip both vertically and horizontally
				*/
			boolean flipx = flip == 1 || flip == 3; 
			boolean flipy = flip >= 2;
			
			int angle = rotate * 90;
			
			TextureRegion r = current.getSheet(sheetNum);
			if(r == null) throw new RuntimeException("No sheet "+sheetNum+" found.");
			//batch.draw(r.getTexture(), x+ox, y+oy,w/2f,h/2f,w-0.001f,h-0.001f,1,1,angle, srcx, srcy, w, h, false != flipx, true != flipy);

			if(!batch.isDrawing()) batch.begin();
			batch.draw(r.getTexture(), x+ox, y+oy,w/2f,h/2f,w,h,1,1,angle, srcx, srcy, w, h, false != flipx, true != flipy);
		}
	}


	@Override
	public int getPix(int sheetNum, int x, int y) {
		if(sheetNum == 0)
		{
			boolean is = batch.isDrawing(); 
			if(is)
			{
				batch.end();
			}
			int w = current.screenInfo.requiredSysConfig.width;
			int h = current.screenInfo.requiredSysConfig.height;
			
			if(currentScreenCache == null)
			{
				// for performances reason, we just download all of the screen fron the graphic memory and store it in an array of bytes, currentScreenCache
				// if the screen is updated by any call, the currentScreenCache is cleared so next call to getPix will recalculate it (correctly).
				// this improves A LOT the common use case of user wanting to pick a good portion of the screen into a buffer (repetedly calling getPix)
				


				
				proj.set(w,0,0);
				cam.project(proj);
				double ww = proj.x;
				double hh = proj.y;
				
				// ratio between physical and virtual screen size.
				double ratiox = (ww/(double)w);
				double ratioy = (hh/(double)h);
				
				// b is in real display coordinates (like bigger than omicron width/height).
				byte[] b = ScreenUtils.getFrameBufferPixels(0,0, (int) Math.round(ww), (int) Math.round(hh), false);
				
				// this is in virtual width/height
				currentScreenCache = new int[w*h];
				
				// map physical coordinates to virtual. Y is inverted. Also group color bytes into a single color int.
				for (int ay = 0; ay < h; ay++) {
					for (int ax = 0; ax < w; ax++) {
						double g = ((ay*ratioy*ww) + (ax*ratiox)) * 4;
						int idx = (int) (g);
						int c = ColorsCopy.from(0xFF & b[idx], 0xFF & b[idx+1], 0xFF &  b[idx+2], 0xFF & b[idx+3]);
						currentScreenCache[(h-ay-1)*w+ax] = c;
					}
				}
				
			}
			return currentScreenCache[y*w+x];
		}
		else
		{
			// current.uploadDirtyTexture(); // ensure surfaced are updated
			TextureRegion r = current.getSheet(sheetNum);
			if(r==null) throw new RuntimeException("Sheet "+sheetNum+" not found!");
			PixmapTextureData d = (PixmapTextureData) r.getTexture().getTextureData();
			
			return d.consumePixmap().getPixel(x, y);
		}
	}

	@Override
	public int newSurface(int w, int h) {
		Pixmap p = new Pixmap(w, h, Format.RGBA8888);
		p.setBlending(Blending.None);
		
		int i = -1;
		while(current.sheets.containsKey(i))
		{
			i--;
		}
		
		Texture tt = new Texture(p, false);
		
		TextureRegion img = new TextureRegion(tt);
		img.flip(false, true);
		
		current.sheets.put(i, img);
		
		return i;
	}

	@Override
	public void fill(int sheetNum, int x, int y, int w, int h, int color) {
		if(sheetNum==0)
		{
			screenChanged();
			// drawing on screen
			if(lastPixel != color) // caching our pixel texture
			{
				if(batch.isDrawing())batch.end();
				
				( (PixmapTextureData) pixel.getTextureData() ).consumePixmap().drawPixel(0, 0, color);
				lastPixel = color;
				pixel.load(pixel.getTextureData());
			}
			if(!batch.isDrawing()) batch.begin();
			batch.draw(pixel, x+ox, y+oy, w, h);
		}
		else
		{
			TextureRegion r = current.getSheet(sheetNum);
			PixmapTextureData d = (PixmapTextureData) r.getTexture().getTextureData();
			Pixmap pp = d.consumePixmap();
			pp.setColor(color);
			
			pp.fillRectangle(x, y, w, h); // this works becouse we set blending to none
			
			current.addDirtyTexture(sheetNum);
		}
	}


	private void screenChanged() {
		currentScreenCache = null;
	}

	@Override
	public void offset(int x, int y) {
		ox += x;
		oy  += y;
		
	}
	
	
	public class MyInputProcessor implements InputProcessor {
		   public boolean keyDown (int keycode) {
			   if( Gdx.input.isKeyPressed(Input.Keys.ENTER) && Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT))
			   {
				   FullscreenToggler.toggleFullscreen();
				   return true;
			   }
			   
			   
			   if(keyboardListener!=null) 
			   {
				   if(keyboardListener.keyDown(keycode)) return true;
			   }
			   
			   
			   ControllerImpl c = (ControllerImpl)controllers[0];
			   switch (keycode) {
					case Input.Keys.UP: c.up=true; break;
					case Input.Keys.DOWN: c.down=true; break;
					case Input.Keys.LEFT: c.left=true; break;
					case Input.Keys.RIGHT: c.right=true; break;
		
					case Input.Keys.Z: c.btn[0]=true; break;
					case Input.Keys.X: c.btn[1]=true; break;
					case Input.Keys.A: c.btn[2]=true; break;
					case Input.Keys.S: c.btn[3]=true; break;
					
			default:
				break;
			}
		      return true;
		   }

		   public boolean keyUp (int keycode) {
			   if(keyboardListener!=null) 
			   {
				   if(keyboardListener.keyUp(keycode)) return true;
			   }
			   ControllerImpl c = (ControllerImpl) controllers[0];
			   switch (keycode) {
					case Input.Keys.UP: c.up=false; break;
					case Input.Keys.DOWN: c.down=false; break;
					case Input.Keys.LEFT: c.left=false; break;
					case Input.Keys.RIGHT: c.right=false; break;
		
					case Input.Keys.Z: c.btn[0]=false; break;
					case Input.Keys.X: c.btn[1]=false; break;
					case Input.Keys.A: c.btn[2]=false; break;
					case Input.Keys.S: c.btn[3]=false; break;
			
			default:
				break;
			}
			   return true;
		   }

		   public boolean keyTyped (char character) {
			   if(keyboardListener!=null) 
			   {
				   if(keyboardListener.keyTyped(character)) return true;
			   }
		      return true;
		   }

		   public boolean touchDown (int x, int y, int pointer, int button) {
			   if(pointers == null) return true;
			   if(pointer>=pointers.length) return true;
			   
			   proj.set(x,y,0);
			   cam.unproject(proj);
			   MouseImpl mouse = (MouseImpl) pointers[pointer];
			   mouse.set((int) proj.x, (int) proj.y);
			  mouse.btn[button] = true;
		      return true;
		   }

		   public boolean touchUp (int x, int y, int pointer, int button) {
			   if(pointers == null) return true;
			   if(pointer>=pointers.length) return true;
			   
			   proj.set(x,y,0);
			   cam.unproject(proj);
			   MouseImpl mouse = (MouseImpl) pointers[pointer];
			   mouse.set((int) proj.x, (int) proj.y);
			   //mouse.num = pointer;
			   mouse.btn[button] = false;
		      return true;
		   }

		   public boolean touchDragged (int x, int y, int pointer) {
			   if(pointers == null) return true;
			   if(pointer>=pointers.length) return true;
			   
			   proj.set(x,y,0);
			   cam.unproject(proj);
			   MouseImpl mouse = (MouseImpl) pointers[pointer];
			   mouse.set((int) proj.x, (int) proj.y);
			   //mouse.num = pointer;
		      return true;
		   }

		   
		   public boolean mouseMoved (int x, int y) {
			   if(pointers == null) return true; 
			   proj.set(x,y,0);
			   cam.unproject(proj);
			   MouseImpl mouse = (MouseImpl) pointers[0];
			   mouse.set((int) proj.x, (int) proj.y);
			   //mouse.num = -1;
		      return true;
		   }

			public boolean scrolled(float amountX, float amountY) {
			   if(pointers == null) return true;
			   
			   if(amountY == -1)
			   {
				   ((MouseImpl) pointers[0]).btn[3] = true;
			   }
			   else
			   {
				   ((MouseImpl) pointers[0]).btn[4] = true;
			   }
			   
			   
		      return false;
			}
		   
		   public boolean scrolled (int amount) {
			   if(pointers == null) return true;
			   
			   if(amount == -1)
			   {
				   ((MouseImpl) pointers[0]).btn[3] = true;
			   }
			   else
			   {
				   ((MouseImpl) pointers[0]).btn[4] = true;
			   }
			   
			   
		      return false;
		   }

		
		}


	@Override
	public int fps() {
		return Gdx.graphics.getFramesPerSecond();
	}

	
	static Color tmpColor = new Color(0);
	@Override
	public void color(int color) {
		Color.rgba8888ToColor(tmpColor, color);
		batch.setColor(tmpColor);
	}

	@Override
	public Pointer[] pointers() {
		return pointers;
	}

	@Override
	public Controller[] controllers() {
		return controllers;
	}

	@Override
	public void sound(int soundNum, float volume, float pitch) {
		Sound r = current.getSound(soundNum);
		if(r == null) throw new RuntimeException("Trying to play sound "+soundNum+" but was not found");
		r.play(volume, pitch, 0);
		
	}

	@Override
	public String mem(final String key) {
		
		// Not needed since we switched to WiseLoader
		//return AccessController.doPrivileged( new PrivilegedAction<String>() {

		//	@Override
		//	public String run() {
				return getPrefs().getString(key);
		//	}
		//} );
		
	}

	private Preferences getPrefs() {
		if(prefs == null)
		{
			prefs = Gdx.app.getPreferences(current.screenInfo.requiredSysConfig.code);
		}
		return prefs ;
	}

	@Override
	public void mem(final String key, final String value) {
		// Not needed since we switched to WiseLoader
//		AccessController.doPrivileged( new PrivilegedAction<Void>() {
//			@Override
//			public Void run() {
				getPrefs().putString(key, value).flush();
//				return null;
//			}
//		} );
	}

	@Override
	public Object hardware(String module, String command, Object param) {
		Gdx.app.log("GdxOmicron", "Plugin called "+module+" "+command+" "+param);
		if("HTTP".equals(module))
		{
			HttpRequest r = new HttpRequest(HttpMethods.GET);
			r.setUrl((String) param);
			Gdx.net.sendHttpRequest(r, new HttpResponseListener() {
				
				@Override
				public void handleHttpResponse(HttpResponse res) {
					System.out.println(res.getResultAsString());
					
				}
				
				@Override
				public void failed(Throwable t) {
					t.printStackTrace();
					
				}
				
				@Override
				public void cancelled() {
					System.out.println("Cancelled");
					
				}
			});
			return null;
		}
		else
		{
			HardwarePlugin e = current.plugins.get(module);
			if(e==null) e = initPlugin(module);
			return e.exec(command, param);
		}
	}

	private HardwarePlugin initPlugin(String module) {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends HardwarePlugin> cc = (Class<? extends HardwarePlugin>) current.game.getClass().getClassLoader().loadClass(module);
			HardwarePlugin p = cc.newInstance();
			p.init(this, hw);
			current.plugins.put(module, p);
			return p;
		} catch (Exception e) {
			throw new RuntimeException("Unable to load plugin "+module, e);
		}
	}

	@Override
	public void music(final int musicNum, final float volume, final boolean loop) {
		
		
		// inside class SynthesisFilter there's a file loaded from resources
		// without privileged access it doesn't work
		
		
		// Not needed since we switched to WiseLoader
//		AccessController.doPrivileged( new PrivilegedAction<Void>() {
//			
//			@Override
//			public Void run() {

				if (currentMusic != null) {
					currentMusic.stop();
				}
				currentMusic = current.getMusic(musicNum);
				if(currentMusic == null) throw new RuntimeException("Trying to play music "+musicNum+" but was not found");
				currentMusic.setVolume(volume);
				currentMusic.setLooping(loop);
				currentMusic.play();
//				return null;
//			}});
//		
		
	}

	@Override
	public void stopMusic() {
		if (currentMusic != null) {
			currentMusic.stop();
			currentMusic = null;
		}
	}
	
	@Override
	public void execute(final Cartridge cartridge, final Consumer<String> onResult, final Consumer<Throwable> onException) {
		
		// schedule the start of a new cartridge at the end of the current cycle.
		
		afterLoop = new Runnable() {
			
			@Override
			public void run() {

				GameRun gr;
				try {
					gr = new GameRun(cartridge, new ScreenInfo(options.getRenderingToTexture()), onResult, onException);
				} catch (Exception e) {
					onException.accept(e);
					return;
				}
				
				gameStack.push(gr);
				current = gr;
				
				try {
					initOrResumeGameRun(gr);
				} catch (Exception e) {
					// if we're here, we already pushed the game on the stack, just handle normal quit/except
					handleQuitOrException(null, e);
				}
				// we clean mouse state so that clicking doesn't pass to child
				for (Pointer p : pointers) {
					MouseImpl mouse = (MouseImpl)p;
					mouse.resetButtons();
				}
				
				
			}
		}; 
				
	}

	@Override
	public void quit(final String result) {
		afterLoop = new Runnable() {
			
			@Override
			public void run() {

				handleQuitOrException(result, null);				
				
			}
		};
		
	}

	@Override
	public void activateKeyboardInput(KeyboardListener listener) {
		this.keyboardListener  = listener;
	}

	@Override
	public byte[] binfile(int numFile) {
		
		return current.loadBinfile(numFile);
	}

	private void handleQuitOrException(final String result, Throwable ex) {
		if(ex!=null)ex.printStackTrace();
		GameRun old = gameStack.pop();
		Consumer<String> onResult = old.onResult;
		Consumer<Throwable> onException = old.onException;
		old.dispose();
		if(gameStack.isEmpty())
		{
			// the main cartridge had quit or excepted.
			if(ex != null)
			{
				// if excepted, pop out the exception and crash the program
				throw new RuntimeException("Main cartridge threw an exception", ex);
			}
			else
			{
				// if simple quit, idk just close the app i guess
				Gdx.app.exit();
			}
		}
		else
		{
			current = gameStack.peek();
			
			// we clean mouse state so that clicking doesn't pass to parent
			for (Pointer p : pointers) {
				MouseImpl mouse = (MouseImpl)p;
				mouse.resetButtons();
			}
			
			initOrResumeGameRun(current);
			
			if(ex == null)
			{
				if(onResult!=null) onResult.accept(result);
			}
			else
			{
				if(onException!=null) onException.accept(ex);
			}
			
			old = null;
			onResult = null;
			onException = null;			
			System.gc();
		}
	}

	@Override
	public long millis() {
		
		return System.currentTimeMillis();
	}

	@Override
	public void trace(String s) {
		System.out.println("TRACE "+s);
	}

	public void contextReset()
	{
		// create all opengl-related things.
		System.out.println("Creating pixmap");
		Pixmap p = new Pixmap(1, 1, Format.RGBA8888);
		p.drawPixel(0, 0, ColorsCopy.WHITE);
		System.out.println("Creating texture");
		pixel = new Texture(p);
		lastPixel = ColorsCopy.WHITE;
		System.out.println("Creating spriteBatcg");
			
		batch = new NonBleedingSpriteBatch();
		System.out.println("done");
	}

	public void contextDestroy()
	{
		// create all opengl-related things.
		
		pixel.dispose();
		pixel = null;
			
		batch.dispose();
		batch = null;
		
		for (GameRun gr : gameStack) {
			gr.dispose();
		}
	}
}
