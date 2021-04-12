package com.github.msx80.omicron.api;

public final class SysConfig {
	public enum VirtualScreenMode { 
		
		/** 
		 * Will scale the virtual screen an integer number of times, the max possible while still fitting 
		 * into the physical screen. Will mantain original aspect ratio and have pixel-perfect virtual-pixels 
		 * but also (probably) a black frame around the virtual screen. 
		 */
		SCALED, 
		
		
		/**
		 * Will scale one dimension of the virtual screen to fill the physical screen, the other will be scaled accordingly 
		 * to maintain aspect ratio. Will not have pixel-perfect virtual-pixels
		 */
		FILL_SIDE,
		
		/**
		 * Will scale the virtual screen to fill all physical screen. Will not maintain original aspect ratio, 
		 * nor have pixel-perfect virtual pixels. 
		 */
		STRETCH_FULL
		
	};
		
	public final int width;
	public final int height;
	public final String title;
	public final String code;
	public final VirtualScreenMode mode;
	
	public SysConfig(int width, int height, VirtualScreenMode mode, String title, String code) {
		super();
		this.width = width;
		this.height = height;
		this.title = title;
		this.code = code;
		this.mode = mode;
	}

	@Override
	public String toString() {
		return "SysConfig [width=" + width + ", height=" + height + ", title=" + title + ", code=" + code + ", mode="
				+ mode + "]";
	}
	
}
