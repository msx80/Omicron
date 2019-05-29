package org.github.msx80.omicron;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.github.msx80.omicron.api.Controller;
import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Mouse;
import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.Colors;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;


public final class GdxOmicron extends ApplicationAdapter implements Sys {
	
	FPSLogger fps = new FPSLogger();
	SpriteBatch batch;
	OrthographicCamera cam=new OrthographicCamera();
	
	Game game;
	
	Mouse mouse = new Mouse();
	Controller[] controllers;
	
	Map<Integer, TextureRegion> sheets = new HashMap<Integer, TextureRegion>();
	Map<Integer, Sound> sounds = new HashMap<Integer, Sound>();
	
	private int ox = 0;
	private int oy = 0;
	private Set<Integer> textureToReload = new HashSet<Integer>();	
	
	Texture pixel;
	private int lastPixel;
	
	ScreenInfo screenInfo = new ScreenInfo();
	
	Rectangle scissor = new Rectangle();
	
	Map<String, HardwarePlugin> plugins = new HashMap<String, HardwarePlugin>();
	
	public GdxOmicron(Game game) {
		super();
		this.game = game;
		
	}
	
	Vector3 proj = new Vector3();
	private Preferences prefs = null;

	@Override
	public void create () {

		
		Pixmap p = new Pixmap(1, 1, Format.RGBA8888);
		p.drawPixel(0, 0, Colors.WHITE);
		pixel = new Texture(p);
		lastPixel = Colors.WHITE;
		
		
		batch = new SpriteBatch();
				
	
		controllers = new Controller[] {new Controller()}; // first is keyboard, TODO use joypad etc.
		
		Gdx.input.setInputProcessor(new MyInputProcessor());
		
		Cursor cursor = Gdx.graphics.newCursor(new Pixmap(1, 1, Format.RGBA8888),0,0);
		Gdx.graphics.setCursor(cursor);
		
		screenInfo.requiredSysConfig = game.sysConfig();

		HardwarePlugin pp = new DebugPlugin();
		plugins.put(pp.name(), pp);
		game.init(this);
		if(screenInfo.requiredSysConfig.title!=null) Gdx.graphics.setTitle(screenInfo.requiredSysConfig.title);
		setUpCam(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
		
	}

	private void setUpCam(int winwidth, int winheight) 
	{
		System.out.println("Resize to "+winwidth+" "+winheight);
		screenInfo.handleResize(winwidth, winheight, cam);
	}

	
	
	@Override
	public void resize(int width, int height) {
		
		setUpCam(width, height);
		
	}

	public void colorf(float red, float green, float blue, float alpha) {
		batch.setColor(red, green, blue, alpha);
	}

	
    private TextureRegion getSheet(int sheetNum) {
    	TextureRegion b = sheets.get(sheetNum);
        if(b == null)
        {
        	
        	b = loadSheet(sheetNum);
            
        	sheets.put(sheetNum, b);
        }
        return b;
    }
    private Sound getSound(int soundNum) {
    	Sound b = sounds.get(soundNum);
        if(b == null)
        {
        	
        	b = loadSound(soundNum);
            
        	sounds.put(soundNum, b);
        }
        return b;
    }
	
	
	@Override
	public void render () {
		fps.log();
		offset(0,0); // reset offset
			
		game.update(); 
		
		batch.setProjectionMatrix(cam.combined);
	
		this.colorf(1, 1, 1, 1);
		Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
		this.clear(Colors.BLACK);
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
			
		screenInfo.applyGlClipping();
		
		batch.begin();
		try
		{
			game.render();
		}
		finally
		{
			batch.end();
		}
	}

	private void uploadDirtyTexture() {
		if (textureToReload != null)
		{
			for (Integer sn : textureToReload) {
				Texture r = getSheet(sn).getTexture();
				r.load(r.getTextureData());
			}
			textureToReload=null;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
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
		
		TextureRegion r = getSheet(sheetNum);
		r.setRegion(srcx, srcy, w, h);
		r.flip(false, true);
		batch.draw(r, x+ox, y+oy);
		// batch.draw(r.getTexture(), x,y,srcx,srcy,w,h);
	}
	
	@Override
	public void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h, int rotate, int flip)
	{
		uploadDirtyTexture();
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
			
			TextureRegion r = getSheet(sheetNum);
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
			
			
			// not working yet
			proj.set(x,y,0);
			cam.project(proj);
			
			byte[] b = ScreenUtils.getFrameBufferPixels(Math.round(proj.x), Math.round(proj.y), 1, 1, false);
			
			if(is) batch.begin();
			
			return Colors.from(0xFF & b[0], 0xFF & b[1], 0xFF &  b[2], 0xFF & b[3]);
		}
		else
		{
			TextureRegion r = getSheet(sheetNum);
			PixmapTextureData d = (PixmapTextureData) r.getTexture().getTextureData();
			
			return d.consumePixmap().getPixel(x, y);
		}
	}

