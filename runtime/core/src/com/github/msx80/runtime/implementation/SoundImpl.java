package com.github.msx80.runtime.implementation;

import com.github.msx80.omicron.Sound;

public final class SoundImpl implements Sound {

	com.badlogic.gdx.audio.Sound sound;

	public SoundImpl(com.badlogic.gdx.audio.Sound sound) {
		this.sound = sound;
	}

}
