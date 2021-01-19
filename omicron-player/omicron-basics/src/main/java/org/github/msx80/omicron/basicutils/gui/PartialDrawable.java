package org.github.msx80.omicron.basicutils.gui;

/**
 * A widget that is able to draw only a portion of itself.
 * It is allowed to print outside the given area is it's easier
 * ie a list could print all items that falls within the region
 *
 */
public interface PartialDrawable {
	void draw(int fromx, int fromy, int w, int h);
}
