package com.github.msx80.omicron;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.plugins.builtin.ArgsPlugin;
import com.github.msx80.omicron.plugins.builtin.SaveFilePlugin;
import com.github.msx80.omicron.plugins.builtin.UrlOpenerPlugin;

public class GameRun {
	public final Game game;
	public final ScreenInfo screenInfo;
	public final Consumer<String> onResult;
	public final Consumer<Throwable> onException;
	
	public final Map<Integer, Sound> sounds = new HashMap<Integer, Sound>();
	public final Map<Integer, Music> music = new HashMap<Integer, Music>();
	public final Map<Integer, TextureRegion> sheets = new HashMap<Integer, TextureRegion>();
	
	private Set<Integer> textureToReload = null;	
	
	//Map<String, HardwarePlugin> plugins = new HashMap<String, HardwarePlugin>();
	private Cartridge cartridge;
	
	public GameRun(Cartridge cartridge, ScreenInfo screenInfo, Consumer<String> onResult, Consumer<Throwable> onException) {
		try {
			this.cartridge = cartridge;
			this.game = cartridge.getGameObject();
			this.screenInfo = screenInfo;
			this.onResult = onResult;
			this.onException = onException;
			
//			HardwarePlugin pp = new DebugPlugin();
//			UrlOpenerPlugin up = new UrlOpenerPlugin();
//			ArgsPlugin ap = new ArgsPlugin();
//			SaveFilePlugin sp = new SaveFilePlugin();
//			plugins.put(pp.getClass().getName(), pp);
//			plugins.put(up.getClass().getName(), up);
//			plugins.put(ap.getClass().getName(), ap);
//			plugins.put(sp.getClass().getName(), sp);
		
		} catch (Exception e) {
			throw new RuntimeException("Unable to start cartridge: "+e.getMessage(), e );
		}
		
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
			byte[] is = cartridge.loadFile(fn);
			if(is == null) return null; // no such resource
			
			FileHandle ff = Gdx.files.local(fn);
			
			if(ff.exists() && (ff.length() == is.length))
			{
				// already cached, resuse the same file
			}
			else
			{
				ff.writeBytes(is, false);
			}
			return ff;
		}
		else
		{
			
			CartridgeFileHandle res = new CartridgeFileHandle(cartridge, fn);
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
			String fn = "music"+n+".mp3";
			FileHandle fh = loadResource(fn);
			System.out.println("Loading music: "+fh);
			if(fh== null) return null;
			return Gdx.audio.newMusic(fh);
		}
		private TextureRegion loadSheet(int n) {
			FileHandle r = loadResource("sheet"+n+".png");
			if (r==null) return null;
			
			Pixmap p = readPixmapSafe(r);
			p.setBlending(Blending.None);
			Texture tt = new Texture(p, false);
			TextureRegion img = new TextureRegion(tt);
			img.flip(false, true);
			return img;
		}


		private Pixmap readPixmapSafe(FileHandle r) {
			try {
				System.out.println("Loading pixmap");
			Pixmap p = new Pixmap( r); // Gdx2DPixmap.newPixmap(stre, 0));
			System.out.println("Pixmap loaded ok!");
			return p;
			} catch (Throwable e) {
				throw new RuntimeException("**** Unable to load pixmap! "+e.getMessage(), e); 
			}
		}	  
		private Sound loadSound(int n) {
			String fn = "sound"+n+".wav";
			FileHandle fh = loadResource(fn);
			if(fh== null) return null;
			return Gdx.audio.newSound(fh);
		}
		
	    public TextureRegion getSheet(int sheetNum) {
	    	TextureRegion b = sheets.get(sheetNum);
	        if(b == null)
	        {
	        	System.out.println("Calling load sheet "+sheetNum);
	        	b = loadSheet(sheetNum);
	            
	        	sheets.put(sheetNum, b);
	        }
	        return b;
	    }

		public byte[] loadBinfile(int n) {
			FileHandle r = loadResource("file"+n+".bin");
			
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
			FileHandle r = new CartridgeFileHandle(cartridge, "/sheet"+n+".png");
			if(!r.exists()) return null;
			if(r.read() == null)
			{
				String resourceName = "/"+x.getClass().getPackage().getName().replace('.', '/')+"/sheet"+n+".png";
				System.out.println("trying "+resourceName);
				r = new CartridgeFileHandle(cartridge, resourceName);
			}
			if(!r.exists()) return null;
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
			// Note: becouse of all opengl stuff this should reset to a usable state
			
			for (Disposable a : sounds.values()) {
				if(a!=null) a.dispose();
			}
			for (Disposable a : music.values()) {
				if(a!=null) a.dispose();
			}
			for (TextureRegion a : sheets.values()) {
				if(a!=null)
				{
					Texture t = a.getTexture();
					if(t!=null)t.dispose();
				}
			}
			sounds.clear();
			music.clear();
			sheets.clear();
			if(textureToReload != null)	textureToReload.clear();
			
			
		}
		
}
