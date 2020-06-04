package org.github.msx80.omicron;

import org.github.msx80.omicron.api.adv.Cartridge;

import com.badlogic.gdx.ApplicationListener;

public class OmicronEngineLibgdx {
	
	/**
	 * Returns a valid libgdx ApplicationListener to be started by any libgdx frontend.
	 * @param game
	 * @return
	 */
	public static ApplicationListener getApplicationForLibgdx(Cartridge cartridge, HardwareInterface hardwareInterface)
	{
		return new GdxOmicron(cartridge, hardwareInterface);		
	}
	
}
