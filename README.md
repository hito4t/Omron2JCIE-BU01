# Data Reader for OMRON Sensor 2JCIE-BU01 (Python, Java)

## 概要

OMRONの環境センサー2JCIE-BU01からデータ（温度、湿度、照度、騒音）を取得するためのクラスです。
Python版とJava版があります。

センサーはUSB接続ですが、シリアル通信によりデータを取得します。
あらかじめOMRONのマニュアルに従ってドライバをインストールしてください。

Python版はpySerial、Java版はjSerialCommを利用しています。


## 使用方法


以下を参考にしてください。

Python : python/Omron2JCIE_BU01Test.py

Java: src/test/java/com/hito4t/iot/Omron2JCIE_BU01Test.java

