package com.github.msx80.runtime.implementation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.msx80.omicron.Image;

public final class ImageImpl implements Image {

	TextureRegion img;

	public ImageImpl(TextureRegion img) {
		this.img = img;
	}

  public int width()
  {
	  return img.getRegionWidth();
  }
	public int height()
	{
		return img.getRegionHeight();
	}
	
	
}