	@Override
	public int newSurface(int w, int h) {
		Pixmap p = new Pixmap(w, h, Format.RGBA8888);
		
		int i = -1;
		while(sheets.containsKey(i))
		{
			i--;
		}
		
		Texture tt = new Texture(p, false);
		
		TextureRegion img = new TextureRegion(tt);
		img.flip(false, true);
		
		sheets.put(i, img);
		
		return i;
	}

	@Override
	public void setPix(int sheetNum, int x, int y, int color) {
		if(sheetNum==0)
		{
			if(lastPixel != color)
			{
				boolean a = batch.isDrawing();
				if(a)batch.end();
				( (PixmapTextureData) pixel.getTextureData() ).consumePixmap().drawPixel(0, 0, color);
				lastPixel = color;
				pixel.load(pixel.getTextureData());
				if(a)batch.begin();
			}
			batch.draw(pixel, x+ox, y+oy);
		}
		else
		{
			TextureRegion r = getSheet(sheetNum);
			PixmapTextureData d = (PixmapTextureData) r.getTexture().getTextureData();
			d.consumePixmap().drawPixel(x, y, color);
			
			// we don't actually update textures now, just mark as dirty so we upload later
			// user could be updating a large area
			if(textureToReload == null) textureToReload = new HashSet<Integer>();
			textureToReload.add(sheetNum);
		}
	}


	@Override
	public void offset(int x, int y) {
		ox = x;
		oy  = y;
		
	}
	
	
	public class MyInputProcessor implements InputProcessor {
		   public boolean keyDown (int keycode) {
			   Controller c = controllers[0];
			   switch (keycode) {
					case Input.Keys.UP: c.up=true; break;
					case Input.Keys.DOWN: c.down=true; break;
					case Input.Keys.LEFT: c.left=true; break;
					case Input.Keys.RIGHT: c.right=true; break;
		
					case Input.Keys.Z: c.btn[0]=true; break;
					case Input.Keys.X: c.btn[1]=true; break;
			
			default:
				break;
			}
		      return true;
		   }

		   public boolean keyUp (int keycode) {
			   Controller c = controllers[0];
			   switch (keycode) {
					case Input.Keys.UP: c.up=false; break;
					case Input.Keys.DOWN: c.down=false; break;
					case Input.Keys.LEFT: c.left=false; break;
					case Input.Keys.RIGHT: c.right=false; break;
		
					case Input.Keys.Z: c.btn[0]=false; break;
					case Input.Keys.X: c.btn[1]=false; break;
			
			default:
				break;
			}
			   return true;
		   }

		   public boolean keyTyped (char character) {
		      return true;
		   }

		   public boolean touchDown (int x, int y, int pointer, int button) {
			   if(mouse == null) return true;
			   proj.set(x,y,0);
			   cam.unproject(proj);
			   mouse.x = (int) proj.x;
			   mouse.y = (int) proj.y;
			  mouse.btn[0] = true;
		      return true;
		   }

		   public boolean touchUp (int x, int y, int pointer, int button) {
			   if(mouse == null) return true;
			   proj.set(x,y,0);
			   cam.unproject(proj);
			   mouse.x = (int) proj.x;
			   mouse.y = (int) proj.y;
			   mouse.btn[0] = false;
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

	private TextureRegion loadSheet(int n) {

		Pixmap p = new Pixmap( new ResourceFileHandle("/sheet"+n+".png", game.getClass()) ); // Gdx2DPixmap.newPixmap(stre, 0));
			
		Texture tt = new Texture(p, false);
			
		TextureRegion img = new TextureRegion(tt);
		img.flip(false, true);
		return img;
	}
	private Sound loadSound(int n) {
		String fn = "/sound"+n+".wav";
		if(Gdx.app.getType() == ApplicationType.Android)
		{
			// no way to load from stream :(
			FileHandle ff = Gdx.files.local(fn);
			ff.write(game.getClass().getResourceAsStream(fn), false);
			return Gdx.audio.newSound(ff);
		}
		else
		{
			return Gdx.audio.newSound( new ResourceFileHandle(fn, game.getClass())   );
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
	public Mouse mouse() {
		return mouse;
	}

	@Override
	public Controller[] controllers() {
		return controllers;
	}

	@Override
	public void sound(int soundNum, float volume, float pitch) {
		getSound(soundNum).play(volume, pitch, 0);
		
	}

	@Override
	public String mem(String key) {
		return getPrefs().getString(key);
		
	}

	private Preferences getPrefs() {
		if(prefs == null)
		{
			prefs = Gdx.app.getPreferences(screenInfo.requiredSysConfig.code);
		}
		return prefs ;
	}

	@Override
	public void mem(String key, String value) {
		
			getPrefs().putString(key, value).flush();
		
	}

	@Override
	public String hardware(String module, String command, String param) {
		HardwarePlugin e = plugins.get(module);
		return e == null ? null : e.exec(command, param);
	}
	
}
