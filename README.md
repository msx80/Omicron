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

Omicron has a minimalistic and self-explanatory API.
Take a look [here](https://github.com/msx80/Omicron/blob/master/api/src/main/java/org/github/msx80/omicron/api/Sys.java) for the full API!

Get Started
-----------

Take a look at the [Hello World](https://github.com/msx80/Omicron/blob/master/demo/HelloWorld/hello-world/src/main/java/org/github/msx80/omicron/helloworld/HelloWorld.java) example to get started!

