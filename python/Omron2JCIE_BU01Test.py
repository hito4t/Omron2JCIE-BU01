import time
import Omron2JCIE_BU01

sensor = Omron2JCIE_BU01.Omron2JCIE_BU01("/dev/ttyUSB0")

for i in range(0, 5):
    sensor.update()

    print "Temperature : " + str(sensor.temperature) + " degC"
    print "Relative humidity : " + str(sensor.humidity) + " %"
    print "Ambient light : " + str(sensor.light) + " lx"
    print "Sound noise : " + str(sensor.noise) + " dB"

    time.sleep(1)

sensor.close()
