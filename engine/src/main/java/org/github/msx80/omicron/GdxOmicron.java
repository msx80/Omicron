package org.github.msx80.omicron;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.github.msx80.omicron.api.Acceptor;
import org.github.msx80.omicron.api.Controller;
import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Mouse;
import org.github.msx80.omicron.api.ScreenConfig;
import org.github.msx80.omicron.api.Sys;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;


public class GdxOmicron extends ApplicationAdapter implements Sys {
	
	FPSLogger fps = new FPSLogger();
	SpriteBatch batch;
	OrthographicCamera cam;
	
	Game game;
	
	Mouse mouse = new Mouse();
	Controller[] controllers;
	
	Map<Integer, TextureRegion> sheets = new HashMap<Integer, TextureRegion>();
	
	private int ox = 0;
	private int oy = 0;	
	
	public GdxOmicron(Game game) {
		super();
		this.game = game;
	}

	@Override
	public void create () {

		batch = new SpriteBatch();
				
	
		controllers = new Controller[] {new Controller()}; // first is keyboard, TODO use joypad etc.
		
		Gdx.input.setInputProcessor(new MyInputProcessor());
		
		Cursor cursor = Gdx.graphics.newCursor(new Pixmap(1, 1, Format.RGBA8888),0,0);
		Gdx.graphics.setCursor(cursor);
		
		ScreenConfig s = game.screenConfig();
		game.init(this);
		if(s.title!=null) Gdx.graphics.setTitle(s.title);
		cam = new OrthographicCamera();//768/2,384/2);
		cam.setToOrtho(true,s.width, s.height);
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();
		
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
	
	
	@Override
	public void render () {
		fps.log();
		offset(0,0); // reset offset
		
			
		// render:
		//cam.update();
		game.update(); // update topmost game on the stack
		//batch.setTransformMatrix(new Matrix4());
		batch.setProjectionMatrix(cam.combined);
	
		this.colorf(1, 1, 1, 1);
			
		batch.begin();
		game.render();
		batch.end();
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


	@Override
	public void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h)
	{
		TextureRegion r = getSheet(sheetNum);
		r.setRegion(srcx, srcy, w, h);
		r.flip(false, true);
		batch.draw(r, x+ox, y+oy);
		// batch.draw(r.getTexture(), x,y,srcx,srcy,w,h);
	}
	
	@Override
	public void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h, int rotate)
	{
		TextureRegion r = getSheet(sheetNum);
		batch.draw(r.getTexture(), x+ox, y+oy,w/2f,h/2f,w,h,1,1,rotate, srcx, srcy, w, h, false, true); // TODO rotation
		
	}


	@Override
	public int getPix(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void http(String arg0, String arg1, String arg2, Acceptor<String> arg3, Acceptor<Exception> arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int newSurface(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPix(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
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
			  if(mouse != null) mouse.btn[0] = true;
		      return true;
		   }

		   public boolean touchUp (int x, int y, int pointer, int button) {
			   if(mouse != null) mouse.btn[0] = false;
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

	private TextureRegion loadSheet(int n) {
		
		InputStream stre  = game.getClass().getResourceAsStream("/sheet"+n+".png");
		try
		{
			Pixmap p = new Pixmap(Gdx2DPixmap.newPixmap(stre, 0));
			
			Texture tt = new Texture(p, false);
			
			TextureRegion img = new TextureRegion(tt);
			img.flip(false, true);
			return img;
		}
		finally
		{
			try {
				stre.close();
			} catch (IOException e) {
				throw new RuntimeException("Error closing resource", e);
			}
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
	
}
