package dk.emad.humidsensor;

import dk.emad.humidsensor.sensor.DHT22;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(App.class, args);
		DHT22 dht22 = new DHT22();
		dht22.run();
		while (true) {
			log.info("dht22 data: humid: {}, temp: {}", dht22.getHumidity(), dht22.getTemperature());
			Thread.sleep(1000);
		}
	}

}
