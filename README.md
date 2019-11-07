Omicron
======
Omicron is an open source Game Engine for Java based on LibGDX, inspired by Fantasy Consoles. Write your retro games with the simplest possible API and either package it as a cartridge, or include the runtime in your own distribution!

Features:

1. Super simple API, just a couple of methods for drawing, playing sound, handling input, etc.
2. No resource management: just refer to the thing you want to draw or play by it's number.
3. Work on desktop and android
4. Plaform independent API
5. Customizable resolution

![Hello Wolrd example](https://i.imgur.com/VYVhZtv.png)

Hello Wolrd example


![A screenshot from the demo](https://i.imgur.com/esxGpDW.png)

A screenshot from the demo game, Alien Buster

![Doors of Doom](https://i.imgur.com/GoCecbG.png)

A videogame developed with Omicron.


How can I try it?
-----------------

1. Build the omicron-api and omicron-engine  project (`mvn clean install`)
2. Build the helloworld demo project (`mvn clean install`)
3. Build the helloworld-desktop project (`mvn clean install assembly:single`)
4. Run the output jar (`java -jar omicron-demo-desktop-0.0.1-jar-with-dependencies.jar`)

For android:

1. Go to android folder and run `gradlew android:installDebug android:run`

The API
-------

Here's Omicron minimalistic and self-explaining API:

```java
package org.github.msx80.omicron.api;

public interface Sys 
{
	void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h, int rotate, int flip);
	int newSurface(int w, int h);
	int getPix(int sheetNum, int x, int y);
	void fill(int sheetNum, int x, int y, int w, int h, int color);
	void offset(int x, int y);
	void clear(int color);
	void color(int color);
	int fps();
	String mem(String key);
	void mem(String key, String value);
	Mouse mouse();
	Controller[] controllers();
	void sound(int soundNum, float volume, float pitch);
	void music(int musicNum, float volume, boolean loop);
	void stopMusic();
	String hardware(String module, String command, String param);
}


```

Hello World
-----------

Take a look at the [Hello World](https://github.com/msx80/Omicron/blob/master/demo/HelloWorld/omicron-demo/src/main/java/org/github/msx80/omicron/HelloWorld.java) example to get started!

