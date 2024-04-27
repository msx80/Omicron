package com.github.msx80.omicron;

import java.util.function.Consumer;

public interface HardwareInterface {

	void openUrl(String url) throws Exception;
	String[] startupArgs();
	void saveFile(String mimeType, String filename, byte[] content, Consumer<String> result);
	Object hardware(String module, String command, Object param);
}
