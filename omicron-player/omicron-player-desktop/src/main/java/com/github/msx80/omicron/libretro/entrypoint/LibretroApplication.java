package com.github.msx80.omicron.libretro.entrypoint;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Net;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import com.badlogic.gdx.backends.lwjgl3.audio.mock.MockAudio;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.ObjectMap;

public class LibretroApplication implements Application {

	private ApplicationListener game;

	private final ObjectMap<String, Preferences> preferences = new ObjectMap<String, Preferences>();
	
	public LibretroApplication(ApplicationListener game) {
		this.game = game;
		Gdx.app = this;

		Gdx.gl20 = new Lwjgl3GL20(); // new UnimplementedGL20();
		Gdx.gl = Gdx.gl20;
		Gdx.gl30 = new Lwjgl3GL30(); // new UnimplementedGL30();
		
		Gdx.graphics = new LibretroGraphics(Gdx.gl20, Gdx.gl30);
		Gdx.files = new Lwjgl3Files();
		Gdx.input = new MockInput();
		Gdx.audio = new MockAudio();
		Gdx.net = new Lwjgl3Net(new Lwjgl3ApplicationConfiguration()); // TODO fix ?
		
		game.create();
	}

	@Override
	public ApplicationListener getApplicationListener() {
		
		return game;
	}

	@Override
	public Graphics getGraphics() {
		return Gdx.graphics;
	}

	@Override
	public Audio getAudio() {
		return Gdx.audio;
	}

	@Override
	public Input getInput() {
		return Gdx.input;
	}

	@Override
	public Files getFiles() {
		return Gdx.files;
	}

	@Override
	public Net getNet() {

		return null;
	}

	@Override
	public void log(String tag, String message) {
		System.out.println("[LIBGDX LOG "+tag+"]"+message);

	}

	@Override
	public void log(String tag, String message, Throwable exception) {
		System.out.println("[LIBGDX LOG "+tag+"]"+message);
		exception.printStackTrace(System.out);
	}

	@Override
	public void error(String tag, String message) {
		System.out.println("[LIBGDX ERR "+tag+"]"+message);
	}

	@Override
	public void error(String tag, String message, Throwable exception) {
		System.out.println("[LIBGDX ERR "+tag+"]"+message);
		exception.printStackTrace(System.out);

	}

	@Override
	public void debug(String tag, String message) {
		System.out.println("[LIBGDX DBG "+tag+"]"+message);

	}

	@Override
	public void debug(String tag, String message, Throwable exception) {
		System.out.println("[LIBGDX DBG "+tag+"]"+message);
		exception.printStackTrace(System.out);
	}

	@Override
	public void setLogLevel(int logLevel) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLogLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setApplicationLogger(ApplicationLogger applicationLogger) {
		// TODO Auto-generated method stub

	}

	@Override
	public ApplicationLogger getApplicationLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationType getType() {
		
		return null;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getJavaHeap() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNativeHeap() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Preferences getPreferences(String name) {
		if (preferences.containsKey(name)) {
			return preferences.get(name);
		} else {
			String tmpdir = System.getProperty("java.io.tmpdir");
			Preferences prefs = new Lwjgl3Preferences(new Lwjgl3FileHandle(new File(tmpdir, name), FileType.Absolute));
			/*Preferences prefs = new Preferences() {
				Map<String, String> mm = new HashMap<String, String>();
				@Override
				public void remove(String key) {
					mm.remove(key);
					
				}
				
				@Override
				public Preferences putString(String key, String val) {
					mm.put(key, val);
					return this;
				}
				
				@Override
				public Preferences putLong(String key, long val) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Preferences putInteger(String key, int val) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Preferences putFloat(String key, float val) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Preferences putBoolean(String key, boolean val) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Preferences put(Map<String, ?> vals) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public String getString(String key, String defValue) {
					
					return mm.getOrDefault(key, defValue);
				}
				
				@Override
				public String getString(String key) {
					
					return getString(key, "");
				}
				
				@Override
				public long getLong(String key, long defValue) {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public long getLong(String key) {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public int getInteger(String key, int defValue) {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public int getInteger(String key) {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public float getFloat(String key, float defValue) {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public float getFloat(String key) {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public boolean getBoolean(String key, boolean defValue) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public boolean getBoolean(String key) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public Map<String, ?> get() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public void flush() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean contains(String key) {
					
					return mm.containsKey(key);
				}
				
				@Override
				public void clear() {
					mm.clear();
				}
			};*/
			preferences.put(name, prefs);
			return prefs;
		}
	}

	@Override
	public Clipboard getClipboard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void postRunnable(Runnable runnable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub

	}

}
