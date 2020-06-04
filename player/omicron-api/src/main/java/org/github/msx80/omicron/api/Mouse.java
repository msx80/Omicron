package org.github.msx80.omicron.api;

public interface Mouse {
	int x();
	int y();
	public boolean btn(int n);
	public boolean btnp(int n);
}
