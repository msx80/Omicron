package com.github.msx80.omicron;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/** Simple scissor util based on Libgdx original
 * 
 */
public class ScissorStack {
	
	static Vector3 tmp = new Vector3();
	static final Rectangle viewport = new Rectangle();


	public static boolean setScissors (Rectangle scissor) {
		fix(scissor);
		
		if (scissor.width < 1 || scissor.height < 1) return false;
		 
		HdpiUtils.glScissor((int)scissor.x, (int)scissor.y, (int)scissor.width, (int)scissor.height);
		return true;
	}

	/*
	public static Rectangle resetScissors () {
		Rectangle old = scissors.pop();
		if (scissors.size == 0)
		{
			
		}
		else {
			Rectangle scissor = scissors.peek();
			HdpiUtils.glScissor((int)scissor.x, (int)scissor.y, (int)scissor.width, (int)scissor.height);
		}
		return old;
	}
*/
	private static void fix (Rectangle rect) {
		rect.x = Math.round(rect.x);
		rect.y = Math.round(rect.y);
		rect.width = Math.round(rect.width);
		rect.height = Math.round(rect.height);
		if (rect.width < 0) {
			rect.width = -rect.width;
			rect.x -= rect.width;
		}
		if (rect.height < 0) {
			rect.height = -rect.height;
			rect.y -= rect.height;
		}
	}

	/** Calculates a scissor rectangle using 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight() as the viewport.
	 * @see #calculateScissors(Camera, float, float, float, float, Matrix4, Rectangle, Rectangle) */
	public static void calculateScissors (Camera camera, Matrix4 batchTransform, Rectangle area, Rectangle scissor) {
		calculateScissors(camera, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), batchTransform, area, scissor);
	}

	/** Calculates a scissor rectangle in OpenGL ES window coordinates from a {@link Camera}, a transformation {@link Matrix4} and
	 * an axis aligned {@link Rectangle}. The rectangle will get transformed by the camera and transform matrices and is then
	 * projected to screen coordinates. Note that only axis aligned rectangles will work with this method. If either the Camera or
	 * the Matrix4 have rotational components, the output of this method will not be suitable for
	 * {@link GL20#glScissor(int, int, int, int)}.
	 * @param camera the {@link Camera}
	 * @param batchTransform the transformation {@link Matrix4}
	 * @param area the {@link Rectangle} to transform to window coordinates
	 * @param scissor the Rectangle to store the result in */
	public static void calculateScissors (Camera camera, float viewportX, float viewportY, float viewportWidth,
		float viewportHeight, Matrix4 batchTransform, Rectangle area, Rectangle scissor) {
		tmp.set(area.x, area.y, 0);
		tmp.mul(batchTransform);
		camera.project(tmp, viewportX, viewportY, viewportWidth, viewportHeight);
		scissor.x = tmp.x;
		scissor.y = tmp.y;

		tmp.set(area.x + area.width, area.y + area.height, 0);
		tmp.mul(batchTransform);
		camera.project(tmp, viewportX, viewportY, viewportWidth, viewportHeight);
		scissor.width = tmp.x - scissor.x;
		scissor.height = tmp.y - scissor.y;
	}

	/** @return the current viewport in OpenGL ES window coordinates based on the currently applied scissor */
	/*
	public static Rectangle getViewport () {
		if (scissors.size == 0) {
			viewport.set(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			return viewport;
		} else {
			Rectangle scissor = scissors.peek();
			viewport.set(scissor);
			return viewport;
		}
	}*/
}
