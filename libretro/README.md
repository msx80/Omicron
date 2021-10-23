This is an experimental Libretro core for Omicron.

HOW TO
======

Build the C project in `omicron/libretro/LibretroOmicronCore` with the provided `Makefile`. 
It should generate a file named `omicron_libretro.dll` (or .so)

move it to `yourpath/RetroArch/cores`

Go to `omicron/omicron-player` and build the player:

`mvn clean package`

it generates a file named `omicron-assembly-0.0.3-jar-with-dependencies.jar` in `omicron-assembly/target/`

move and rename it to:

`yourpath\RetroArch\system\omicron\omicron.jar`

Set the JAVA_HOME environment variable and run retroarch executable. It needs to see the JAVA_HOME environment variable to locate the jvm.

`set JAVA_HOME=C:\Program Files\java\jdk1.8.0_191`
`retroarch.exe`

Alternatively you can run a cartridge directly like so:

`retroarch_debug.exe -L yourpath\omicron_libretro.dll yourpath\SnakeMain.omicron`
