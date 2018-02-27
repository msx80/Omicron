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

