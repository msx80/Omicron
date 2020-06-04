package org.github.msx80.omicron;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.adv.CustomResourceGame;
import org.github.msx80.omicron.plugins.UrlOpenerPlugin;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class GameRun {
	public final Game game;
	public final ScreenInfo screenInfo;
	public final Consumer<String> onResult;
	
	public final Map<Integer, Sound> sounds = new HashMap<Integer, Sound>();
	public final Map<Integer, Music> music = new HashMap<Integer, Music>();
	public final Map<Integer, TextureRegion> sheets = new HashMap<Integer, TextureRegion>();
	
	private Set<Integer> textureToReload = null;	
	
	Map<String, HardwarePlugin> plugins = new HashMap<String, HardwarePlugin>();
	
	public GameRun(Game game, ScreenInfo screenInfo, Consumer<String> onResult) {
		this.game = game;
		this.screenInfo = screenInfo;
		this.onResult = onResult;
		
		HardwarePlugin pp = new DebugPlugin();
		UrlOpenerPlugin up = new UrlOpenerPlugin();
		plugins.put(pp.name(), pp);
		plugins.put(up.name(), up);
		
	}
	

    public Music getMusic(int musicNum) {
    	Music b = music.get(musicNum);
        if(b == null)
        {
        	
        	b = loadMusic(musicNum);
            
        	music.put(musicNum, b);
        }
        return b;
    }

	private FileHandle loadResource(String fn) {
		
		if(Gdx.app.getType() == ApplicationType.Android)
		{
			// no way to load from stream :( unload on the local disk and load from there.
			InputStream is;
			if(game instanceof CustomResourceGame)
			{
				is = ((CustomResourceGame) game).getResourceAsStream(fn);
			}
			else
			{
				is = game.getClass().getResourceAsStream(fn);
			}
			if(is == null) return null; // no such resource
			
			FileHandle ff = Gdx.files.local(fn);
			
			long av;
			try {
				av = is.available();
			} catch (IOException e) {
				throw new RuntimeException("available() failed: "+e.getMessage(), e);
			}
			if(ff.exists() && (ff.length() == av))
			{
				// already cached, resuse the same file
			}
			else
			{
				ff.write(is, false);
			}
			return ff;
		}
		else
		{
			
			ResourceFileHandle res;
			if(game instanceof CustomResourceGame)
			{
				res = new ResourceFileHandle(fn, (CustomResourceGame)game);
			}
			else
			{
				res = new ResourceFileHandle(fn, game.getClass());
			}
			if (!res.exists()) return null;
			return res;
		}
	
	}
	
	
    
	  public Sound getSound(int soundNum) {
	    	Sound b = sounds.get(soundNum);
	        if(b == null)
	        {
	        	
	        	b = loadSound(soundNum);
	            
	        	sounds.put(soundNum, b);
	        }
	        return b;
	    }
		private Music loadMusic(int n) 
		{
			String fn = "/music"+n+".mp3";
			FileHandle fh = loadResource(fn);
			System.out.println("Loading music: "+fh);
			if(fh== null) return null;
			return Gdx.audio.newMusic(fh);
		}
		private TextureRegion loadSheet(int n) {
			FileHandle r = loadResource("/sheet"+n+".png");
			
			if (r==null) return null;
			
			Pixmap p = new Pixmap( r); // Gdx2DPixmap.newPixmap(stre, 0));
			p.setBlending(Blending.None);
			Texture tt = new Texture(p, false);
				
			TextureRegion img = new TextureRegion(tt);
			img.flip(false, true);
			return img;
		}	  
		private Sound loadSound(int n) {
			String fn = "/sound"+n+".wav";
			FileHandle fh = loadResource(fn);
			if(fh== null) return null;
			return Gdx.audio.newSound(fh);
		}
		
	    public TextureRegion getSheet(int sheetNum) {
	    	TextureRegion b = sheets.get(sheetNum);
	        if(b == null)
	        {
	        	
	        	b = loadSheet(sheetNum);
	            
	        	sheets.put(sheetNum, b);
	        }
	        return b;
	    }

		public byte[] loadBinfile(int n) {
			FileHandle r = loadResource("/file"+n+".bin");
			
			if (r==null) return null;
			
			return r.readBytes();
		}
		
		
		@SuppressWarnings("unused")
		private TextureRegion loadSheetCustom(int n) {
			// this version has a hack that can load png directly from the resources without passing for the storage first.
			// since it's hacky and for uniformity, and to support CustomGameResource i'll use the same system used for sound and music (the storage first)
			Game x = game;
			
			System.out.println("Reading resource "+n);
		
			//System.out.println(game.getClass());
			//System.out.println(game.getClass().getPackage().getName());
			ResourceFileHandle r = new ResourceFileHandle("/sheet"+n+".png", x.getClass());
			if(!r.exists()) return null;
			if(r.read() == null)
			{
				String resourceName = "/"+x.getClass().getPackage().getName().replace('.', '/')+"/sheet"+n+".png";
				System.out.println("trying "+resourceName);
				r = new ResourceFileHandle(resourceName, x.getClass());
			}
			Pixmap p = new Pixmap( r); // Gdx2DPixmap.newPixmap(stre, 0));
			p.setBlending(Blending.None);
			Texture tt = new Texture(p, false);
				
			TextureRegion img = new TextureRegion(tt);
			img.flip(false, true);
			return img;
		}
		public void uploadDirtyTexture() {
			if (textureToReload != null)
			{
				for (Integer sn : textureToReload) {
					Texture r = getSheet(sn).getTexture();
					r.load(r.getTextureData());
				}
				textureToReload=null;
			}
		}


		public void addDirtyTexture(int sheetNum) {
			// we don't actually update textures now, just mark as dirty so we upload later
			// user could be updating a large area
			if(textureToReload == null) textureToReload = new HashSet<Integer>();
			textureToReload.add(sheetNum);
		}
		
		public void dispose()
		{
			for (Disposable a : sounds.values()) {
				a.dispose();
			}
			for (Disposable a : music.values()) {
				a.dispose();
			}
			for (TextureRegion a : sheets.values()) {
				a.getTexture().dispose();
			}
			sounds.clear();
			music.clear();
			sheets.clear();
			if(textureToReload != null)	textureToReload.clear();
		}
		
}
