package com.hito4t.iot;


public class Omron2JCIE_BU01Test {
    public static void main(String[] args) throws Exception {
        Omron2JCIE_BU01 sensor = new Omron2JCIE_BU01(args[0]);
        for (int i = 0; i < 5; i++) {
            sensor.update();
            System.out.println(String.format("Temperature : %.2f degC", sensor.getTemperature()));
            System.out.println(String.format("Relative humidity : %.2f %%", sensor.getHumidity()));
            System.out.println(String.format("Ambient light : %.0f lx", sensor.getLight()));
            System.out.println(String.format("Sound noise : %.2f dB", sensor.getNoise()));

            Thread.sleep(1000);
        }
        sensor.close();
    }
}
