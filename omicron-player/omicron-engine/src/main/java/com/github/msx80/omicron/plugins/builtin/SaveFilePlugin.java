package com.github.msx80.omicron.plugins.builtin;

import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;

public class SaveFilePlugin implements HardwarePlugin {

	private HardwareInterface hw;

	@Override
	public void init( HardwareInterface hw) {
		this.hw = hw;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object exec(String command, Object params) {
	/*	Object[] p = (Object[]) params;
		String mimeType = (String) p[0];
		String filename = (String) p[1];
		Consumer<String> result = (Consumer<String>) p[2];
		byte[] content = (byte[]) p[3];
		hw.saveFile(mimeType, filename, content, result);*/
		throw new UnsupportedOperationException("Unimplemented");
	}

}
