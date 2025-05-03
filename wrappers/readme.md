These are wrappers to create desktop or android app from an omicron cartridge (that has been "mvn install"ed)

For android, the command is:
./gradlew -Pcartridge.artifactId=yourArtifact -Pcartridge.groupId=your.group.id -Pcartridge.version=0.0.6 clean assembleDebug

For desktop, the comman is:

./mvnw -Dcartridge.artifactId=yourArtifact -Dcartridge.groupId=your.group.id -Dcartridge.version=0.0.6 clean package