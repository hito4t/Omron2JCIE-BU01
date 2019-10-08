package com.hito4t.iot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.fazecast.jSerialComm.*;

public class Omron2JCIE_BU01 implements AutoCloseable {

    private SerialPort port;
    private double temperature;
    private double humidity;
    private double light;
    private double noise;

    public Omron2JCIE_BU01(String portName) throws Exception {
        port = getPort(portName);
    }

    public void update() throws Exception {
        byte[] bytes = readDataShort(port);

        temperature = toShort(bytes, 1) * 0.01;
        humidity = toShort(bytes, 3) * 0.01;
        light = toShort(bytes, 5);
        noise = toShort(bytes, 11) * 0.01;
    }

    /**
     * Temperature (degC)
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Relative humidity (%)
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * Ambient light (lx)
     */
    public double getLight() {
        return light;
    }

    /**
     * Sound noise (dB)
     */
    public double getNoise() {
        return noise;
    }

    public void close() throws Exception {
        port.closePort();
    }

    private static byte[] readDataShort(SerialPort port) throws IOException {
        byte[] command = new byte[9];

        // Header
        command[0] = (byte)0x52;
        command[1] = (byte)0x42;
        // Length
        command[2] = (byte)0x05;
        command[3] = (byte)0x00;

        // Payload
        //   Command
        command[4] = (byte)0x01;
        //   Address (Latest data Short)
        command[5] = (byte)0x22;
        command[6] = (byte)0x50;

        // CRC-16
        short crc = calculateCRC16(command, 0, 7);
        debug(String.format("CRC=%d", crc));
        command[7] = (byte)(crc & 0xFF);
        command[8] = (byte)(crc >> 8);

        port.writeBytes(command, command.length);

        byte[] responseHeader = new byte[7];
        port.readBytes(responseHeader, responseHeader.length);
        if (toShort(responseHeader, 0) != 0x4252) {
            throw new IOException(String.format("Illegal response header : %04X", toShort(responseHeader, 0)));
        }

        int length = toShort(responseHeader, 2);
        debug(String.format("Length = %d", length));
        debug(String.format("Command = %x", responseHeader[4]));
        if (responseHeader[4] == (byte)0x81) {
            byte[] errorCode = new byte[1];
            port.readBytes(errorCode, errorCode.length);
            throw new IOException(String.format("Read error : %02X", errorCode[0]));
        }


        byte[] bytes = new byte[length - 3];
        port.readBytes(bytes, bytes.length);
        return bytes;
    }

    private static short calculateCRC16(byte[] bytes, int fromIndex, int toIndex) {
        short crc = (short)0xFFFF;
        for (int i = fromIndex; i < toIndex; i++) {
            byte b = bytes[i];
            crc ^= b;
            for (int j = 0; j < 8; j++) {
                int lsb = (crc & 1);
                crc = (short)((crc >> 1) & 0x7FFF);
                if (lsb == 1) {
                    crc ^= 0xA001;
                }
            }
        }
        return crc;
    }

    private static short toShort(byte[] bytes, int index) {
        return (short)((bytes[index] & 0xFF) | ((bytes[index + 1] & 0xFF) << 8));
    }


    private SerialPort getPort(String portName) throws Exception {
        List<String> portNames = new ArrayList<String>();
        for (SerialPort port : SerialPort.getCommPorts()) {
            if (port.getSystemPortName().equals(portName)) {
                port.setBaudRate(115200);
                if (!port.openPort(1000)) {
                    throw new Exception("Cannot open port.");
                }
                port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 1000);
                return port;
            } else {
                portNames.add(port.getSystemPortName());
            }
        }
        throw new Exception("Port \"" + portName + "\" is not found (found ports = " + portNames + ").");
    }

    private static void debug(String line) {
        System.out.println(line);
    }
}
