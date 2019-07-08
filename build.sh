mkdir bin
javac -sourcepath src/main/java:src/test/java -classpath lib/jSerialComm-2.5.1.jar src/main/java/com/hito4t/iot/Omron2JCIE_BU01.java -d bin src/test/java/com/hito4t/iot/Omron2JCIE_BU01Test.java
