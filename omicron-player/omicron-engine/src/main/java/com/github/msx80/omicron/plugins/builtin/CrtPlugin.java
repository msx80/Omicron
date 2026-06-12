package com.github.msx80.omicron.plugins.builtin;

import com.badlogic.gdx.graphics.Pixmap;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.BloomEffect;
import com.crashinvaders.vfx.effects.RadialDistortionEffect;
import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;

public class CrtPlugin implements HardwarePlugin {

	
	
	 private VfxManager vfxManager = null;
	 private BloomEffect vfxEffect;
	 private RadialDistortionEffect vfxEffect2;
	
	@Override
	public void init(HardwareInterface hw) {
	      vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
	        vfxEffect2 = new RadialDistortionEffect();
	        vfxEffect2.setDistortion(0.1f);
	        vfxManager.addEffect(vfxEffect2);
	        vfxEffect = new BloomEffect();
	        vfxManager.addEffect(vfxEffect);

	}

	@Override
	public Object exec(String command, Object params) throws Exception {
		// just init the plugin
		return null;
	}

	@Override
	public void dispose() {
		 vfxManager.dispose();
         vfxEffect.dispose();
         vfxEffect2.dispose();
		
	}

	@Override
	public void beforeRender() {
		// Clean up internal buffers, as we don't need any information from the last render.
        vfxManager.cleanUpBuffers();

        // Begin render to an off-screen buffer.
        vfxManager.beginInputCapture();
		
	}

	@Override
	public void afterRender() {
		 // End render to an off-screen buffer.
        vfxManager.endInputCapture();

        // Apply the effects chain to the captured frame.
        // In our case, only one effect (gaussian blur) will be applied.
        vfxManager.applyEffects();

        // Render result to the screen.
        vfxManager.renderToScreen();

	}

	@Override
	public void resize(int width, int height) {
		vfxManager.resize(width, height);
	}
	
	

}
