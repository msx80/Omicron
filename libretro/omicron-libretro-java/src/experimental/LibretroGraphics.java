package experimental;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.GLVersion;

public class LibretroGraphics implements Graphics {

	private GL20 gl20;
	private GL30 gl30;
	
	private long lastFrameTime = -1;
	private float deltaTime;
	private long frameId;
	private long frameCounterStart = 0;
	private int frames;
	private int fps;

	public LibretroGraphics(GL20 gl20, GL30 gl30) {
		this.gl20 = gl20;
		this.gl30 = gl30;
	}

	@Override
	public boolean isGL30Available() {
		
		return gl30 != null;
	}

	@Override
	public GL20 getGL20() {
		
		return gl20;
	}

	@Override
	public GL30 getGL30() {
		return gl30;
	}

	@Override
	public void setGL20(GL20 gl20) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void setGL30(GL30 gl30) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public int getWidth() {
		
		return 640;
	}

	@Override
	public int getHeight() {
		return 400;
	}

	@Override
	public int getBackBufferWidth() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public int getBackBufferHeight() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public long getFrameId() {
		return frameId;
	}

	@Override
	public float getDeltaTime() {
		return deltaTime;
	}

	@Override
	public float getRawDeltaTime() {
		return deltaTime;
	}

	@Override
	public int getFramesPerSecond() {
		return fps;
	}


	@Override
	public GraphicsType getType() {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public GLVersion getGLVersion() {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public float getPpiX() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public float getPpiY() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public float getPpcX() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public float getPpcY() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public float getDensity() {
		throw new RuntimeException("Unimplemented method");
		//return 0;
	}

	@Override
	public boolean supportsDisplayModeChange() {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public Monitor getPrimaryMonitor() {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public Monitor getMonitor() {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public Monitor[] getMonitors() {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public DisplayMode[] getDisplayModes() {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public DisplayMode[] getDisplayModes(Monitor monitor) {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public DisplayMode getDisplayMode() {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public DisplayMode getDisplayMode(Monitor monitor) {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public boolean setFullscreenMode(DisplayMode displayMode) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public boolean setWindowedMode(int width, int height) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public void setTitle(String title) {
		// nope

	}

	@Override
	public void setUndecorated(boolean undecorated) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void setResizable(boolean resizable) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void setVSync(boolean vsync) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public BufferFormat getBufferFormat() {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public boolean supportsExtension(String extension) {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public void setContinuousRendering(boolean isContinuous) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public boolean isContinuousRendering() {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public void requestRendering() {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public boolean isFullscreen() {
		throw new RuntimeException("Unimplemented method");
		//return false;
	}

	@Override
	public Cursor newCursor(Pixmap pixmap, int xHotspot, int yHotspot) {
		throw new RuntimeException("Unimplemented method");
		//return null;
	}

	@Override
	public void setCursor(Cursor cursor) {
		throw new RuntimeException("Unimplemented method");

	}

	@Override
	public void setSystemCursor(SystemCursor systemCursor) {
		throw new RuntimeException("Unimplemented method");

	}

	public void update() {
		long time = System.nanoTime();
		if (lastFrameTime == -1)
			lastFrameTime = time;
		deltaTime = (time - lastFrameTime) / 1000000000.0f;
		lastFrameTime = time;

		if (time - frameCounterStart >= 1000000000) {
			fps = frames;
			frames = 0;
			frameCounterStart = time;
		}
		frames++;
		frameId++;
	}

	
}
