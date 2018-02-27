Omicron
======
Omicron is an open source Game Engine/Fantasy Console for Java based on LibGDX. Write your retro games with the simplest possible API and either package it as a cartridge, or include the runtime in your own distribution!

Why should I use it?
--------------------

Becouse it's fun! And also:

- Runs on plenty of platforms (including Raspbian/Retropie!)
- Generalized gamepad configuration
- Sandboxed cartridge execution
- Incredibly simple API
- Open source

How can I try it?
-----------------

1. Build the omicron-api project (mvn clean install)
2. Build the HelloWorld cartridge (or your own) (gradlew build)
3. Run the cartridge (java -jar omicron.jar Helloworld.omicron)

The API
-------

Here's Omicron minimalistic and self-explaining API:

```java
package com.github.msx80.omicron;

public interface Sys {
	public static final int SCREEN_WIDTH = 384;
	public static final int SCREEN_HEIGHT = 192;
	
	Image loadImage(String filename);
	Image[][] loadSpritesheet(String filename, int sizex, int sizey);
	Sound loadSound(String filename);
	void clearScreen(float red, float green, float blue);
	void color(float red, float green, float blue, float alpha);
	void draw(Image image, int x, int y);
	void draw(Image image, int x, int y, boolean flipX, boolean flipY);
	void draw(Image image, int x, int y, boolean flipX, boolean flipY, int width, int height);
	void draw(Image image, int x, int y, boolean flipX, boolean flipY, int width, int height, int angle);
	void write(Image[][] font, int x, int y, String text);
	void offset(int x, int y);
	void playSound(Sound sound);
	int availableControllers();
	Controller controller(int index);
	Mouse mouse();
	void dispose(Image image);
	void dispose(Sound sound);
	void exit(Object returnValue);
}
```

Hello World
-----------

Here's the minimal Hello World example, a controlled sprite with a background:

```java
package omicron.demo;

import com.github.msx80.omicron.*;

public class HelloWorld implements Game {

	Image background, sprite;
	int x = 100, y = 100;
	Sys sys;
	
	public void init(Sys sys) 
	{
		this.sys = sys;
		background = sys.loadImage("stars.png");
		sprite = sys.loadImage("ufo.png");
	}

	public void update() 
	{
		Controller c = sys.controller(0);
		y += c.up() ? -2 : c.down() ? 2 : 0;
		x += c.left() ? -2 : c.right() ? 2 : 0;
	}

	public void render() {
		sys.draw(background, 0, 0);
		sys.draw(sprite, x, y);
		sys.color(0.5f, 1, 0, 1);
		sys.write(null, 5, 5, "Very simple movement demo. Have fun!");
	}

	public void close() {		
	}
}
```