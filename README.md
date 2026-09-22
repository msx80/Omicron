Omicron
======
![Build](https://github.com/msx80/Omicron/workflows/Build/badge.svg)
[![Contributors](https://img.shields.io/github/contributors/msx80/omicron.svg)](https://github.com/msx80/omicron/graphs/contributors)

Omicron is an open source Game Engine for Java based on LibGDX, inspired by Fantasy Consoles. Write your retro games with the simplest possible API and either package it as a cartridge, or include the runtime in your own distribution!

Features:

1. [Super simple API](https://github.com/msx80/omicron-api), just a bunch of methods for drawing, playing sound, handling input, etc.
2. No resource management: just refer to the thing you want to draw or play by it's number.
3. Work on desktop and android (there's also an experimental libretro core)
4. Plaform independent API
5. Customizable resolution and scaling

# Included demos

**Feature Demo**, showing off most of the features:  

[<img height='80' alt='Play in your browser' src='https://cdn.prod.website-files.com/62f10984b7ece0cd68b339a1/634414e97c8fd43650e0f799_play-in-browser.svg'/>](https://msx80.github.io/Omicron/featuredemo)

![Feature Demo example](https://i.imgur.com/VYVhZtv.png)


**Alien Buster**, an included micro game:  

[<img height='80' alt='Play in your browser' src='https://cdn.prod.website-files.com/62f10984b7ece0cd68b339a1/634414e97c8fd43650e0f799_play-in-browser.svg'/>](https://msx80.github.io/Omicron/alienbuster)

![A screenshot from the demo](https://i.imgur.com/esxGpDW.png)

**Snake** a complete Snake game:  

[<img height='80' alt='Play in your browser' src='https://cdn.prod.website-files.com/62f10984b7ece0cd68b339a1/634414e97c8fd43650e0f799_play-in-browser.svg'/>](https://msx80.github.io/Omicron/snake)

<img width="868" height="524" alt="image" src="https://github.com/user-attachments/assets/f57d2ada-f7fd-40dc-9941-da1a9a83875f" />

# Real games:

An open source videogame developed with Omicron, **Doors of Doom**, available on [FDroid](https://f-droid.org/packages/com.github.msx80.doorsofdoom/) - ([sources](https://github.com/msx80/DoorsOfDoomOmicron)):

![Doors of Doom](https://i.imgur.com/GoCecbG.png)

[Retrodrawing](https://github.com/msx80/retrodrawing), a simple drawing app with big pixels:  
![Retrodrawing](https://github.com/msx80/RetroDrawing/raw/main/fastlane/metadata/android/en-US/images/phoneScreenshots/1.jpg?raw=true)

[Turns of War](https://github.com/msx80/turnsofwar) an open source turn based strategy game made with Omicron:  
<img width="868" height="524" alt="image" src="https://raw.githubusercontent.com/msx80/turnsofwar/main/fastlane/metadata/android/en-US/images/phoneScreenshots/4.png" />

Architecture
------------

Omicron can work as a game engine (as a dependency in a standalone project) or as a Fantasy Console (Omicron itself is executed and it runs omicron cartridges).

Cartridges are maven projects with a standardized pom.xml which can be run on the fly or produces an `.omicron` cartridge file.

A maven archetype is available to produce a preconfigured cartridge projects ready to run.

Some [wrappers](https://github.com/msx80/Omicron/tree/master/wrappers) are available that take a cartridge and produce an android, web or desktop app.


How can I try it?
-----------------

**Quickest start**:

1. Build and install the omicron-player project:
  - `cd omicron-player\`
  - `mvn clean install package`
3. Run a demo:
  - `cd demo/snake`
  - `mvn clean compile exec:exec`

You can also run Omicron itself as a **Fantasy Console**:

1. Generate a demo cartridge
  - `cd demo/snake`
  - `mvn clean package`
3. Run the player jar in omicron-assembly/target:
  - `java -jar omicron.jar`
4. Open a cartrigde (demo/snake/snake.omicron)

**Generate your own cartridge**:

You can generate a preconfigured, ready to run project with the following command:

- `mvn archetype:generate -DarchetypeGroupId=com.github.msx80.omicron  -DarchetypeArtifactId=omicron-archetype -DarchetypeVersion=0.0.13-SNAPSHOT`

Then run it with:

- `cd mygame`
- `mvn clean compile exec:exec`


The API
-------

Omicron has a minimalistic and self-explanatory API.
Take a look at it [here](https://github.com/msx80/omicron-api/blob/main/src/main/java/com/github/msx80/omicron/api/Sys.java) for the full API!

Get Started
-----------

Take a look at the [Hello World](https://github.com/msx80/Omicron/tree/master/demo/HelloWorld) example to get a feeling!

Then head to the [wiki](https://github.com/msx80/Omicron/wiki) for some documentation!
