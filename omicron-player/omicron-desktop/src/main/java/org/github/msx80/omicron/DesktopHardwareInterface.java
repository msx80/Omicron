package org.github.msx80.omicron;

import java.awt.Desktop;
import java.net.URI;
import java.util.function.Consumer;

public class DesktopHardwareInterface implements HardwareInterface {

	
	
	private String[] args;
	
	

	public DesktopHardwareInterface(String[] args) {
		this.args = args;
	}

	@Override
	public void openUrl(String url) throws Exception {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    Desktop.getDesktop().browse(new URI(url));
		}
		else
		{
			throw new UnsupportedOperationException("Operation not supported");
		}

	}

	@Override
	public String[] startupArgs() {
		
		return args;
	}

	@Override
	public void saveFile(String mimeType, String filename, byte[] content, Consumer<String> result) {
		result.accept("Not implemented");
	}

}
