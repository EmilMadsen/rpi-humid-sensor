package dk.emad.humidsensor.service;

import com.google.gson.Gson;
import dk.emad.humidsensor.sensor.DHT22;
import dk.emad.humidsensor.sensor.HumidSensorLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Persist current humidity and temperature readings with given interval
 */

@Service
public class PersistinatorService {
    private static final Logger log = LoggerFactory.getLogger(PersistinatorService.class);

    @Value("${api.home}") private String apiHome;

    private DHT22 dht22;
    private final TaskExecutor taskExecutor;
    private final RestTemplate restTemplate;
    private final Gson gson;

    public PersistinatorService(TaskExecutor taskExecutor, RestTemplate restTemplate, Gson gson) {
        this.restTemplate = restTemplate;
        this.gson = gson;
        this.dht22 = new DHT22();
        this.taskExecutor = taskExecutor;
    }

    @PostConstruct
    public void init() {
        log.info("init sensor");
        taskExecutor.execute(dht22);
    }

    @Scheduled(fixedDelay = 5000)
    public void logReading() {
        log.info("dht22 data: humid: {}, temp: {}", dht22.getHumidity(), dht22.getTemperature());
    }

    @Scheduled(fixedDelay = 1000 * 60 * 2)
    public void storeReading() {
        log.info("storing dht22 data: humid: {}, temp: {}", dht22.getHumidity(), dht22.getTemperature());
        if (dht22.getHumidity() > 0 || dht22.getTemperature() > 0) {
            HumidSensorLog log = new HumidSensorLog((double) dht22.getHumidity(), (double) dht22.getTemperature());
            String url = apiHome + "/api/sensor";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(gson.toJson(log), headers);
            restTemplate.postForObject(url, entity, String.class);
        }
    }



}
