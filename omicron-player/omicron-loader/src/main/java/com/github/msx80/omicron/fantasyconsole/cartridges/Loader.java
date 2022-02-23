package com.github.msx80.omicron.fantasyconsole.cartridges;

import java.io.Closeable;
import java.util.function.Consumer;

public interface Loader extends Closeable 
{
	public byte[] loadFile(String name);
	public void close();
	public void forEachFile(Consumer<String> consumer);
}
