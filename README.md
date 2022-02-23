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

![Feature Demo example](https://i.imgur.com/VYVhZtv.png)

Feature Demo example


![A screenshot from the demo](https://i.imgur.com/esxGpDW.png)

A screenshot from the demo game, Alien Buster

![Doors of Doom](https://i.imgur.com/GoCecbG.png)

A videogame developed with Omicron, **Doors of Doom**, available on [Play Store](https://play.google.com/store/apps/details?id=org.github.msx80.doorsofdoom.DoorsOfDoom)


How can I try it?
-----------------

1. Build the omicron-player project (`cd omicron-player\`, `mvn clean package`)
2. Run the player jar in omicron-assembly/target (`java -jar omicron-assembly-0.0.3-jar-with-dependencies.jar`)
3. Build some cartridges:
4. `cd demo/HelloWorld` (or any other demo)
5. `gradlew build`
6. Open the cartridges within the player.

For android:

1. Go to android folder
2. Edit `omicron.properties` to have it point to your cartridge
3. Run `gradlew android:installDebug android:run`

The API
-------

Omicron has a minimalistic and self-explanatory API.
Take a look at it [here](https://github.com/msx80/Omicron/blob/master/omicron-player/omicron-api/src/main/java/org/github/msx80/omicron/api/Sys.java) for the full API!

Get Started
-----------

Take a look at the [Hello World](https://github.com/msx80/Omicron/blob/master/demo/HelloWorld/src/main/java/org/github/msx80/omicron/helloworld/HelloWorld.java) example to get a feeling!

Then head to the [wiki](https://github.com/msx80/Omicron/wiki) for some documentation!
