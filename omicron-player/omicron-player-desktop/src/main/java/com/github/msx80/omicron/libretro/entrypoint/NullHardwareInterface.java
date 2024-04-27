package com.github.msx80.omicron.libretro.entrypoint;

import java.util.function.Consumer;

import com.github.msx80.omicron.HardwareInterface;

public class NullHardwareInterface implements HardwareInterface {

	@Override
	public void openUrl(String url) throws Exception {
		

	}

	@Override
	public String[] startupArgs() {
		return new String[0];
	}

	@Override
	public void saveFile(String arg0, String arg1, byte[] arg2, Consumer<String> arg3) {
		
		
	}

	@Override
	public Object hardware(String module, String command, Object param) {
		
		return null;
	}

}
