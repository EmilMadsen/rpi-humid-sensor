package dk.emad.humidsensor.sensor;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DHT22 implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DHT22.class);

    private static final int maxTimings = 85;
    private final int[] dht22_dat = {0, 0, 0, 0, 0};
    private float temperature = 9999;
    private float humidity = 9999;
    boolean shuttingDown = false;


    public DHT22() {
        log.info("Setting up setup wiringPi GPIO");
        if (Gpio.wiringPiSetup() == -1) {
            log.error(" ==>> GPIO SETUP FAILED");
            return;
        }
        GpioUtil.export(3, GpioUtil.DIRECTION_OUT);
    }

    private int pollDHT22() {
        int lastState = Gpio.HIGH;
        int j = 0;
        dht22_dat[0] = dht22_dat[1] = dht22_dat[2] = dht22_dat[3] = dht22_dat[4] = 0;

        int pinNumber = 16;
        Gpio.pinMode(pinNumber, Gpio.OUTPUT);
        Gpio.digitalWrite(pinNumber, Gpio.LOW);
        Gpio.delay(18);

        Gpio.digitalWrite(pinNumber, Gpio.HIGH);
        Gpio.pinMode(pinNumber, Gpio.INPUT);

        for (int i = 0; i < maxTimings; i++) {
            int counter = 0;
            while (Gpio.digitalRead(pinNumber) == lastState) {
                counter++;
                Gpio.delayMicroseconds(1);
                if (counter == 255) {
                    break;
                }
            }

            lastState = Gpio.digitalRead(pinNumber);

            if (counter == 255) {
                break;
            }

            /* ignore first 3 transitions */
            if (i >= 4 && i % 2 == 0) {
                /* shove each bit into the storage bytes */
                dht22_dat[j / 8] <<= 1;
                if (counter > 16) {
                    dht22_dat[j / 8] |= 1;
                }
                j++;
            }
        }
        return j;

    }

    private void refreshData() {

        int pollDataCheck = pollDHT22();
        log.debug("polled dht22 - timing: {} - storage bytes: {}", pollDataCheck, dht22_dat);
        if (pollDataCheck >= 40 && checkParity()) {

            final float newHumidity = (float) ((dht22_dat[0] << 8) + dht22_dat[1]) / 10;
            final float newTemperature = (float) (((dht22_dat[2] & 0x7F) << 8) + dht22_dat[3]) / 10;

            if (humidity == 9999 || ((newHumidity < humidity + 40) && (newHumidity > humidity - 40))) {
                humidity = newHumidity;
                if (humidity > 100) {
                    humidity = dht22_dat[0]; // for DHT22
                }
            }

            if (temperature == 9999 || ((newTemperature < temperature + 40) && (newTemperature > temperature - 40))) {
                temperature = (float) (((dht22_dat[2] & 0x7F) << 8) + dht22_dat[3]) / 10;
                if (temperature > 125) {
                    temperature = dht22_dat[2]; // for DHT22
                }
                if ((dht22_dat[2] & 0x80) != 0) {
                    temperature = -temperature;
                }
            }

            log.debug("humidity: {} - temp: {}", humidity, temperature);
        }
    }


    public float getHumidity() {
        if (humidity == 9999) {
            return 0;
        }
        return humidity;
    }

    public float getTemperature() {
        if (temperature == 9999) {
            return 0;
        }
        return temperature;
    }

    private boolean checkParity() {
        return dht22_dat[4] == (dht22_dat[0] + dht22_dat[1] + dht22_dat[2] + dht22_dat[3] & 0xFF);
    }

    @Override
    public void run() {
        while (!shuttingDown) {
            refreshData();
        }
    }
}
