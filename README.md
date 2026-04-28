DSASter — Disk Scheduling Algorithm SimulatER
==============================================

REQUIREMENTS:
- Java JDK 11 or later

HOW TO COMPILE (from project root):
  javac -d out/production/DSASter src/**/*.java

  Or using the manifest:
  javac -sourcepath src -d out src/main/Main.java

HOW TO RUN:
  java -cp out/production/DSASter main.Main

HOW TO CREATE JAR:
  jar cfm DSASter.jar MANIFEST.MF -C out/production/DSASter .

  MANIFEST.MF content:
  Main-Class: main.Main

HOW TO CONVERT JAR TO EXE:
  Use Launch4j (https://launch4j.sourceforge.net/) or JSmooth.
  Point the tool to DSASter.jar and configure the output as DSASter.exe.